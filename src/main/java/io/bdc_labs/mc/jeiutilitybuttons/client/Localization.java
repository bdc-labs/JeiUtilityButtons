package io.bdc_labs.mc.jeiutilitybuttons.client;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.MagnetModeHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by universal on 20.01.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class Localization {

    private static final String PREFIX = JeiUtilityButtons.MODID + ".";

    public static final String SWITCH_TO = PREFIX + "switchto";
    public static final String TIME_DAY = PREFIX + "timeday";
    public static final String TIME_NIGHT = PREFIX + "timenight";
    public static final String FREEZE_TIME = PREFIX + "freezetime";
    public static final String UNFREEZE_TIME = PREFIX + "unfreezetime";
    public static final String NO_MOBS = PREFIX + "nomobs";
    public static final String DELETE_ALL = PREFIX + "deleteall";
    public static final String DELETE_SINGLE = PREFIX + "deletesingle";
    public static final String IGNORE_META = PREFIX + "ignoringmeta";
    public static final String DRAG_ITEMS_HERE = PREFIX + "dragitemshere";
    public static final String HOLD_SHIFT = PREFIX + "holdshift";
    public static final String CLEAR_INVENTORY = PREFIX + "clearinventory";
    public static final String CUSTOM_COMMAND = PREFIX + "customcommand";
    public static final String SAVE = PREFIX + "save";
    public static final String LOAD = PREFIX + "load";
    public static final String NBT_TOO_LONG = PREFIX + "nbttoolong";
    public static final String MAGNET_ON = PREFIX + "magnet.on";
    public static final String MAGNET_OFF = PREFIX + "magnet.off";
    public static final String MAGNET = PREFIX + "magnetitems";
    public static final String MODS = PREFIX + "mods";
    public static final String NO_PERMISSIONS = "commands.help.failed";
    public static final String MORE = PREFIX + "more";
    public static final String CMD_NO_RAIN = "commands.weather.set.clear";
    public static final String CMD_RAIN = "commands.weather.set.rain";
    public static final String KEY_CATEGORY = "key.category.justenoughbuttons";
    public static final String KEY_MAKECOPY = "justenoughbuttons.key.makecopy";
    public static final String KEY_MOBOVERLAY = "justenoughbuttons.key.moboverlay";
    public static final String KEY_CHUNKOVERLAY = "justenoughbuttons.key.chunkoverlay";
    public static final String KEY_HIDE_OVERLAY = "justenoughbuttons.key.hideall";

    public static List<TranslationTextComponent> getTooltip(EnumButtonCommands btn) {
        ArrayList<TranslationTextComponent> list = new ArrayList<TranslationTextComponent>();
        if (btn == null)
            return null;

        switch (btn) {
            case ADVENTURE:
                list.add(new TranslationTextComponent(I18n.get(Localization.SWITCH_TO, I18n.get("gameMode.adventure"))));
                break;
            case CREATIVE:
                list.add(new TranslationTextComponent(I18n.get(Localization.SWITCH_TO, I18n.get("gameMode.creative"))));
                break;
            case SPECTATE:
                list.add(new TranslationTextComponent(I18n.get(Localization.SWITCH_TO, I18n.get("gameMode.spectator"))));
                break;
            case SURVIVAL:
                list.add(new TranslationTextComponent(I18n.get(Localization.SWITCH_TO, I18n.get("gameMode.survival"))));
                break;
            case DAY:
                list.add(new TranslationTextComponent(I18n.get(Localization.SWITCH_TO, I18n.get(Localization.TIME_DAY))));
                break;
            case NIGHT:
                list.add(new TranslationTextComponent(I18n.get(Localization.SWITCH_TO, I18n.get(Localization.TIME_NIGHT))));
                break;
            case DELETE:
                ItemStack draggedStack = ClientProxy.player.inventory.getCarried();
                if (!draggedStack.isEmpty()) {
                    if (JeiUtilityButtons.isServerSidePresent) {
                        list.add(new TranslationTextComponent(I18n.get(Localization.DELETE_SINGLE, I18n.get(draggedStack.getDescriptionId()))));
                    } else {
                        list.add(new TranslationTextComponent(I18n.get(Localization.DELETE_ALL, I18n.get(draggedStack.getDescriptionId()))));
                        if (Screen.hasShiftDown())
                            list.add(new TranslationTextComponent(TextFormatting.GRAY + I18n.get(Localization.IGNORE_META)));
                    }

                } else {
                    list.add(new TranslationTextComponent(I18n.get(Localization.DRAG_ITEMS_HERE)));
                    if (!JeiUtilityButtons.isServerSidePresent)
                        list.add(new TranslationTextComponent(TextFormatting.GRAY + I18n.get(Localization.HOLD_SHIFT)));
                    if (ConfigHandler.COMMON.enableClearInventory.get())
                        list.add(new TranslationTextComponent(TextFormatting.GRAY + I18n.get(Localization.CLEAR_INVENTORY)));
                }
                break;
            case FREEZETIME:
                boolean gameRuleDayCycle = ClientProxy.mc.level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
                if (gameRuleDayCycle)
                    list.add(new TranslationTextComponent(I18n.get(Localization.FREEZE_TIME)));
                else
                    list.add(new TranslationTextComponent(I18n.get(Localization.UNFREEZE_TIME)));
                break;
            case NOMOBS:
                list.add(new TranslationTextComponent(I18n.get(Localization.NO_MOBS)));
                break;
            case RAIN:
                list.add(new TranslationTextComponent(I18n.get(Localization.CMD_RAIN)));
                break;
            case SUN:
                list.add(new TranslationTextComponent(I18n.get(Localization.CMD_NO_RAIN)));
                break;
            case MAGNET:
                if (JeiUtilityButtons.isServerSidePresent) {
                    if (MagnetModeHandler.state)
                        list.add(new TranslationTextComponent(I18n.get(Localization.MAGNET_OFF)));
                    else
                        list.add(new TranslationTextComponent(I18n.get(Localization.MAGNET_ON)));
                } else
                    list.add(new TranslationTextComponent(I18n.get(Localization.MAGNET)));

                break;
        }

        return list;
    }
}
