package vapourdrive.enchantingextras.content.mob_slayer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.config.ConfigSettings;
import vapourdrive.enchantingextras.content.BlockEntityContentHolder;
import vapourdrive.enchantingextras.content.VitaeIngredientHandler;
import vapourdrive.enchantingextras.content.VitaeTablet;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.base.AbstractBaseFuelUserTile;
import vapourdrive.vapourware.shared.base.itemhandlers.FuelHandler;
import vapourdrive.vapourware.shared.base.itemhandlers.IngredientHandler;
import vapourdrive.vapourware.shared.base.itemhandlers.OutputHandler;
import vapourdrive.vapourware.shared.base.itemhandlers.ToolHandler;
import vapourdrive.vapourware.shared.utils.MachineUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MobSlayerBlockEntity extends AbstractBaseFuelUserTile implements MenuProvider {

    public final int[] TOOL_SLOT = {0};
    public final int[] VITAE_EXTRACT_SLOT = {0};
    private final FuelHandler fuelHandler = new FuelHandler(this, FUEL_SLOT.length);
    private final IngredientHandler toolHandler = new ToolHandler(this, TOOL_SLOT.length);
    private final OutputHandler outputHandler = new OutputHandler(this, OUTPUT_SLOTS.length);
    private final IngredientHandler vitaeExtractHandler = new VitaeIngredientHandler(this, VITAE_EXTRACT_SLOT.length);
    private final CombinedInvWrapper combined = new CombinedInvWrapper(fuelHandler, toolHandler, outputHandler, vitaeExtractHandler);
    public final ContainerData containerData = new SimpleContainerData(3);
    private final BlockEntityContentHolder VITAE = new BlockEntityContentHolder("vitae",1,
            ConfigSettings.MOB_SLAYER_VITAE_STORAGE.get(), ConfigSettings.MOB_SLAYER_PROCESS_TIME.get(), containerData);
    private List<Entity> entities = new ArrayList<>();
    private Player attacker;

    public MobSlayerBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.MOB_SLAYER_BLOCK_ENTITY.get(), pos, state, ConfigSettings.MOB_SLAYER_FUEL_STORAGE.get() * 100, ConfigSettings.MOB_SLAYER_FUEL_TO_WORK.get(), new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14});

    }

    public void tickServer(BlockState state) {
        super.tickServer(state);
        if(this.hasLevel()) {
            if (Objects.requireNonNull(this.getLevel()).getGameTime() % 20 == 0){
                doSetupProcess(state);
            }
            if ((this.getLevel().getGameTime() % ConfigSettings.MOB_SLAYER_PROCESS_TIME.get()) == 0) {
                doWorkProcess(state);
                doCleanProcess(state);
            }

            if (this.getLevel().getGameTime() % 4 == 0) {
                doVitaeExchangeProcess(ConfigSettings.VITAE_TABLET_TRANSFER_RATE.get());
            }
        }
    }

    private void doSetupProcess(BlockState state) {
        if(!canWork(state)) {
            return;
        }
        this.entities = getEntities(state);
        this.attacker = FakePlayerFactory.getMinecraft((ServerLevel) level);
        attacker.setItemInHand(InteractionHand.MAIN_HAND, this.getStackInSlot(MachineUtils.Area.TOOL, 0));
    }

    private void doVitaeExchangeProcess(int amountPerProcess) {
        ItemStack receiveTabletStack = this.getStackInSlot(MachineUtils.Area.AUX_1, 0);
        if(receiveTabletStack.is(Registration.VITAE_TABLET)) {
            VitaeTablet vitaeReceiver = (VitaeTablet) receiveTabletStack.getItem();
            int canTake = Math.min(amountPerProcess, getVITAE().getCurrent());
            int toTake = canTake - vitaeReceiver.addVitae(receiveTabletStack, canTake, false);
            getVITAE().consume(toTake, false);
        }
    }

    public BlockEntityContentHolder getVITAE() {
        return VITAE;
    }

    private void doWorkProcess(BlockState state) {
//        EnchantingExtras.debugLog("working");
        assert this.level != null;
        if (!canWork(state) || entities.isEmpty() || attacker == null) {
            return;
        }
        List<Entity> living = entities.stream().filter(entity -> entity instanceof LivingEntity).toList();
        if(living.isEmpty()){
            return;
        }
        ItemStack tool = this.getStackInSlot(MachineUtils.Area.TOOL, 0);
        LivingEntity victim = (LivingEntity) living.get(level.getRandom().nextInt(living.size()));
        if(this.consumeFuel(this.getMinFuelToWork(), false)){
            if(MobAttackHelper.attack(attacker, victim, tool, level)){
                entities.remove(victim);
            }
        }
    }

    private void doCleanProcess(BlockState state) {
        assert this.level != null;
        if (!canWork(state) || entities.isEmpty()) {
            return;
        }
        List<Entity> items = entities.stream().filter(entity -> entity instanceof ItemEntity).toList();
        for (Entity entity : items) {
            if(entity instanceof ItemEntity item){
                if(MachineUtils.canPushAllOutputs(List.of(item.getItem()), this)) {
                    MachineUtils.pushOutput(item.getItem(), false, this);
                    item.discard();
                    entities.remove(entity);
                }
            }
        }
        List<Entity> orbs = entities.stream().filter(entity -> entity instanceof ExperienceOrb).toList();
        for (Entity entity : orbs) {
            if(entity instanceof ExperienceOrb orb){
                if(getVITAE().add(orb.value*ConfigSettings.VITAE_PER_ENTITY_XP.get(),false)) {
                    orb.discard();
                    entities.remove(entity);
                }
            }
        }
    }

    private List<Entity> getEntities(BlockState state){
        assert level != null;
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        BlockPos pos = this.worldPosition.relative(direction, 3);
        AABB area = new AABB(pos.getX() - 2, pos.getY(), pos.getZ() - 2, pos.getX() + 3, pos.getY() + 2, pos.getZ() + 3);
        return level.getEntities(null, area);
    }

    @Override
    public int getCurrentFuel() {
        return getContainerData().get(0);
    }

    @Override
    public boolean addFuel(int toAdd, boolean simulate) {
        if (toAdd + getCurrentFuel() > getMaxFuel()) {
            return false;
        }
        if (!simulate) {
            getContainerData().set(0, getCurrentFuel() + toAdd);
        }

        return true;
    }

    @Override
    public boolean consumeFuel(int toConsume, boolean simulate) {
        if (getCurrentFuel() < toConsume) {
            return false;
        }
        if (!simulate) {
            getContainerData().set(0, getCurrentFuel() - toConsume);
        }
        return true;
    }

    @Override
    public boolean canWork(BlockState state) {
        boolean canWork = true;
        if (Objects.requireNonNull(this.getLevel()).hasNeighborSignal(this.worldPosition)) {
            canWork = false;
        } else if (getCurrentFuel() < getMinFuelToWork()) {
            canWork = false;
        } else if(outputHandler.isFull()){
            canWork = false;
        }
        changeStateIfNecessary(state, canWork);
//        EnchantingExtras.debugLog("can: "+canWork);
        return canWork;
    }

