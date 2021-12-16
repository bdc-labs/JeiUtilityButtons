package io.bdc_labs.mc.jeiutilitybuttons.core.network;

/**
 * Created by universal on 05.04.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.client.Localization;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Iterator;

/**
 *  Server side implementation for JUB buttons
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

    public static boolean checkPermissions(ServerPlayerEntity player, MinecraftServer server) {
        if (server.isSingleplayer())
            return true;
        return server.getOperatorUserPermissionLevel() == server.getProfilePermissions(player.getGameProfile());
    }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        ServerPlayerEntity p = context.getSender();

        if (p == null)
            return false;
        ServerWorld world = p.getLevel();
        MinecraftServer s = p.server;
        ServerWorldInfo worldinfo = (ServerWorldInfo) world.getLevelData();
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
                p.inventory.clearContent();
                break;
            case DELETE:
                if (!isOP && ConfigHandler.COMMON.deleteRequiresOP.get())
                    break;

                error = false;
                p.inventory.setCarried(ItemStack.EMPTY);
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
                for (Iterator<Entity> e = world.getEntities().iterator(); e.hasNext(); ) {
                    Entity entity = e.next();

                    if (!(entity instanceof PlayerEntity) && entity instanceof LivingEntity || entity instanceof ItemEntity) {
                        world.removeEntity(entity, false);
                    }
                }

                break;
        }

        if (error) {
            ITextComponent msg = new TranslationTextComponent(Localization.NO_PERMISSIONS).withStyle(TextFormatting.RED);
            p.sendMessage(msg, p.getUUID());
        }
        return !error;
    }

}
