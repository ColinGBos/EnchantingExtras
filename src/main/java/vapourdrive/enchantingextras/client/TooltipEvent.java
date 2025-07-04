package vapourdrive.enchantingextras.client;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import vapourdrive.enchantingextras.EnchantingExtras;

import java.util.List;

@EventBusSubscriber(modid = EnchantingExtras.MODID)
public class TooltipEvent {
    @SubscribeEvent
    public static void onToolTipEarly(ItemTooltipEvent event) {
        if (EnchantingExtras.isDebugMode()) {
            List<Component> tips = event.getToolTip();
            ItemStack stack = event.getItemStack();
            for(Holder<Enchantment> enchant : stack.getTagEnchantments().keySet()){
                Enchantment ench = enchant.value();
                tips.add(Component.literal("enchant: "+ ench +", level: "+stack.getEnchantmentLevel(enchant)+", anvil cost: "+ench.getAnvilCost()));
            }
            tips.add(Component.literal("repair cost: "+ stack.getOrDefault(DataComponents.REPAIR_COST, 0)));
        }
    }
}
