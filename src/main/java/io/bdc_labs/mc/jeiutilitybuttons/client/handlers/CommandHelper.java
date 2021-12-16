package io.bdc_labs.mc.jeiutilitybuttons.client.handlers;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.client.ClientProxy;
import io.bdc_labs.mc.jeiutilitybuttons.client.EnumButtonCommands;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.MagnetModeHandler;
import io.bdc_labs.mc.jeiutilitybuttons.core.network.MessageExecuteButton;
import io.bdc_labs.mc.jeiutilitybuttons.core.network.MessageMagnetMode;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;

public class CommandHelper {

    public static boolean useCheats = false;

    public static void handleClick(EnumButtonCommands btn) {
        String[] command = null;
        switch (btn) {
            case CREATIVE:
                handleButton(MessageExecuteButton.GM_CREATIVE, btn.getCommand().split(" "));
                break;
            case ADVENTURE:
                handleButton(MessageExecuteButton.GM_ADVENTURE, btn.getCommand().split(" "));
                break;
            case SURVIVAL:
                handleButton(MessageExecuteButton.GM_SURVIVAL, btn.getCommand().split(" "));
                break;
            case SPECTATE:
                handleButton(MessageExecuteButton.GM_SPECTATE, btn.getCommand().split(" "));
                break;
            case DELETE:
                ItemStack draggedStack = ClientProxy.player.inventoryMenu.getCarried();
                if (draggedStack.isEmpty()) {
                    if (Screen.hasShiftDown() && ConfigHandler.COMMON.enableClearInventory.get())
                        command = new String[]{"clear", "@p"};
                } else {
                    String name  = draggedStack.getItem().getRegistryName().toString();
                    command = new String[]{"clear", "@p", name};
                    boolean ghost = draggedStack.hasTag() && draggedStack.getTag().getBoolean("JEI_Ghost");
                    if (ghost)
                        ClientProxy.player.inventoryMenu.setCarried(ItemStack.EMPTY);
                }

                if (JeiUtilityButtons.isServerSidePresent) {
                    ClientProxy.player.inventoryMenu.setCarried(ItemStack.EMPTY);
                    if (Screen.hasShiftDown() && ConfigHandler.COMMON.enableClearInventory.get())
                        ClientProxy.player.getInventory().clearContent();
                }

                if (Screen.hasShiftDown() && ConfigHandler.COMMON.enableClearInventory.get())
                    handleButton(MessageExecuteButton.DELETE_ALL, command);
                else if (!draggedStack.equals(ItemStack.EMPTY))
                    handleButton(MessageExecuteButton.DELETE, command);
                break;
            case RAIN:
                handleButton(MessageExecuteButton.RAIN, btn.getCommand().split(" "));
                break;
            case SUN:
                handleButton(MessageExecuteButton.SUN, btn.getCommand().split(" "));
                break;
            case DAY:
                handleButton(MessageExecuteButton.DAY, btn.getCommand().split(" "));
                break;
            case NIGHT:
                handleButton(MessageExecuteButton.NIGHT, btn.getCommand().split(" "));
                break;
            case FREEZETIME:
                boolean gameRuleDayCycle = ClientProxy.mc.level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
                command = new String[] { "gamerule", "doDaylightCycle", (gameRuleDayCycle ? "false" : "true")};
                handleButton(MessageExecuteButton.FREEZE, command);
                break;
            case NOMOBS:
                handleButton(MessageExecuteButton.KILL, btn.getCommand().split(" "));
                break;
            case MAGNET:
                command = new String[]{"tp", "@e[type=minecraft:item,distance=.." + ConfigHandler.COMMON.magnetRadius.get() + "]", "@p"};
                    handleButton(MessageExecuteButton.MAGNET, command);
                break;
        }
    }

    private static void handleButton(int msgId, String[] args) {
        if (msgId != MessageExecuteButton.MAGNET) {
            ClientProxy.network.sendToServer(new MessageExecuteButton(msgId, args));
        } else {
            ClientProxy.network.sendToServer(new MessageMagnetMode(MagnetModeHandler.state));
            MagnetModeHandler.state = !MagnetModeHandler.state;
        }
    }
}
