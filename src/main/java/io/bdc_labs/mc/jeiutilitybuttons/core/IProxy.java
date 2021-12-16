package io.bdc_labs.mc.jeiutilitybuttons.core;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy {
    Level getClientWorld();

    void commonSetup(FMLCommonSetupEvent e);

    void serverStarting(ServerStartingEvent event);
}
