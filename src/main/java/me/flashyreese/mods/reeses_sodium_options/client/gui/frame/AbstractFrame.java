package me.flashyreese.mods.reeses_sodium_options.client.gui.frame;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFrame extends AbstractWidget implements ParentElement {
    protected final Dim2i dim;
    protected final List<AbstractWidget> children = new ArrayList<>();
    protected final List<Drawable> drawable = new ArrayList<>();
    protected final List<ControlElement<?>> controlElements = new ArrayList<>();
    protected boolean renderOutline;
    private Element focused;
    private boolean dragging;

    public AbstractFrame(Dim2i dim, boolean renderOutline) {
        this.dim = dim;
        this.renderOutline = renderOutline;
    }

    public void buildFrame() {
        for (Element element : this.children) {
            if (element instanceof AbstractFrame) {
                this.controlElements.addAll(((AbstractFrame) element).controlElements);
            }
            if (element instanceof ControlElement<?>) {
                this.controlElements.add((ControlElement<?>) element);
            }
            if (element instanceof Drawable) {
                this.drawable.add((Drawable) element);
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        if (this.renderOutline) {
            this.drawRectOutline(this.dim.x(), this.dim.y(), this.dim.getLimitX(), this.dim.getLimitY(), 0xFFAAAAAA);
        }
        for (Drawable drawable : this.drawable) {
            drawable.render(matrixStack, mouseX, mouseY, delta);
        }
    }

    public void applyScissor(int x, int y, int width, int height, Runnable action) {
        double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        RenderSystem.enableScissor((int) (x * scale), (int) (MinecraftClient.getInstance().getWindow().getFramebufferHeight() - (y + height) * scale),
                (int) (width * scale), (int) (height * scale));
        action.run();
        RenderSystem.disableScissor();
    }

    protected void drawRectOutline(double x, double y, double w, double h, int color) {
        final float a = (float) (color >> 24 & 255) / 255.0F;
        final float r = (float) (color >> 16 & 255) / 255.0F;
        final float g = (float) (color >> 8 & 255) / 255.0F;
        final float b = (float) (color & 255) / 255.0F;

        this.drawQuads(vertices -> {
            addQuad(vertices, x, y, w, y + 1, a, r, g, b);
            addQuad(vertices, x, h - 1, w, h, a, r, g, b);
            addQuad(vertices, x, y, x + 1, h, a, r, g, b);
            addQuad(vertices, w - 1, y, w, h, a, r, g, b);
        });
    }

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Nullable
    @Override
    public Element getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        this.focused = focused;
    }

    @Override
    public List<? extends Element> children() {
        return this.children;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.dim.containsCursor(mouseX, mouseY);
    }
}
