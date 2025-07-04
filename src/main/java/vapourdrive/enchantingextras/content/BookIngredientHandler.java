package vapourdrive.enchantingextras.content;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import vapourdrive.vapourware.shared.base.itemhandlers.IngredientHandler;

import javax.annotation.Nonnull;

public class BookIngredientHandler extends IngredientHandler {
    public BookIngredientHandler(BlockEntity tile, int size) {
        super(tile, size);
    }

    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.is(Items.BOOK);
    }
}
