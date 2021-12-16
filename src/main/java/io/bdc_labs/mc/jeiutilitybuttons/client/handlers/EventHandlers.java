package io.bdc_labs.mc.jeiutilitybuttons.client.handlers;

import com.mojang.blaze3d.platform.InputConstants;
import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import io.bdc_labs.mc.jeiutilitybuttons.client.ClientProxy;
import io.bdc_labs.mc.jeiutilitybuttons.client.ClientUtil;
import io.bdc_labs.mc.jeiutilitybuttons.client.EnumButtonCommands;
import io.bdc_labs.mc.jeiutilitybuttons.client.Localization;
import io.bdc_labs.mc.jeiutilitybuttons.core.CommonProxy;
import io.bdc_labs.mc.jeiutilitybuttons.core.handlers.ConfigHandler;
import io.bdc_labs.mc.jeiutilitybuttons.core.network.MessageNotifyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.List;

import static io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons.setUpPositions;

public class EventHandlers {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawScreen(ScreenEvent.DrawScreenEvent e) {
        if (ConfigHandler.COMMON.showButtons.get() && e.getScreen() != null && (e.getScreen() instanceof InventoryScreen || e.getScreen() instanceof CreativeModeInventoryScreen)) {
            int mouseY = e.getMouseY();
            int mouseX = e.getMouseX();


            if (JeiUtilityButtons.isAnyButtonHovered) {
               List<TranslatableComponent> tip = Localization.getTooltip(JeiUtilityButtons.hoveredButton);
                if (tip != null) {
                    e.getScreen().renderComponentTooltip(e.getPoseStack(), tip, mouseX, mouseY, ClientProxy.mc.font);
                }
            }

        }

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseClickPre(ScreenEvent.MouseClickedEvent.Pre e) {
        if (!(e.getScreen() instanceof InventoryScreen || e.getScreen() instanceof CreativeModeInventoryScreen))
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
    public void onDrawBackgroundEventPost(ScreenEvent.BackgroundDrawnEvent e) {
        if (JeiUtilityButtons.configHasChanged) {
            JeiUtilityButtons.configHasChanged = false;
            setUpPositions();
        }

        if (JeiUtilityButtons.isServerSidePresent && e.getScreen() instanceof TitleScreen) {
            JeiUtilityButtons.isServerSidePresent = false;
        } else if (ConfigHandler.COMMON.showButtons.get() && e.getScreen() != null &&
                (e.getScreen() instanceof InventoryScreen || e.getScreen() instanceof CreativeModeInventoryScreen)) {

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
            if (e.getEntity() instanceof LocalPlayer) {
                ClientProxy.player = Minecraft.getInstance().player;
                if (((LocalPlayer) e.getEntity()).isCreative()) {
                    JeiUtilityButtons.btnGameMode = JeiUtilityButtons.btnGameMode.cycle();
                } else {
                    JeiUtilityButtons.btnGameMode = EnumButtonCommands.CREATIVE;
                }
            }
        }
        if (e.getEntity() != null && e.getEntity() instanceof ServerPlayer)
            CommonProxy.network.sendToPlayer(new MessageNotifyClient(), (ServerPlayer) e.getEntity());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        JeiUtilityButtons.isServerSidePresent = false;
    }
    @SubscribeEvent
    public void handleKeyInputEvent(ScreenEvent.KeyboardKeyPressedEvent.Post e) {
        Screen gui = ClientProxy.mc.screen;

        if (gui instanceof ContainerScreen) {
            int keyCode = e.getKeyCode();

            if (ClientProxy.makeCopyKey.isActiveAndMatches(InputConstants.getKey(e.getKeyCode(),e.getScanCode()))) {
                Slot hovered = ((ContainerScreen) gui).getSlotUnderMouse();

                if (hovered != null && ClientProxy.player.getInventory().getSelected().isEmpty() && !hovered.getItem().isEmpty() && hovered.hasItem()) {

                    ItemStack stack = hovered.getItem().copy();
                    stack.setCount(1);
                    CompoundTag t = stack.hasTag() ? stack.getTag() : new CompoundTag();
                    t.putBoolean("JEI_Ghost", true);
                    stack.setTag(t);
                    ClientProxy.player.getInventory().setPickedItem(stack);
                }
            } else if (ClientProxy.hideAll.isActiveAndMatches(InputConstants.getKey(e.getKeyCode(), e.getScanCode()))) {
                ConfigHandler.COMMON.showButtons.set(!ConfigHandler.COMMON.showButtons.get());
                ConfigHandler.COMMON.showButtons.save();
            }
        }

    }

    @SubscribeEvent
    public void onMouseScrollEvent(ScreenEvent.MouseScrollEvent event) {
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
    public void onWorldDraw(RenderLevelLastEvent event) {
        //if (drawMobOverlay)
            //MobOverlayRenderer.renderMobSpawnOverlay();

        if (ClientProxy.mc.screen == null) {
            //ModSubsetButtonHandler.isListShown = false;
            JeiUtilityButtons.isAnyButtonHovered = false;
        }
    }
}
