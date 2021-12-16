package io.bdc_labs.mc.jeiutilitybuttons.core.handlers;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.client.EnumButtonCommands;
import io.bdc_labs.mc.jeiutilitybuttons.client.handlers.CommandHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public final class ConfigHandler {

    public static class DefaultBooleanValidator implements Predicate<Boolean> {
        @Override
        public boolean test(Boolean t) {
            return true;
        }
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue enableAdventureMode;
        public final ForgeConfigSpec.BooleanValue enableSpectatoreMode;
        public final ForgeConfigSpec.IntValue magnetRadius;

        public final ForgeConfigSpec.BooleanValue enableClearInventory;
        public final ForgeConfigSpec.BooleanValue enableSubsets;

        public final ForgeConfigSpec.BooleanValue enableGamemode;
        public final ForgeConfigSpec.BooleanValue enableDelete;
        public final ForgeConfigSpec.BooleanValue enableTime;
        public final ForgeConfigSpec.BooleanValue enableWeather;
        public final ForgeConfigSpec.BooleanValue enableKillMobs;
        public final ForgeConfigSpec.BooleanValue enableTimeFreeze;
        public final ForgeConfigSpec.BooleanValue enableMagnet;

        private static List<? extends Boolean> defaultCommandState = Arrays.asList(false, false, false, false);
        private static List<String> defaultCommands = Arrays.asList("help", "help", "help", "help");
        private static List<String> defaultNames = Arrays.asList("Print help", "Print help", "Print help", "Print help");

        public final ForgeConfigSpec.BooleanValue magnetRequiresOP;
        public final ForgeConfigSpec.BooleanValue saveRequireOP;
        public final ForgeConfigSpec.BooleanValue timeRequiresOP;
        public final ForgeConfigSpec.BooleanValue gamemodeRequiresOP;
        public final ForgeConfigSpec.BooleanValue weatherRequiresOP;
        public final ForgeConfigSpec.BooleanValue killMobsRequiresOP;
        public final ForgeConfigSpec.BooleanValue timeFreezeRequiresOP;
        public final ForgeConfigSpec.BooleanValue deleteRequiresOP;

        public final ForgeConfigSpec.IntValue yOffset;
        public final ForgeConfigSpec.IntValue xOffset;
        public final ForgeConfigSpec.BooleanValue showButtons;

        public Common(ForgeConfigSpec.Builder builder) {
            enableAdventureMode = builder.comment("When false the gamemode button won't allow you to switch to" +
                    "adventure mode").define("enableAdventureMode", true);
            enableSpectatoreMode = builder.comment("When false the gamemode button won't allow you to switch to" +
                    "spectator mode").define("enableSpectatoreMode",  true);
            enableGamemode = builder.comment("When false the gamemode button will be disabled").define(
                    "enableGamemode", true);
            enableDelete = builder.comment("When false the delete button will be disabled").define(
                    "enableDelete", true);
            showButtons = builder.comment("When false no button will be shown").define("showButtons",true);
            enableWeather = builder.comment("When false the weather buttons will be disabled").define("enableWeather", true);
            enableTime = builder.comment("When false the time buttons will be disabled").define("enableTime", true);
            enableKillMobs = builder.comment("When false the kill entities button will be disabled").define("enableKillMobs", true);
            enableTimeFreeze = builder.comment("When false the freeze time button will be disabled").define("enableTimeFreeze",true);
            enableMagnet = builder.comment("When false the magnet mode button will be disabled").define("enableMagnet", true);
            enableSubsets = builder.comment("When true the subsets button will be shown to get quick access to all items" +
                    "from all mods (Requires JEI)").define("enableSubsets", true);
            magnetRadius = builder.comment("The radius in which the magnet mode attracts items").defineInRange("magnetRadius",  12, 1, 32);
            enableClearInventory = builder.comment("When true shift clicking the delete buttonwill clear your inventory").define("enableClearInventory",  false);

            // Permissions
            magnetRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JUB is installed on the server)").define("magnetRequiresOP", true);
            saveRequireOP = builder.comment("When false the magnet mode can be used on servers without op (When JUB is installed on the server)").define("savesRequireOP", true);
            weatherRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JUB is installed on the server)").define("weatherRequiresOP", true);
            killMobsRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JUB is installed on the server)").define("killMobsRequiresOP", true);
            timeFreezeRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JUB is installed on the server)").define("timeFreezeRequiresOP", true);
            deleteRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JUB is installed on the server)").define("deleteRequiresOP", true);
            timeRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JUB is installed on the server)").define("timeRequiresOP", true);
            gamemodeRequiresOP = builder.comment("When false the magnet mode can be used on servers without op (When JUB is installed on the server)").define("gamemodeRequiresOP", true);

            yOffset = builder.comment("Y offset of the buttons").defineInRange("yOffset", 5, 0, 1024);
            xOffset = builder.comment("X offset of the buttons").defineInRange("xOffset", 5, 0, 1024);
        }

        public void load()
        {
            EnumButtonCommands.ADVENTURE.setEnabled(enableAdventureMode.get());
            EnumButtonCommands.SPECTATE.setEnabled(enableSpectatoreMode.get());
            JeiUtilityButtons.btnGameMode.setVisible(enableGamemode.get());
            JeiUtilityButtons.btnDay.setVisible(enableTime.get());
            JeiUtilityButtons.btnNight.setVisible(enableTime.get());
            JeiUtilityButtons.btnTrash.setVisible(enableDelete.get());
            JeiUtilityButtons.btnNoMobs.setVisible(enableKillMobs.get());
            JeiUtilityButtons.btnFreeze.setVisible(enableTimeFreeze.get());
            JeiUtilityButtons.btnRain.setVisible(enableWeather.get());
            JeiUtilityButtons.btnSun.setVisible(enableWeather.get());
            JeiUtilityButtons.btnMagnet.setVisible(enableMagnet.get());
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
