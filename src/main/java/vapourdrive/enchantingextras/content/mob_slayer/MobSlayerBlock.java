package vapourdrive.enchantingextras.content.mob_slayer;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.base.AbstractBaseMachineBlock;

import javax.annotation.Nullable;


public class MobSlayerBlock extends AbstractBaseMachineBlock {

    public static final MapCodec<MobSlayerBlock> CODEC = simpleCodec(MobSlayerBlock::new);

    public MobSlayerBlock() {
        super(Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASEDRUM), 0.2f);
    }

    public MobSlayerBlock(Properties properties) {
        super(properties, 0.2f);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MobSlayerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        } else {
            return (level1, pos, state1, tile) -> {
                if (tile instanceof MobSlayerBlockEntity machine) {
                    machine.tickServer(state1);
                }
            };
        }
    }

    @Override
    protected void openContainer(Level level, @NotNull BlockPos pos, @NotNull Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MobSlayerBlockEntity quarry) {
            player.openMenu((MenuProvider) blockEntity, pos);
        }
    }

    @Override
    protected ItemStack putAdditionalInfo(ItemStack stack, BlockEntity blockEntity) {
        super.putAdditionalInfo(stack,blockEntity);
        if(blockEntity instanceof MobSlayerBlockEntity be) {
            stack.set(Registration.VITAE_DATA, be.getVITAE().getCurrent());
        }
        return stack;
    }

    @Override
    protected @NotNull MapCodec<? extends MobSlayerBlock> codec() {
        return CODEC;
    }
}
