package io.bdc_labs.mc.jeiutilitybuttons;

import io.bdc_labs.mc.jeiutilitybuttons.client.ClientProxy;
import io.bdc_labs.mc.jeiutilitybuttons.client.EnumButtonCommands;
import io.bdc_labs.mc.jeiutilitybuttons.client.Localization;
import io.bdc_labs.mc.jeiutilitybuttons.core.CommonProxy;
import io.bdc_labs.mc.jeiutilitybuttons.core.IProxy;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(JeiUtilityButtons.MODID)
public class JeiUtilityButtons
{
    public static final String MODID = "jeiutilitybuttons";

    private static final Logger LOGGER = LogManager.getLogger();

    public static EnumButtonCommands btnGameMode = EnumButtonCommands.SURVIVAL;
    public static EnumButtonCommands btnTrash = EnumButtonCommands.DELETE;
    public static EnumButtonCommands btnSun = EnumButtonCommands.SUN;
    public static EnumButtonCommands btnRain = EnumButtonCommands.RAIN;
    public static EnumButtonCommands btnDay = EnumButtonCommands.DAY;
    public static EnumButtonCommands btnNight = EnumButtonCommands.NIGHT;
    public static EnumButtonCommands btnNoMobs = EnumButtonCommands.NOMOBS;
    public static EnumButtonCommands btnFreeze = EnumButtonCommands.FREEZETIME;
    public static EnumButtonCommands btnMagnet = EnumButtonCommands.MAGNET;

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static boolean configHasChanged = false;
    public static boolean isServerSidePresent = false;

    public enum EnumButtonState {
        DISABLED,
        ENABLED,
        HOVERED
    }

    public static EnumButtonCommands hoveredButton;
    public static boolean isAnyButtonHovered;

    public JeiUtilityButtons() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(EventPriority.LOWEST, this::loadConfig);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        proxy.commonSetup(event);
    }

    public void loadConfig(FMLCommonSetupEvent e)
    {
        ConfigHandler.COMMON.load();
    }

    public static void setUpPositions() {
        EnumButtonCommands[] btns = new EnumButtonCommands[]{btnGameMode, btnRain, btnSun, btnFreeze, btnDay,
                btnNight, btnNoMobs, btnMagnet,};

        int x = 0, y = 0;
        for (EnumButtonCommands b : btns) {
            if (!b.isVisible)
                continue;

            b.setPosition((EnumButtonCommands.width + 2) * x + ConfigHandler.COMMON.xOffset.get(),
                    (EnumButtonCommands.height + 2) * y + ConfigHandler.COMMON.yOffset.get());
            x++;

            if (y == 0 && x == 4) {
                y++;
                x = 0;
            } else if (y == 1 && x == 4) {
                y++;
                x = 0;
            } else if (x % 2 == 0 && y > 1) {
                x = 0;
                y++;
            }
        }
        EnumButtonCommands.CREATIVE.setPosition(btnGameMode.xPos, btnGameMode.yPos);
        EnumButtonCommands.SPECTATE.setPosition(btnGameMode.xPos, btnGameMode.yPos);
        EnumButtonCommands.ADVENTURE.setPosition(btnGameMode.xPos, btnGameMode.yPos);
    }

    public static void sendCommand(String cmd) {
        cmd = "/" + cmd;
        if (cmd.length() <= 256 && ClientProxy.mc.player != null)
            ClientProxy.mc.player.sendMessage(new TranslationTextComponent(cmd), ClientProxy.mc.player.getUUID());
        else
            ClientProxy.mc.gui.getChat().addMessage(new TranslationTextComponent(Localization.NBT_TOO_LONG));
    }

    public static void logInfo(String s, Object... format) {
        LOGGER.info("[" + MODID + "] " + s, format);
    }
}
