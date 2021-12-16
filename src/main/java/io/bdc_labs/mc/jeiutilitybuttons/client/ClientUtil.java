package io.bdc_labs.mc.jeiutilitybuttons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

public class ClientUtil {

    public static Minecraft mc = Minecraft.getInstance();

    public static int getMouseX() {
        return (int) (mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getWidth());
    }

    public static int getMouseY() {
        return (int) (mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getHeight());
    }

    public static int getScreenWidth() {
        if (mc.screen != null)
            return mc.screen.width;
        return -1;
    }

    public static int getScreenHeight() {
        if (mc.screen != null)
            return mc.screen.height;
        return -1;
    }

    public static void playClick() {
        mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
