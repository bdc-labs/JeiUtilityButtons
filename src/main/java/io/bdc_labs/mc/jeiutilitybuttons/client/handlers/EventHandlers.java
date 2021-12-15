package io.bdc_labs.mc.jeiutilitybuttons.client.handlers;

import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.client.ClientProxy;
import io.bdc_labs.mc.jeiutilitybuttons.client.ClientUtil;
import io.bdc_labs.mc.jeiutilitybuttons.client.EnumButtonCommands;
import io.bdc_labs.mc.jeiutilitybuttons.client.Localization;
import io.bdc_labs.mc.jeiutilitybuttons.core.CommonProxy;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import io.bdc_labs.mc.jeiutilitybuttons.core.network.MessageNotifyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.List;

import static io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons.setUpPositions;

public class EventHandlers {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent e) {
        if (ConfigHandler.COMMON.showButtons.get() && e.getGui() != null && e.getGui() instanceof ContainerScreen) {
            int mouseY = e.getMouseY();
            int mouseX = e.getMouseX();


            if (JeiUtilityButtons.isAnyButtonHovered) {
               List<TranslationTextComponent> tip = Localization.getTooltip(JeiUtilityButtons.hoveredButton);
                if (tip != null) {
                    GuiUtils.drawHoveringText(e.getMatrixStack(), tip, mouseX, Math.max(mouseY, 17), ClientUtil.getScreenWidth(),
                            ClientUtil.getScreenHeight(), -1, ClientProxy.mc.font);
                    //RenderHelper.disableStandardItemLighting();
                }
            }

        }

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseClickPre(GuiScreenEvent.MouseClickedEvent.Pre e) {
        if (!(e.getGui() instanceof ContainerScreen))
            return;

        //int mouseY = ClientUtil.getMouseY();
        //int mouseX = ClientUtil.getMouseX();

        if (e.getButton() == 0) {
            if (JeiUtilityButtons.isAnyButtonHovered && JeiUtilityButtons.hoveredButton.isEnabled) {
                CommandHelper.handleClick(JeiUtilityButtons.hoveredButton);
                ClientUtil.playClick();
            } else { // Save buttons & Mod subsets
                //ModSubsetButtonHandler.click(mouseX, mouseY, e.getButton());
            }
        }

        if (JeiUtilityButtons.isAnyButtonHovered) {
            e.setCanceled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawBackgroundEventPost(GuiScreenEvent.BackgroundDrawnEvent e) {
        if (JeiUtilityButtons.configHasChanged) {
            JeiUtilityButtons.configHasChanged = false;
            setUpPositions();
        }

        if (JeiUtilityButtons.isServerSidePresent && e.getGui() instanceof MainMenuScreen) {
            JeiUtilityButtons.isServerSidePresent = false;
        } else if (ConfigHandler.COMMON.showButtons.get() && e.getGui() != null && e.getGui() instanceof ContainerScreen) {
            int mouseY = ClientUtil.getMouseY();
            int mouseX = ClientUtil.getMouseX();
            ContainerScreen g = (ContainerScreen) e.getGui();
            PlayerEntity pl = ClientProxy.player;

            if (JeiUtilityButtons.btnGameMode == EnumButtonCommands.SPECTATE && !ConfigHandler.COMMON.enableSpectatoreMode.get() ||
                    JeiUtilityButtons.btnGameMode == EnumButtonCommands.ADVENTURE && !ConfigHandler.COMMON.enableAdventureMode.get()) {
                JeiUtilityButtons.btnGameMode = JeiUtilityButtons.btnGameMode.cycle();
            }

            JeiUtilityButtons.isAnyButtonHovered = false;
            {
                JeiUtilityButtons.btnGameMode.draw();
                JeiUtilityButtons. btnTrash.draw();
                JeiUtilityButtons.btnSun.draw();
                JeiUtilityButtons.btnRain.draw();
                JeiUtilityButtons.btnDay.draw();
                JeiUtilityButtons.btnNight.draw();
                JeiUtilityButtons.btnNoMobs.draw();
                JeiUtilityButtons.btnFreeze.draw();
                JeiUtilityButtons.btnMagnet.draw();
            }

            adjustGamemode();
        }

    }

    private void adjustGamemode() {
        GameType t = ClientProxy.mc.gameMode.getPlayerMode();
        boolean doSwitch = false;

        if (t == GameType.CREATIVE && JeiUtilityButtons.btnGameMode == EnumButtonCommands.CREATIVE)
            doSwitch = true;
        else if (t == GameType.SURVIVAL && JeiUtilityButtons.btnGameMode == EnumButtonCommands.SURVIVAL)
            doSwitch = true;
        else if (t == GameType.ADVENTURE && JeiUtilityButtons.btnGameMode == EnumButtonCommands.ADVENTURE)
            doSwitch = true;

        else if (t == GameType.SPECTATOR && JeiUtilityButtons.btnGameMode == EnumButtonCommands.SPECTATE)
            doSwitch = true;

        if (doSwitch)
            JeiUtilityButtons.btnGameMode = JeiUtilityButtons.btnGameMode.cycle();
    }

    @SubscribeEvent
    public void onWorldJoin(EntityJoinWorldEvent e) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            if (e.getEntity() instanceof PlayerEntity) {
                ClientProxy.player = Minecraft.getInstance().player;
                if (((PlayerEntity) e.getEntity()).isCreative()) {
                    JeiUtilityButtons.btnGameMode = JeiUtilityButtons.btnGameMode.cycle();
                } else {
                    JeiUtilityButtons.btnGameMode = EnumButtonCommands.CREATIVE;
                }
            }
        }
        if (e.getEntity() != null && e.getEntity() instanceof ServerPlayerEntity)
            CommonProxy.network.sendToPlayer(new MessageNotifyClient(), (ServerPlayerEntity) e.getEntity());
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        JeiUtilityButtons.isServerSidePresent = false;
    }
    @SubscribeEvent
    public void handleKeyInputEvent(GuiScreenEvent.KeyboardKeyPressedEvent.Post e) {
        Screen gui = ClientProxy.mc.screen;

        if (gui instanceof ContainerScreen) {
            int keyCode = e.getKeyCode();

            if (ClientProxy.makeCopyKey.isActiveAndMatches(InputMappings.getKey(e.getKeyCode(),e.getScanCode()))) {
                Slot hovered = ((ContainerScreen) gui).getSlotUnderMouse();

                if (hovered != null && ClientProxy.player.inventory.getSelected().isEmpty() && !hovered.getItem().isEmpty() && hovered.hasItem()) {

                    ItemStack stack = hovered.getItem().copy();
                    stack.setCount(1);
                    CompoundNBT t = stack.hasTag() ? stack.getTag() : new CompoundNBT();
                    t.putBoolean("JEI_Ghost", true);
                    stack.setTag(t);
                    ClientProxy.player.inventory.setPickedItem(stack);
                }
            } else if (ClientProxy.hideAll.isActiveAndMatches(InputMappings.getKey(e.getKeyCode(), e.getScanCode()))) {
                ConfigHandler.COMMON.showButtons.set(!ConfigHandler.COMMON.showButtons.get());
                ConfigHandler.COMMON.showButtons.save();
            }
        }

    }

    @SubscribeEvent
    public void onMouseScrollEvent(GuiScreenEvent.MouseScrollEvent event) {
        /*
        if (event.getScrollDelta() != 0 && ModSubsetButtonHandler.isListShown) {
            ModSubsetButtonHandler.scroll(event.getScrollDelta());
        }
        */
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

    }

    @SubscribeEvent
    public void onWorldDraw(RenderWorldLastEvent event) {
        //if (drawMobOverlay)
            //MobOverlayRenderer.renderMobSpawnOverlay();

        if (ClientProxy.mc.screen == null) {
            //ModSubsetButtonHandler.isListShown = false;
            JeiUtilityButtons.isAnyButtonHovered = false;
        }
    }
}
