package me.flashyreese.mods.reeses_sodium_options.client.gui.frame;

import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFrame extends AbstractWidget implements ParentElement {
    protected final Dim2i dim;

    protected boolean renderOutline = false;

    private Element focused;
    private boolean dragging;

    protected final List<AbstractWidget> children = new ArrayList<>();
    protected final List<Drawable> drawable = new ArrayList<>();
    protected final List<ControlElement<?>> controlElements = new ArrayList<>();

    public AbstractFrame(Dim2i dim) {
        this.dim = dim;
    }

    public AbstractFrame(Dim2i dim, List<AbstractWidget> children) {
        this(dim);
        this.children.addAll(children);
    }

    public AbstractFrame(Dim2i dim, boolean renderOutline) {
        this(dim);
        this.renderOutline = renderOutline;
    }

    public AbstractFrame(Dim2i dim, List<AbstractWidget> children, boolean renderOutline) {
        this(dim);
        this.children.addAll(children);
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.renderOutline) {
            this.drawRectOutline(this.dim.getOriginX(), this.dim.getOriginY(), this.dim.getLimitX(), this.dim.getLimitY(), 0xFFAAAAAA);
        }
        for (Drawable drawable : this.drawable) {
            drawable.render(matrices, mouseX, mouseY, delta);
        }
    }

    public void applyScissor(int x, int y, int width, int height, Runnable action) {
        int scale = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
        GL11.glScissor(x * scale, MinecraftClient.getInstance().getWindow().getHeight() - (y + height) * scale,
                width * scale, height * scale);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        action.run();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    //Fixme: Duplicated, fix in 1.17 when Sodium release.
    protected void drawRectangle(double x1, double y1, double x2, double y2, int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        this.drawQuads(vertices -> this.addQuad(vertices, x1, y1, x2, y2, a, r, g, b));
    }

    protected void drawRectOutline(double x, double y, double w, double h, int color) {
        final float a = (float) (color >> 24 & 255) / 255.0F;
        final float r = (float) (color >> 16 & 255) / 255.0F;
        final float g = (float) (color >> 8 & 255) / 255.0F;
        final float b = (float) (color & 255) / 255.0F;

        this.drawQuads(vertices -> {
            this.addQuad(vertices, x, y, w, y + 1, a, r, g, b);
            this.addQuad(vertices, x, h - 1, w, h, a, r, g, b);
            this.addQuad(vertices, x, y, x + 1, h, a, r, g, b);
            this.addQuad(vertices, w - 1, y, w, h, a, r, g, b);
        });
    }

    protected void addQuad(VertexConsumer consumer, double x1, double y1, double x2, double y2, float a, float r, float g, float b) {
        consumer.vertex(x2, y1, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x1, y1, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x1, y2, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x2, y2, 0.0D).color(r, g, b, a).next();
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