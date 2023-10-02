package me.flashyreese.mods.reeses_sodium_options.client.gui.frame;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractFrame extends AbstractWidget implements ParentElement {
    protected final Dim2i dim;
    protected final List<AbstractWidget> children = new ArrayList<>();
    protected final List<Drawable> drawable = new ArrayList<>();
    protected final List<ControlElement<?>> controlElements = new ArrayList<>();
    protected boolean renderOutline;
    private Element focused;
    private boolean dragging;
    private Consumer<Element> focusListener;

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
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        if (this.renderOutline) {
            this.drawBorder(drawContext, this.dim.x(), this.dim.y(), this.dim.getLimitX(), this.dim.getLimitY(), 0xFFAAAAAA);
        }
        for (Drawable drawable : this.drawable) {
            drawable.render(drawContext, mouseX, mouseY, delta);
        }
    }

    public void applyScissor(int x, int y, int width, int height, Runnable action) {
        double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        RenderSystem.enableScissor((int) (x * scale), (int) (MinecraftClient.getInstance().getWindow().getFramebufferHeight() - (y + height) * scale),
                (int) (width * scale), (int) (height * scale));
        action.run();
        RenderSystem.disableScissor();
    }

    public void registerFocusListener(Consumer<Element> focusListener) {
        this.focusListener = focusListener;
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
        if (this.focusListener != null) {
            this.focusListener.accept(focused);
        }
    }

    @Override
    public List<? extends Element> children() {
        return this.children;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.dim.containsCursor(mouseX, mouseY);
    }

    @Override
    public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return ParentElement.super.getNavigationPath(navigation);
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return new ScreenRect(this.dim.x(), this.dim.y(), this.dim.width(), this.dim.height());
    }
}
