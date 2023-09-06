package me.flashyreese.mods.reeses_sodium_options.mixin.sodium;

import me.flashyreese.mods.reeses_sodium_options.client.gui.SliderControlElementExtended;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.Rect2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "me.jellysquid.mods.sodium.client.gui.options.control.SliderControl$Button")
public abstract class MixinSliderControlElement extends ControlElement<Integer> implements SliderControlElementExtended {

    @Shadow
    @Final
    private int interval;

    @Shadow
    private double thumbPosition;

    @Shadow
    @Final
    private int min;

    @Unique
    private int max;

    @Unique
    private boolean editMode;

    @Shadow
    @Final
    private Rect2i sliderBounds;

    @Shadow
    @Final
    private int range;

    public MixinSliderControlElement(Option<Integer> option, Dim2i dim) {
        super(option, dim);
    }

    @Override
    public boolean isEditMode() {
        return this.editMode;
    }

    @Override
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    @Shadow
    public abstract double getThumbPositionForValue(int value);

    @Shadow
    protected abstract void setValueFromMouse(double d);

    @Shadow
    public abstract int getIntValue();

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void postInit(Option<Integer> option, Dim2i dim, int min, int max, int interval, ControlValueFormatter formatter, CallbackInfo ci) {
        this.max = max;
    }

    // Fixme: Reverts keyboard slider control hack but breaks sliders on RSO if removed :>
    // I will need to add keyboard navigation support soon:tm: but it's not on my priority. Help wanted :>
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.option.isAvailable() && button == 0 && this.sliderBounds.contains((int) mouseX, (int) mouseY)) {
            this.setValueFromMouse(mouseX);

            return true;
        } else {
            return false;
        }
    }

    private void setValueFromMouseScroll(double amount) {
        if (this.option.getValue() + this.interval * (int) amount <= this.max && this.option.getValue() + this.interval * (int) amount >= this.min) {
            this.option.setValue(this.option.getValue() + this.interval * (int) amount);
            this.thumbPosition = this.getThumbPositionForValue(this.option.getValue());
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.option.isAvailable() && this.sliderBounds.contains((int) mouseX, (int) mouseY) && Screen.hasShiftDown()) {
            this.setValueFromMouseScroll(amount);

            return true;
        }

        return false;
    }
}