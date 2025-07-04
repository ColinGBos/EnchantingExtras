package vapourdrive.enchantingextras.content.enchant_remover;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.VapourWare;
import vapourdrive.vapourware.shared.base.BaseInfoItemBlock;
import vapourdrive.vapourware.shared.utils.CompUtils;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

import javax.annotation.Nullable;
import java.util.List;

public class EnchantRemoverItem extends BaseInfoItemBlock {
    public EnchantRemoverItem(Block block, Item.Properties properties) {
        super(block, properties, new DeferredComponent(EnchantingExtras.MODID, "enchant_remover.info"));
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

    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, Level level, @Nullable Player player, @NotNull ItemStack stack, @NotNull BlockState state) {
        MinecraftServer minecraftserver = level.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity == null || !level.isClientSide && blockentity.onlyOpCanSetNbt() && (player == null || !player.canUseGameMasterBlocks())) {
                return false;
            } else {
                if (blockentity instanceof EnchantRemoverBlockEntity be) {
                    be.getVITAE().setCurrent(getVitae(stack));
                }
                return true;
            }
        }
    }
}