//    @Override
//    public void changeStateIfNecessary(BlockState state, Boolean working) {
//        assert this.level != null;
//
////        if (state.getValue(BlockStateProperties.LIT) && !working) {
////            this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BlockStateProperties.LIT, false));
////        } else if (!(Boolean)state.getValue(BlockStateProperties.LIT) && working) {
////            this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BlockStateProperties.LIT, true));
////        }
//        this.level.setBlockAndUpdate(this.worldPosition, state.setValue(BlockStateProperties.LIT, working));
//
//    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        outputHandler.deserializeNBT(registries, tag.getCompound("invOut"));
        fuelHandler.deserializeNBT(registries, tag.getCompound("invFuel"));
        toolHandler.deserializeNBT(registries, tag.getCompound("invTool"));
        vitaeExtractHandler.deserializeNBT(registries, tag.getCompound("invVitaeOut"));
        VITAE.loadAdditional(tag);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("invOut", outputHandler.serializeNBT(registries));
        tag.put("invFuel", fuelHandler.serializeNBT(registries));
        tag.put("invTool", toolHandler.serializeNBT(registries));
        tag.put("invVitaeOut", vitaeExtractHandler.serializeNBT(registries));
        VITAE.saveAdditional(tag);
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == Direction.DOWN) {
            return outputHandler;
        }
        return combined;
    }

    @Override
    public ItemStack getStackInSlot(MachineUtils.Area area, int index) {
        return switch (area) {
            case FUEL -> fuelHandler.getStackInSlot(FUEL_SLOT[index]);
            case OUTPUT -> outputHandler.getStackInSlot(OUTPUT_SLOTS[index]);
            case TOOL -> toolHandler.getStackInSlot(TOOL_SLOT[index]);
            case AUX_1 -> vitaeExtractHandler.getStackInSlot(VITAE_EXTRACT_SLOT[index]);
            default -> new ItemStack(Items.DIAMOND);
        };
    }

    @Override
    public void removeFromSlot(MachineUtils.Area area, int index, int amount, boolean simulate) {
        switch (area) {
            case FUEL -> fuelHandler.extractItem(FUEL_SLOT[index], amount, simulate);
            case OUTPUT -> outputHandler.extractItem(OUTPUT_SLOTS[index], amount, simulate);
            case TOOL -> toolHandler.extractItem(TOOL_SLOT[index], amount, simulate);
            case AUX_1 -> vitaeExtractHandler.extractItem(VITAE_EXTRACT_SLOT[index], amount, simulate);
        }
    }

    @Override
    public ItemStack insertToSlot(MachineUtils.Area area, int index, ItemStack stack, boolean simulate) {
        return switch (area) {
            case FUEL -> fuelHandler.insertItem(FUEL_SLOT[index], stack, simulate);
            case OUTPUT -> outputHandler.insertItem(OUTPUT_SLOTS[index], stack, simulate, true);
            case INGREDIENT_1 -> toolHandler.insertItem(TOOL_SLOT[index], stack, simulate);
            case AUX_1 -> vitaeExtractHandler.insertItem(VITAE_EXTRACT_SLOT[index], stack, simulate);
            default -> new ItemStack(Items.DIAMOND);
        };
    }

    @Override
    public ContainerData getContainerData() {
        return this.containerData;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new MobSlayerMenu(id, this.level, this.worldPosition, player.getInventory(), player, this.getContainerData());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(EnchantingExtras.MODID + ".mob_slayer");
    }

}
