package me.flashyreese.mods.reeses_sodium_options;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(ReesesSodiumOptions.MODID)
public class ReesesSodiumOptions {
    public static final String MODID = "textrues_embeddium_options";

    public ReesesSodiumOptions() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> MODID, (a, b) -> true));
    }
}
