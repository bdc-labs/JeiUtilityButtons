package io.bdc_labs.mc.jeiutilitybuttons.core.network;


import io.bdc_labs.mc.jeiutilitybuttons.client.Localization;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.network.NetworkEvent;

import java.util.Iterator;

/**
 * Server side implementation for JUB buttons
 */
public class MessageExecuteButton implements IMessage {

    public static final byte GM_CREATIVE  = 0;
    public static final byte GM_ADVENTURE = 1;
    public static final byte GM_SURVIVAL  = 2;
    public static final byte GM_SPECTATE  = 3;
    public static final byte DELETE       = 4;
    public static final byte RAIN         = 5;
    public static final byte SUN          = 6;
    public static final byte DAY          = 7;
    public static final byte NIGHT        = 8;
    public static final byte FREEZE       = 9;
    public static final byte KILL         = 10;
    public static final byte MAGNET       = 11;
    public static final byte DELETE_ALL   = 12;
    public int commandOrdinal;
    public String[] cmd;

    public MessageExecuteButton(int cmdId, String[] cmd) {
        this.commandOrdinal = cmdId;
        this.cmd = cmd != null ? cmd : new String[] { "" };
    }

    public static boolean checkPermissions(ServerPlayer player, MinecraftServer server) {
        if (server.isSingleplayer())
            return true;
        return server.getOperatorUserPermissionLevel() == server.getProfilePermissions(player.getGameProfile());
    }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        ServerPlayer p = context.getSender();

        if (p == null)
            return false;
        ServerLevel world = p.getLevel();
        MinecraftServer s = p.server;
        ServerLevelData worldinfo = (ServerLevelData) world.getLevelData();
        boolean isOP = checkPermissions(p, s);
        boolean error = true;

        switch (commandOrdinal) {
            case GM_ADVENTURE:
                if (!isOP && ConfigHandler.COMMON.gamemodeRequiresOP.get())
                    break;

                error = false;
                p.setGameMode(GameType.ADVENTURE);
                break;
            case GM_CREATIVE:
                if (!isOP && ConfigHandler.COMMON.gamemodeRequiresOP.get())
                    break;

                error = false;
                p.setGameMode(GameType.CREATIVE);
                break;
            case GM_SURVIVAL:
                if (!isOP && ConfigHandler.COMMON.gamemodeRequiresOP.get())
                    break;

                error = false;
                p.setGameMode(GameType.SURVIVAL);
                break;
            case GM_SPECTATE:
                if (!isOP && ConfigHandler.COMMON.gamemodeRequiresOP.get())
                    break;

                error = false;
                p.setGameMode(GameType.SPECTATOR);
                break;
            case DELETE_ALL:
                if (!isOP && ConfigHandler.COMMON.deleteRequiresOP.get())
                    break;

                error = false;
                p.getInventory().clearContent();
                break;
            case DELETE:
                if (!isOP && ConfigHandler.COMMON.deleteRequiresOP.get())
                    break;

                error = false;
                p.inventoryMenu.setCarried(ItemStack.EMPTY);
                break;
            case SUN:
                if (!isOP && ConfigHandler.COMMON.weatherRequiresOP.get())
                    break;

                error = false;
                worldinfo.setClearWeatherTime(1000000);
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
                break;
            case RAIN:
                if (!isOP && ConfigHandler.COMMON.weatherRequiresOP.get())
                    break;

                error = false;
                worldinfo.setClearWeatherTime(0);
                worldinfo.setRainTime(1000000);
                worldinfo.setThunderTime(1000000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(false);
                break;
            case DAY:
                if (!isOP && ConfigHandler.COMMON.timeRequiresOP.get())
                    break;

                error = false;
                worldinfo.setDayTime(1000);
                break;
            case NIGHT:
                if (!isOP && ConfigHandler.COMMON.timeRequiresOP.get())
                    break;

                error = false;
                world.setDayTime(13000);
                break;
            case FREEZE:
                if (!isOP && ConfigHandler.COMMON.timeFreezeRequiresOP.get())
                    break;

                error = false;
                boolean origValue = worldinfo.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
                worldinfo.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(!origValue, s);
                break;
            case KILL:
                if (!isOP && ConfigHandler.COMMON.killMobsRequiresOP.get())
                    break;

                error = false;
                for (Iterator<Entity> e = world.getEntities().getAll().iterator(); e.hasNext(); ) {
                    Entity entity = e.next();

                    if (!(entity instanceof ServerPlayer) && entity instanceof LivingEntity || entity instanceof ItemEntity) {
                        world.removeEntity(entity, false);
                    }
                }

                break;
        }

        if (error) {
            TextComponent msg = (TextComponent) new TextComponent(Localization.NO_PERMISSIONS).withStyle(ChatFormatting.RED);
            p.sendMessage(msg, p.getUUID());
        }
        return !error;
    }

}
