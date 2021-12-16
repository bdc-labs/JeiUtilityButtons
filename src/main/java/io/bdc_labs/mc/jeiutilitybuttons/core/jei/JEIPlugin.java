package io.bdc_labs.mc.jeiutilitybuttons.core.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

public class JEIPlugin implements IModPlugin {

    private static IJeiRuntime runtime;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return null;
    }

    public static void setJEIText(String text) {
        runtime.getIngredientFilter().setFilterText(text);
    }
}
