package io.bdc_labs.mc.jeiutilitybuttons.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.bdc_labs.mc.jeiutilitybuttons.JeiUtilityButtons;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.GuiUtils;

public enum EnumButtonCommands {
    CREATIVE("gamemode creative", 5, 5),
    ADVENTURE("gamemode adventure", 5, 5),
    SURVIVAL("gamemode survival", 5, 5),
    SPECTATE("gamemode spectator", 5, 5),
    DELETE("clear ", 65, 5),
    RAIN("weather rain", 25, 5),
    SUN("weather clear", 45, 5),
    DAY("time set day", 5, 26),
    NIGHT("time set night", 25, 26),
    FREEZETIME("gamerule doDaylightCycle", 25, 47),
    NOMOBS("kill @e[type=!minecraft:player]", 5, 47),
    MAGNET("tp", 25, 47);

    public boolean isEnabled = true;
    public boolean isVisible = true;

    String command;

    JeiUtilityButtons.EnumButtonState state = JeiUtilityButtons.EnumButtonState.DISABLED;

    public static final int width = 18;
    public static final int height = 19;
    public int xPos;
    public int yPos;
    public byte id;

    EnumButtonCommands(String commandToExecute, int x, int y) {
        this.command = commandToExecute;
        this.xPos = x;
        this.yPos = y;
    }

    EnumButtonCommands(String commandToExecute, int x, int y, int id) {
        this.id = (byte) id;
        this.command = commandToExecute;
        this.xPos = x;
        this.yPos = y;
    }

    static final ResourceLocation icons = new ResourceLocation(JeiUtilityButtons.MODID, "textures/icons.png");

    public void setEnabled(boolean b) {
        isEnabled = b;
    }

    public void setPosition(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public EnumButtonCommands cycle() {
        if (ordinal() == 0)
            return ADVENTURE.isEnabled ? ADVENTURE : SURVIVAL;
        else if (ordinal() == 1)
            return SURVIVAL;
        else if (ordinal() == 2)
            return SPECTATE.isEnabled ? SPECTATE : CREATIVE;
        else if (ordinal() == 3)
            return CREATIVE;
        else
            return this; // Other buttons don't cycle
    }

    public void draw() {
        if (!isVisible || getCommand().equals(""))
            return;

        int mouseX = ClientUtil.getMouseX();
        int mouseY = ClientUtil.getMouseY();

        if (isEnabled) {
            if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
                state = JeiUtilityButtons.EnumButtonState.HOVERED;
                JeiUtilityButtons.hoveredButton = this;
                JeiUtilityButtons.isAnyButtonHovered = true;
            } else
                state = JeiUtilityButtons.EnumButtonState.ENABLED;
        } else {
            if (mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height) {
                JeiUtilityButtons.hoveredButton = this;
                JeiUtilityButtons.isAnyButtonHovered = true;
            }
            state = JeiUtilityButtons.EnumButtonState.DISABLED;
        }

        RenderSystem.setShaderTexture(0, icons);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GuiUtils.drawTexturedModalRect(new PoseStack(), xPos, yPos, width * iconID(), height * state.ordinal(), width, height, 1);
    }

    public String getCommand() {
        return command;
    }

    public int iconID() {
        if (this == MAGNET)
            return 12;
        return this.ordinal() > MAGNET.ordinal() ? 11 : this.ordinal();
    }
}
