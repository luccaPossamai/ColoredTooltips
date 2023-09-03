package net.lpcamors.colored_tooltips;

import net.lpcamors.colored_tooltips.config.TooltipElements;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class ColoredTooltipsEvents {


    @SubscribeEvent
    public static void remakeTooltips(ItemTooltipEvent event){
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        if(player == null || !Config.APPLY_CHANGES.get()) return;
        List<Component> newTooltip = event.getToolTip();
        MutableComponent component = Component.literal("");
        component.withStyle(Style.EMPTY);
        Config.TOOLTIP_ELEMENTS_ORDER.get().forEach(tooltipElement -> {
            component.append(TooltipElements.buildNameComponent(tooltipElement, itemStack));
        });
        event.getToolTip().set(0, component);
        for(int i = 1; i < event.getToolTip().size(); i++){
            Component component1 = event.getToolTip().get(i);
            event.getToolTip().set(i, TooltipElements.buildDescriptionComponent(component1));
        }
    }
}
