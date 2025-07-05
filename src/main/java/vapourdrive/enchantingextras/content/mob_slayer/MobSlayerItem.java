package vapourdrive.enchantingextras.content.mob_slayer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.VapourWare;
import vapourdrive.vapourware.shared.base.BaseMachineItem;
import vapourdrive.vapourware.shared.utils.CompUtils;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

import java.util.List;

public class MobSlayerItem extends BaseMachineItem {
    public MobSlayerItem(Block block, Properties properties) {
        super(block, properties, new DeferredComponent(EnchantingExtras.MODID, "mob_slayer.info"));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        String vitae = VapourWare.decimalFormat.format(getVitae(stack));
        tooltipComponents.add(CompUtils.getArgComp(EnchantingExtras.MODID,"vitae_amount", vitae).withStyle(ChatFormatting.LIGHT_PURPLE));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public int getVitae(ItemStack stack) {
        return stack.getOrDefault(Registration.VITAE_DATA, 0);
    }

    protected void updateAdditional(BlockEntity blockentity, ItemStack stack) {
        if (blockentity instanceof MobSlayerBlockEntity be) {
            be.getVITAE().setCurrent(getVitae(stack));
        }
        blockentity.setChanged();
    }

}
