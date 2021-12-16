package io.bdc_labs.mc.jeiutilitybuttons.core.network;

import io.bdc_labs.mc.jeiutilitybuttons.client.Localization;
import io.bdc_labs.mc.jeiutilitybuttons.core.CommonProxy;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/**
 * Created by universal on 20.09.2016 14:31.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENSE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JEI Buttons
 */
public class MessageMagnetMode implements IMessage {

    public boolean removePlayer;

    public MessageMagnetMode() { }

    public MessageMagnetMode(boolean remove) {
        removePlayer = remove;
    }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        ServerPlayer p = context.getSender();
        MinecraftServer s = p.getServer();
        if (s == null)
            return false;

        boolean isOP = MessageExecuteButton.checkPermissions(p, s);
        TextComponent msg = (TextComponent) new TextComponent(Localization.NO_PERMISSIONS).withStyle(ChatFormatting.RED);

        if (!isOP && ConfigHandler.COMMON.magnetRequiresOP.get()) {
            p.sendMessage(msg, p.getUUID());
            return false;
        }

        if (removePlayer)
            CommonProxy.MAGNET_MODE_HANDLER.removePlayer(p);
        else
            CommonProxy.MAGNET_MODE_HANDLER.addPlayer(p);

        return true;
    }
}
