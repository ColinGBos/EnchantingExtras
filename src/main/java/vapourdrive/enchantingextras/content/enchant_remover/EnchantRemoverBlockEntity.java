package vapourdrive.enchantingextras.content.enchant_remover;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.config.ConfigSettings;
import vapourdrive.enchantingextras.content.*;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.base.ITickingContainer;
import vapourdrive.vapourware.shared.base.itemhandlers.IngredientHandler;
import vapourdrive.vapourware.shared.base.itemhandlers.OutputHandler;
import vapourdrive.vapourware.shared.utils.CompUtils;
import vapourdrive.vapourware.shared.utils.MachineUtils;

import java.util.List;
import java.util.Objects;

public class EnchantRemoverBlockEntity extends BlockEntity implements MenuProvider, ITickingContainer {
    public final int[] VITAE_INPUT_SLOT = {0};
    public final int[] VITAE_EXTRACT_SLOT = {0};
    public final int[] INGREDIENT_SLOT = {0};
    public final int[] BOOK_SLOT = {0};
    public final int[] OUTPUT_SLOTS = {0,1,2,3,4,5,6,7,8};
    private final IngredientHandler enchantedItemIngredientHandler = new EnchantedItemIngredientHandler(this, INGREDIENT_SLOT.length);
    private final IngredientHandler bookIngredientHandler = new BookIngredientHandler(this, BOOK_SLOT.length);
    private final IngredientHandler vitaeInputHandler = new VitaeIngredientHandler(this, VITAE_INPUT_SLOT.length);
    private final IngredientHandler vitaeExtractHandler = new VitaeIngredientHandler(this, VITAE_EXTRACT_SLOT.length);
    private final OutputHandler outputHandler = new OutputHandler(this, OUTPUT_SLOTS.length);
    private final CombinedInvWrapper combined = new CombinedInvWrapper(vitaeInputHandler, vitaeExtractHandler, enchantedItemIngredientHandler, bookIngredientHandler, outputHandler);
    private final CombinedInvWrapper combinedOut = new CombinedInvWrapper(vitaeExtractHandler, outputHandler);
    private final ContainerData enchantRemoverData = new SimpleContainerData(2);
    private final BlockEntityContentHolder VITAE = new BlockEntityContentHolder("vitae",0,
            ConfigSettings.ENCHANT_REMOVER_VITAE_STORAGE.get(), ConfigSettings.ENCHANT_REMOVER_PROCESS_TIME.get(), enchantRemoverData);

    public EnchantRemoverBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.ENCHANT_REMOVER_BLOCK_ENTITY.get(), pos, blockState);
