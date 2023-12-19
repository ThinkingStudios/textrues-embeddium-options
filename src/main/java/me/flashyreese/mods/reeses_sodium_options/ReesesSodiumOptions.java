package me.flashyreese.mods.reeses_sodium_options;

import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.NetworkConstants;

@Mod(ReesesSodiumOptions.MODID)
public class ReesesSodiumOptions {
    public static final String MODID = "textrues_embeddium_options";

    public ReesesSodiumOptions() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
}
