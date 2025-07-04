package vapourdrive.enchantingextras.content.enchant_remover;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.base.AbstractBaseContainerBlock;

public class EnchantRemoverBlock extends AbstractBaseContainerBlock {
    public static final MapCodec<EnchantRemoverBlock> CODEC = simpleCodec(EnchantRemoverBlock::new);
    protected static final VoxelShape MID = Block.box(1.0, 4.0, 1.0, 15.0, 12.0, 15.0);
    protected static final VoxelShape BASE = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    protected static final VoxelShape TOP = Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape SHARD1 = Block.box(0.0, 10.0, 0.0, 4.0, 12.0, 4.0);
    protected static final VoxelShape SHARD2 = Block.box(0.0, 10.0, 12.0, 4.0, 12.0, 16.0);
    protected static final VoxelShape SHARD3 = Block.box(12.0, 10.0, 0.0, 16.0, 12.0, 4.0);
    protected static final VoxelShape SHARD4 = Block.box(12.0, 10.0, 12.0, 16.0, 12.0, 16.0);
    protected static final VoxelShape SHAPE = Shapes.or(MID, BASE, TOP, SHARD1, SHARD2, SHARD3, SHARD4);
//    protected static final VoxelShape SHAPE = Shapes.or(MID, BASE, TOP, SHARD1);

    public EnchantRemoverBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return SHAPE;
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected @NotNull MapCodec<? extends EnchantRemoverBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EnchantRemoverBlockEntity(pos, state);
    }


    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        } else {
            return (level1, pos, state1, tile) -> {
                if (tile instanceof EnchantRemoverBlockEntity be) {
                    be.tickServer(state1);
                }
            };
        }
    }

    @Override
    protected ItemStack putAdditionalInfo(ItemStack stack, BlockEntity blockEntity) {
        super.putAdditionalInfo(stack,blockEntity);
        if(blockEntity instanceof EnchantRemoverBlockEntity be) {
            stack.set(Registration.VITAE_DATA, be.getVITAE().getCurrent());
        }
        return stack;
    }

    @Override
    protected void openContainer(Level level, @NotNull BlockPos pos, @NotNull Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EnchantRemoverBlockEntity be) {
            player.openMenu(be, pos);
        }
    }
}
