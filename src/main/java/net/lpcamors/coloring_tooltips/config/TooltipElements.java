package net.lpcamors.coloring_tooltips.config;

import net.lpcamors.coloring_tooltips.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class TooltipElements {

    public static final TooltipElement NAME = itemStack -> {
        MutableComponent mutableComponent = ((MutableComponent) itemStack.getHoverName());
        Config.NAME_FORMATS.get().stream().map(s -> s.charAt(1)).map(ChatFormatting::getByCode).filter(Objects::nonNull).forEach(mutableComponent::withStyle);
        return mutableComponent;
    };
    public static final TooltipElement RARITY = itemStack -> {
        MutableComponent mutableComponent = Component.literal(Config.RARITY_ICON.get());
        List<ColorOverride> colorOverrides = Config.RARITY_COLORS_OVERRIDE.get().stream().map(s -> new ColorOverride(s.split(" "))).toList();
        mutableComponent.withStyle(itemStack.getRarity().getStyleModifier());
        colorOverrides.forEach(colorOverride -> {
            List<ChatFormatting> chatFormattings = colorOverride.forceOverride(itemStack.getRarity().color);
            if(!chatFormattings.isEmpty())  chatFormattings.forEach(mutableComponent::withStyle);
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
                    List<ChatFormatting> chatFormattings = colorOverride.forceOverride(chatFormatting);
                    if(!chatFormattings.isEmpty())  chatFormattings.forEach(mutableComponent::withStyle);
                });
            }
        }
        return mutableComponent;
    }
    private interface TooltipElement{
        Component buildComponent(ItemStack itemStack);
    }

    public record ColorOverride(ChatFormatting ct1, List<ChatFormatting> ct2){

        public static final List<String> COLOR_DESC = Arrays.stream(ChatFormatting.values()).map(chatFormatting -> chatFormatting.getName() +": "+ chatFormatting).collect(Collectors.toList());

        public ColorOverride(String[] strings){
            this(ChatFormatting.getByCode(strings[0].charAt(1)), Arrays.stream(strings).skip(1).map(s -> s.charAt(1)).map(ChatFormatting::getByCode).collect(Collectors.toList()));
        }

        public static Boolean canCast(Object o){
            boolean flag = false;
            if(o instanceof String s){
                String[] strings = s.split(" ");
                flag = true;
                for (String string : strings) {
                    if(ChatFormatting.getByCode(string.charAt(1)) == null){
                        flag = false;
                        break;
                    }
                }
            }
            return flag;
        };

        @Nullable
        public static ChatFormatting getChatFormattingByTextColor(@Nonnull TextColor textColor){
            for(ChatFormatting chatFormatting : ChatFormatting.values()){
                if(textColor.equals(TextColor.fromLegacyFormat(chatFormatting))){
                    return chatFormatting;
                }
            }
            return null;
        }
        public List<ChatFormatting> forceOverride(ChatFormatting chatFormatting){
            if(chatFormatting == this.ct1) return new ArrayList<>(ct2);
            return List.of();
        }
    }
}
