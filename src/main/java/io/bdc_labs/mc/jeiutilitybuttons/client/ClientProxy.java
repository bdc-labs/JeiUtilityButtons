package io.bdc_labs.mc.jeiutilitybuttons.client;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.client.handlers.EventHandlers;
import io.bdc_labs.mc.jeiutilitybuttons.core.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


public class ClientProxy extends CommonProxy {

    public static KeyBinding makeCopyKey = new KeyBinding(Localization.KEY_MAKECOPY, KeyConflictContext.GUI,
            InputMappings.getKey("key.keyboard.c"), Localization.KEY_CATEGORY);
    public static KeyBinding hideAll = new KeyBinding(Localization.KEY_HIDE_OVERLAY, KeyConflictContext.GUI,
            KeyModifier.CONTROL, InputMappings.getKey("key.keyboard.h"), Localization.KEY_CATEGORY);

    public static Minecraft mc;
    public static PlayerEntity player;

    @Override
    public void commonSetup(FMLCommonSetupEvent e) {
        super.commonSetup(e);
        registerKeyBind();
        MinecraftForge.EVENT_BUS.register(new EventHandlers());
        mc = Minecraft.getInstance();
        JeiUtilityButtons.setUpPositions();
        JeiUtilityButtons.logInfo("Client setup complete");
    }

    public void registerKeyBind() {
        ClientRegistry.registerKeyBinding(makeCopyKey);
        ClientRegistry.registerKeyBinding(hideAll);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public World getClientWorld() {
        return Minecraft.getInstance().level;
    }
}
