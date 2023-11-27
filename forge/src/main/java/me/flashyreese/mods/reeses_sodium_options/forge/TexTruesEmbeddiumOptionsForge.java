package me.flashyreese.mods.reeses_sodium_options.forge;

import me.flashyreese.mods.reeses_sodium_options.TexTruesEmbeddiumOptions;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(TexTruesEmbeddiumOptions.MODID)
public class TexTruesEmbeddiumOptionsForge {
    public TexTruesEmbeddiumOptionsForge() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (a, b) -> true));
    }
}
