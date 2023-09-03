package net.lpcamors.colored_tooltips.config;

import com.google.common.collect.ImmutableMap;
import net.lpcamors.colored_tooltips.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TooltipElements {

    public static final TooltipElement NAME = itemStack -> {
        MutableComponent mutableComponent = ((MutableComponent) itemStack.getHoverName());
        Config.NAME_FORMATS.get().stream().map(s -> {
            return s.charAt(1);
        }).map(character -> {
            return ChatFormatting.getByCode(character);
        }).filter(Objects::nonNull).forEach(chatFormatting -> {
            mutableComponent.withStyle(chatFormatting);
        });
        return mutableComponent;
    };
    public static final TooltipElement RARITY = itemStack -> {
        MutableComponent mutableComponent = Component.literal(Config.RARITY_ICON.get());
        List<ColorOverride> colorOverrides = Config.RARITY_COLORS_OVERRIDE.get().stream().map(s -> new ColorOverride(s.split(" "))).toList();
        mutableComponent.withStyle(itemStack.getRarity().getStyleModifier());
        colorOverrides.forEach(colorOverride -> {
            ChatFormatting chatFormatting = colorOverride.forceOverride(itemStack.getRarity().color);
            if(chatFormatting != null) mutableComponent.withStyle(chatFormatting);
        });
        Config.RARITY_ADDITIONAL_FORMAT.get().stream().map(s -> s.charAt(1)).map(ChatFormatting::getByCode).filter(Objects::nonNull).forEach(mutableComponent::withStyle);
        return mutableComponent;
    };

    public static final TooltipElement[] TOOLTIP_ELEMENTS = {NAME, RARITY};

    public static Component buildNameComponent(int i, ItemStack itemStack){
        return TOOLTIP_ELEMENTS[i].buildComponent(itemStack);
    }

    public static MutableComponent buildDescriptionComponent(Component component){
        MutableComponent mutableComponent = Component.literal(component.getString());
        List<ColorOverride> colorOverrides = Config.DESCRIPTION_COLORS_OVERRIDE.get().stream().map(s -> new ColorOverride(s.split(" "))).toList();
        if(component.getStyle().getColor() != null) {
            ChatFormatting chatFormatting = ColorOverride.getChatFormattingByTextColor(component.getStyle().getColor());
            if(chatFormatting != null) {
                mutableComponent.withStyle(chatFormatting);
                colorOverrides.forEach(colorOverride -> {
                    ChatFormatting chatFormatting1 = colorOverride.forceOverride(chatFormatting);
                    if(chatFormatting1 != null) mutableComponent.withStyle(chatFormatting1);
                });
            }
        }
        return mutableComponent;
    }
    private interface TooltipElement{
        Component buildComponent(ItemStack itemStack);
    }

    public record ColorOverride(ChatFormatting ct1, ChatFormatting ct2){

        public static final List<String> COLOR_DESC = Arrays.stream(ChatFormatting.values()).map(chatFormatting -> chatFormatting.getName() +": "+ chatFormatting).collect(Collectors.toList());

        public ColorOverride(String[] strings){
            this(ChatFormatting.getByCode(strings[0].charAt(1)), ChatFormatting.getByCode(strings[1].charAt(1)));
        }

        @Nullable
        public static ChatFormatting getChatFormattingByTextColor(@Nonnull TextColor textColor){
            for(ChatFormatting chatFormatting : ChatFormatting.values()){
                if(textColor.equals(TextColor.fromLegacyFormat(chatFormatting))){
                    return chatFormatting;
                }
            }
            return null;
        }
        @Nullable
        public ChatFormatting forceOverride(ChatFormatting chatFormatting){
            return chatFormatting == this.ct1 ? ct2 : null;
        }

        @Nullable
        public ChatFormatting forceOverride(@Nullable TextColor textColor){
            if(textColor != null && textColor.equals(TextColor.fromLegacyFormat(this.ct1))){
                return this.ct2;
            }
            return null;
        }
    }
}
