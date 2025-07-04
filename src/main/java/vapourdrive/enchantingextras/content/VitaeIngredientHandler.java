package vapourdrive.enchantingextras.content;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.base.itemhandlers.IngredientHandler;

import javax.annotation.Nonnull;

public class VitaeIngredientHandler extends IngredientHandler {
    public VitaeIngredientHandler(BlockEntity blockEntity, int length) {
        super(blockEntity, length);
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.is(Registration.VITAE_TABLET);
    }
}
