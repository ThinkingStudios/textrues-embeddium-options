package me.flashyreese.mods.reeses_sodium_options;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(TROStub.MODID)
public class TROStub {
    public static final String MODID = "reeses_sodium_options";

    public TROStub() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> MODID, (a, b) -> true));
    }
}
