package net.lpcamors.coloring_tooltips;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(ColoringTooltips.MODID)
public class ColoringTooltips {
    public static final String MODID = "coloring_tooltips";

    public ColoringTooltips() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC, "coloring_tooltips-client.toml" );
    }

}
