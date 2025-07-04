package vapourdrive.enchantingextras.content.slots;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.base.slots.BaseSlotIngredient;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

public class VitaeHolderSlot extends BaseSlotIngredient {
    public VitaeHolderSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, DeferredComponent comp) {
        super(itemHandler, index, xPosition, yPosition, comp);
    }

    protected boolean isValidIngredient(ItemStack stack) {
        return stack.is(Registration.VITAE_TABLET);
    }
}
