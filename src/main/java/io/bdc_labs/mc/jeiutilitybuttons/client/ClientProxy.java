package io.bdc_labs.mc.jeiutilitybuttons.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.client.handlers.EventHandlers;
import io.bdc_labs.mc.jeiutilitybuttons.core.CommonProxy;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


public class ClientProxy extends CommonProxy {

    public static KeyMapping makeCopyKey = new KeyMapping(Localization.KEY_MAKECOPY, KeyConflictContext.GUI,
            InputConstants.getKey("key.keyboard.c"), Localization.KEY_CATEGORY);
    public static KeyMapping hideAll = new KeyMapping(Localization.KEY_HIDE_OVERLAY, KeyConflictContext.GUI,
            KeyModifier.CONTROL, InputConstants.getKey("key.keyboard.h"), Localization.KEY_CATEGORY);

    public static Minecraft mc;
    public static LocalPlayer player;

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
    public Level getClientWorld() {
        return Minecraft.getInstance().level;
    }
}