//        EnchantingExtras.debugLog("enchant data: "+enchantRemoverData.get(VITAE.getDataIndex()));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return CompUtils.getComp(EnchantingExtras.MODID, "enchant_remover");
    }

    public void tickServer(BlockState state) {
        if(this.hasLevel()) {
            progressProcess();
            if(!Objects.requireNonNull(getLevel()).hasNeighborSignal(this.worldPosition)) {
                if (getVITAE().getTimer() <= 0) {
                    startEnchantRemovalProcess();
                }
            }
            if (this.getLevel().getGameTime() % 4 == 0) {
                doVitaeExchangeProcess(ConfigSettings.VITAE_TABLET_TRANSFER_RATE.get());
            }
        }
    }

    public void startEnchantRemovalProcess() {
        if(getVITAE().getTimer() <= 0 && canWork()) {
            getVITAE().setTimer(getVITAE().getProcessDuration());
        }
    }

    public boolean canWork() {
        ItemStack inToolStack = this.getStackInSlot(MachineUtils.Area.INGREDIENT_1,0);
        ItemStack bookStack = this.getStackInSlot(MachineUtils.Area.INGREDIENT_2,0);
        if(!inToolStack.isEnchanted() || bookStack.getCount()<1){
            return false;
        }
        if(outputHandler.getEmptySlots()<2){
            return false;
        }
        ItemEnchantments.Mutable mutInEnchants = new ItemEnchantments.Mutable(inToolStack.getTagEnchantments());
        int maxCost = getMaxCost(mutInEnchants);
        return getVITAE().consume(maxCost, true);
    }

    private void progressProcess() {
        if(getVITAE().getTimer() > 0){
            if(!canWork()){
                getVITAE().setTimer(0);
            } else {
                getVITAE().tickTimer();
                if (getVITAE().getTimer() == 0) {
                    doEnchantRemovalProcess(Objects.requireNonNull(this.getLevel()).getRandom());
                }
            }
        }
    }

    private void doVitaeExchangeProcess(int amountPerProcess) {
        ItemStack sourceTabletStack = this.getStackInSlot(MachineUtils.Area.AUX_1, 0);
        if(sourceTabletStack.is(Registration.VITAE_TABLET)) {
            VitaeTablet vitaeSource = (VitaeTablet) sourceTabletStack.getItem();
            int canAdd = Math.min(amountPerProcess, getVITAE().getSpace());
            int toAdd = canAdd - vitaeSource.consumeVitae(sourceTabletStack, canAdd, false);
            getVITAE().add(toAdd, false);
        }

        ItemStack receiveTabletStack = this.getStackInSlot(MachineUtils.Area.AUX_2, 0);
        if(receiveTabletStack.is(Registration.VITAE_TABLET)) {
            VitaeTablet vitaeReceiver = (VitaeTablet) receiveTabletStack.getItem();
            int canTake = Math.min(amountPerProcess, getVITAE().getCurrent());
            int toTake = canTake - vitaeReceiver.addVitae(receiveTabletStack, canTake, false);
            getVITAE().consume(toTake, false);
        }
    }

    private void doEnchantRemovalProcess(RandomSource random) {
        if (!canWork()){
            return;
        }
        ItemStack inToolStack = this.getStackInSlot(MachineUtils.Area.INGREDIENT_1,0);
        ItemStack booksStack = this.getStackInSlot(MachineUtils.Area.INGREDIENT_2,0);
        ItemEnchantments.Mutable mutInEnchants = new ItemEnchantments.Mutable(inToolStack.getTagEnchantments());
        int counter = 0;
        int selection = random.nextInt(mutInEnchants.keySet().size());
        for(Holder<Enchantment> enchantmentHolder : mutInEnchants.keySet()){
            if(counter != selection){
                counter++;
                continue;
            }
            ItemEnchantments.Mutable bookEnchants = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            int enchLevel = mutInEnchants.getLevel(enchantmentHolder);
            bookEnchants.set(enchantmentHolder, enchLevel);
            ItemStack outBookStack = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(outBookStack, bookEnchants.toImmutable());
            ItemStack outEnchStack = inToolStack.copy();
            mutInEnchants.removeIf(enchHolder -> enchHolder.value().equals(enchantmentHolder.value()));
            EnchantmentHelper.setEnchantments(outEnchStack, mutInEnchants.toImmutable());
            int repairCost = Math.max(0, (outEnchStack.getOrDefault(DataComponents.REPAIR_COST, 0)-1)/2);
            outEnchStack.set(DataComponents.REPAIR_COST, repairCost);

            if(processOutput(booksStack, inToolStack, outBookStack, outEnchStack, enchantmentHolder, enchLevel)){
                break;
            }
        }
    }

    private boolean processOutput(ItemStack booksStack, ItemStack inToolStack, ItemStack outBookStack,ItemStack outEnchStack, Holder<Enchantment> enchantmentHolder, int enchLevel){
        if(MachineUtils.canPushAllOutputs(List.of(outBookStack, outEnchStack), this)) {
            MachineUtils.pushOutput(outBookStack, false, this);
            MachineUtils.pushOutput(outEnchStack, false, this);
            inToolStack.setCount(inToolStack.getCount() - 1);
            booksStack.setCount(booksStack.getCount() - 1);
            int cost = getRemovalCost(enchantmentHolder.value(), enchLevel);
            EnchantingExtras.debugLog("Removing :"+cost);
            getVITAE().consume(cost, false);
            this.setChanged();
            return true;
        }
        return false;
    }

    public static int getMaxCost(ItemEnchantments.Mutable mutInEnchants) {
        int cost = 0;
        for(Holder<Enchantment> enchantHolder : mutInEnchants.keySet()){
            Enchantment enchant = enchantHolder.value();
            int currentCost = getRemovalCost(enchant, mutInEnchants.getLevel(enchantHolder));
            cost = Math.max(cost, currentCost);
        }
        return cost;
    }

    private static int getRemovalCost(Enchantment enchant, int level) {
//        EnchantingExtras.debugLog("Enchant: "+enchant + ", Cost: "+enchant.getAnvilCost()+", Level: "+level);
        return enchant.getAnvilCost()*level*ConfigSettings.ENCHANT_REMOVER_VITAE_TO_WORK.get();
    }

    public BlockEntityContentHolder getVITAE() {
        return VITAE;
    }

    @Override
    public ItemStack getStackInSlot(MachineUtils.Area area, int index) {
        return switch (area) {
            case OUTPUT -> outputHandler.getStackInSlot(OUTPUT_SLOTS[index]);
            case AUX_1 -> vitaeInputHandler.getStackInSlot(VITAE_INPUT_SLOT[index]);
            case AUX_2 -> vitaeExtractHandler.getStackInSlot(VITAE_EXTRACT_SLOT[index]);
            case INGREDIENT_1 -> enchantedItemIngredientHandler.getStackInSlot(INGREDIENT_SLOT[index]);
            case INGREDIENT_2 -> bookIngredientHandler.getStackInSlot(BOOK_SLOT[index]);
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public void removeFromSlot(MachineUtils.Area area, int index, int amount, boolean simulate) {
        switch (area) {
            case OUTPUT -> outputHandler.extractItem(OUTPUT_SLOTS[index], amount, simulate);
            case AUX_1 -> vitaeInputHandler.extractItem(VITAE_INPUT_SLOT[index], amount, simulate);
            case AUX_2 -> vitaeExtractHandler.extractItem(VITAE_EXTRACT_SLOT[index], amount, simulate);
            case INGREDIENT_1 -> enchantedItemIngredientHandler.extractItem(INGREDIENT_SLOT[index], amount, simulate);
            case INGREDIENT_2 -> bookIngredientHandler.extractItem(BOOK_SLOT[index], amount, simulate);
        }
    }

    @Override
    public ItemStack insertToSlot(MachineUtils.Area area, int index, ItemStack stack, boolean simulate) {
        return switch (area) {
            case OUTPUT -> outputHandler.insertItem(OUTPUT_SLOTS[index], stack, simulate, true);
            case AUX_1 -> vitaeInputHandler.insertItem(VITAE_INPUT_SLOT[index], stack, simulate);
            case AUX_2 -> vitaeExtractHandler.insertItem(VITAE_EXTRACT_SLOT[index], stack, simulate);
            case INGREDIENT_1 -> enchantedItemIngredientHandler.insertItem(INGREDIENT_SLOT[index], stack, simulate);
            case INGREDIENT_2 -> bookIngredientHandler.insertItem(BOOK_SLOT[index], stack, simulate);
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new EnchantRemoverMenu(id, this.level, this.worldPosition, player.getInventory(), player, getContainerData());
    }

    @Override
    public int[] getOutputSlots() {
        return this.OUTPUT_SLOTS;
    }

    @Override
    public ContainerData getContainerData() {
        return getVITAE().getData();
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == Direction.DOWN) {
            return combinedOut;
        }
        return combined;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        EnchantingExtras.debugLog("LOADING TAGS");
        super.loadAdditional(tag, registries);
        outputHandler.deserializeNBT(registries, tag.getCompound("invOut"));
        vitaeInputHandler.deserializeNBT(registries, tag.getCompound("invVitaeIn"));
        vitaeExtractHandler.deserializeNBT(registries, tag.getCompound("invVitaeOut"));
        enchantedItemIngredientHandler.deserializeNBT(registries, tag.getCompound("invEnchantedItem"));
        bookIngredientHandler.deserializeNBT(registries, tag.getCompound("invBooks"));
        getVITAE().loadAdditional(tag);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("invOut", outputHandler.serializeNBT(registries));
        tag.put("invVitaeIn", vitaeInputHandler.serializeNBT(registries));
        tag.put("invVitaeOut", vitaeExtractHandler.serializeNBT(registries));
        tag.put("invEnchantedItem", enchantedItemIngredientHandler.serializeNBT(registries));
        tag.put("invBooks", bookIngredientHandler.serializeNBT(registries));
        getVITAE().saveAdditional(tag);
    }
}
