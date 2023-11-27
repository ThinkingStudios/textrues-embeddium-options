package me.flashyreese.mods.reeses_sodium_options.neoforge;

import me.flashyreese.mods.reeses_sodium_options.TexTruesEmbeddiumOptions;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.NetworkConstants;

@Mod(TexTruesEmbeddiumOptions.MODID)
public class TexTruesEmbeddiumOptionsNeoForge {
    public TexTruesEmbeddiumOptionsNeoForge() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
}
