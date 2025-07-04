package vapourdrive.enchantingextras.content.slots;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.content.enchant_remover.EnchantRemoverMenu;
import vapourdrive.vapourware.shared.base.slots.BaseSlotIngredient;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

public class EnchantedItemSlot extends BaseSlotIngredient {
    final EnchantRemoverMenu menu;
    public EnchantedItemSlot(EnchantRemoverMenu menu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition, new DeferredComponent(EnchantingExtras.MODID, "enchanted_item_slot"));
        this.menu = menu;
    }

    protected boolean isValidIngredient(ItemStack stack) {
        return stack.isEnchanted();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        menu.updateCost();
    }
}
