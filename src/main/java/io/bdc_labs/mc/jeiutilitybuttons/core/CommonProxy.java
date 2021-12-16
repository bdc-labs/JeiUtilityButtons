package io.bdc_labs.mc.jeiutilitybuttons.core;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.MagnetModeHandler;
import io.bdc_labs.mc.jeiutilitybuttons.core.network.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;

public class CommonProxy implements IProxy {
    public static final MagnetModeHandler MAGNET_MODE_HANDLER = new MagnetModeHandler();
    public static NetworkHandler network;

    @Override
    public void commonSetup(FMLCommonSetupEvent e) {
        network = new NetworkHandler();

        network.register(MessageExecuteButton.class, NetworkDirection.PLAY_TO_SERVER);
        network.register(MessageMagnetMode.class, NetworkDirection.PLAY_TO_SERVER);
        network.register(MessageNotifyClient.class, NetworkDirection.PLAY_TO_CLIENT);
        MinecraftForge.EVENT_BUS.register(MAGNET_MODE_HANDLER);

        JeiUtilityButtons.logInfo("Common setup complete");
    }

    @Override
    public void serverStarting(ServerStartingEvent event) {
        
    }

    @Override
    public Level getClientWorld() {
        throw new IllegalStateException("Method is client side only");
    }
}
