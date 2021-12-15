package io.bdc_labs.mc.jeiutilitybuttons.core;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.MagnetModeHandler;
import io.bdc_labs.mc.jeiutilitybuttons.core.network.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkDirection;

/**
 * Created by universal on 11.08.2016 16:02.
 * This file is part of JustEnoughButtons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/univrsal/JustEnoughButtons
 */
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
    public void serverStarting(FMLServerStartingEvent event) {
        
    }

    @Override
    public World getClientWorld() {
        throw new IllegalStateException("Method is client side only");
    }
}
