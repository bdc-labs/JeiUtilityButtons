package io.bdc_labs.mc.jeiutilitybuttons.core.network;

import io.bdc_labs.mc.jeiutilitybuttons.client.Localization;
import io.bdc_labs.mc.jeiutilitybuttons.core.CommonProxy;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageMagnetMode implements IMessage {

    public boolean removePlayer;

    public MessageMagnetMode(boolean remove) {
        removePlayer = remove;
    }

    @Override
    public boolean receive(NetworkEvent.Context context) {
        ServerPlayerEntity p = context.getSender();
        MinecraftServer s = p.getServer();
        if (s == null)
            return false;

        boolean isOP = MessageExecuteButton.checkPermissions(p, s);
        ITextComponent msg = new TranslationTextComponent(Localization.NO_PERMISSIONS).withStyle(TextFormatting.RED);

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
