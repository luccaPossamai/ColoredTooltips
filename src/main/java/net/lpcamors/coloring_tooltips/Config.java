package net.lpcamors.coloring_tooltips;

import net.lpcamors.coloring_tooltips.config.TooltipElements;
import net.minecraft.ChatFormatting;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collections;
import java.util.List;

public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> APPLY_CHANGES;

    public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> TOOLTIP_ELEMENTS_ORDER;
    public static final ForgeConfigSpec.ConfigValue<String> RARITY_ICON;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> RARITY_ADDITIONAL_FORMAT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NAME_FORMATS;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DESCRIPTION_COLORS_OVERRIDE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> RARITY_COLORS_OVERRIDE;
    
    static {
        BUILDER.push("Client Configuration for ColoringTooltips");
        APPLY_CHANGES = BUILDER.comment("Enables the mod. Here's a color code description: "+ TooltipElements.ColorOverride.COLOR_DESC).define("ApplyChanges", true);
        TOOLTIP_ELEMENTS_ORDER = BUILDER.comment("Tooltip name's element Order. Number 0 represents the name component and number 1 the rarity component.").defineListAllowEmpty(Collections.singletonList("TitleElementsOrder"), () -> List.of(1, 0), o -> o instanceof Integer i && i <= 2);
        RARITY_ICON = BUILDER.comment("Rarity Icon. A character to represent the rarity.").define("RarityIcon"," ♦ ");
        RARITY_ADDITIONAL_FORMAT = BUILDER.comment("Icon Formatting. A general format for the rarity.").defineListAllowEmpty(Collections.singletonList("RarityFormatting"), () -> List.of("§l"), o -> o instanceof String s && ChatFormatting.getByCode(s.charAt(1)) != null);
        NAME_FORMATS = BUILDER.comment("Name Formatting. A format to the title name.").defineListAllowEmpty(Collections.singletonList("NameFormatting"), () -> List.of("§7"), o -> o instanceof String s && ChatFormatting.getByCode(s.charAt(1)) != null);
        DESCRIPTION_COLORS_OVERRIDE = BUILDER.comment("Description Color Overrides. Example: [\"§f §6\"] change §f format for §6.").defineListAllowEmpty(Collections.singletonList("DescriptionColorsOverride"), List::of, TooltipElements.ColorOverride::canCast);
        RARITY_COLORS_OVERRIDE = BUILDER.comment("Rarity Color Overrides. Example: [\"§f §6\"] change §f format for §6").defineListAllowEmpty(Collections.singletonList("RarityColorsOverride"), List::of, TooltipElements.ColorOverride::canCast);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
