package vapourdrive.enchantingextras.content.mob_slayer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.content.slots.VitaeHolderSlot;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.base.AbstractBaseMachineMenu;
import vapourdrive.vapourware.shared.base.slots.SlotFuel;
import vapourdrive.vapourware.shared.base.slots.SlotOutput;
import vapourdrive.vapourware.shared.base.slots.SlotTool;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

import java.util.Objects;

public class MobSlayerMenu extends AbstractBaseMachineMenu {
    // gui position of the player inventory grid
    public static final int PLAYER_INVENTORY_XPOS = 8;
    public static final int PLAYER_INVENTORY_YPOS = 84;

    public static final int OUTPUT_INVENTORY_XPOS = 53;
    public static final int OUTPUT_INVENTORY_YPOS = 23;
    protected final ContainerData containerData;
    protected final MobSlayerBlockEntity mobSlayerBlockEntity;


    public MobSlayerMenu(int windowId, Level world, BlockPos pos, Inventory inv, Player player, ContainerData machineData) {
        super(windowId, world, pos, inv, player, Registration.MOB_SLAYER_MENU.get(), machineData);
        this.containerData = machineData;
        this.mobSlayerBlockEntity = (MobSlayerBlockEntity) world.getBlockEntity(pos);

//        layoutPlayerInventorySlots(PLAYER_INVENTORY_XPOS, PLAYER_INVENTORY_YPOS);

        layoutPlayerInventorySlots(PLAYER_INVENTORY_XPOS, PLAYER_INVENTORY_YPOS);
        if (tileEntity != null && tileEntity instanceof MobSlayerBlockEntity quarryTile) {
            IItemHandler handler = quarryTile.getItemHandler(null);
            addSlot(new SlotFuel(handler, 0, 8, 59));
            addSlot(new SlotTool(handler, 1, 29, 23));
            addSlot(new SlotOutput(handler, 2, OUTPUT_INVENTORY_XPOS, OUTPUT_INVENTORY_YPOS));
            addSlot(new SlotOutput(handler, 3, OUTPUT_INVENTORY_XPOS + 18, OUTPUT_INVENTORY_YPOS));
            addSlot(new SlotOutput(handler, 4, OUTPUT_INVENTORY_XPOS + 18 * 2, OUTPUT_INVENTORY_YPOS));
            addSlot(new SlotOutput(handler, 5, OUTPUT_INVENTORY_XPOS + 18 * 3, OUTPUT_INVENTORY_YPOS));
            addSlot(new SlotOutput(handler, 6, OUTPUT_INVENTORY_XPOS + 18 * 4, OUTPUT_INVENTORY_YPOS));
            addSlot(new SlotOutput(handler, 7, OUTPUT_INVENTORY_XPOS, OUTPUT_INVENTORY_YPOS+18));
            addSlot(new SlotOutput(handler, 8, OUTPUT_INVENTORY_XPOS + 18, OUTPUT_INVENTORY_YPOS+18));
            addSlot(new SlotOutput(handler, 9, OUTPUT_INVENTORY_XPOS + 18 * 2, OUTPUT_INVENTORY_YPOS+18));
            addSlot(new SlotOutput(handler, 10, OUTPUT_INVENTORY_XPOS + 18 * 3, OUTPUT_INVENTORY_YPOS+18));
            addSlot(new SlotOutput(handler, 11, OUTPUT_INVENTORY_XPOS + 18 * 4, OUTPUT_INVENTORY_YPOS+18));
            addSlot(new SlotOutput(handler, 12, OUTPUT_INVENTORY_XPOS, OUTPUT_INVENTORY_YPOS+18*2));
            addSlot(new SlotOutput(handler, 13, OUTPUT_INVENTORY_XPOS + 18, OUTPUT_INVENTORY_YPOS+18*2));
            addSlot(new SlotOutput(handler, 14, OUTPUT_INVENTORY_XPOS + 18 * 2, OUTPUT_INVENTORY_YPOS+18*2));
            addSlot(new SlotOutput(handler, 15, OUTPUT_INVENTORY_XPOS + 18 * 3, OUTPUT_INVENTORY_YPOS+18*2));
            addSlot(new SlotOutput(handler, 16, OUTPUT_INVENTORY_XPOS + 18 * 4, OUTPUT_INVENTORY_YPOS+18*2));
            addSlot(new VitaeHolderSlot(handler, 17, 152, 59, new DeferredComponent(EnchantingExtras.MODID, "vitae_slot_out")));

        }

        //We use this vs the builtin method because we split all the shorts
        addSplitDataSlots(machineData);
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return stillValid(ContainerLevelAccess.create(Objects.requireNonNull(tileEntity.getLevel()), tileEntity.getBlockPos()), playerEntity, Registration.MOB_SLAYER_BLOCK.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        EnchantingExtras.debugLog("index: " + index);
        if(!slot.hasItem()){
            return itemstack;
        }
        ItemStack stack = slot.getItem();
        itemstack = stack.copy();

        //Machine to Inventory
        if (index >= 36 && index <= 53) {
            EnchantingExtras.debugLog("From machine to player");
            if (!this.moveItemStackTo(stack, 27, 36, false)) {
                if (!this.moveItemStackTo(stack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }

        }

        //Player Inventory
        else if (index <= 35) {
            EnchantingExtras.debugLog("From player to machine");
            if (stack.is(Registration.VITAE_TABLET.get())) {
                if (!this.moveItemStackTo(stack, 53, 54, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.getBurnTime(RecipeType.SMELTING) >0.0) {
                if (!this.moveItemStackTo(stack, 36, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.getItem() instanceof TieredItem) {
                if (!this.moveItemStackTo(stack, 37, 38, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCurrentVitae() {
        return this.containerData.get(mobSlayerBlockEntity.getVITAE().getDataIndex());
    }

    @OnlyIn(Dist.CLIENT)
    public float getVitaePercentage() {
//        return enchantRemoverBlockEntity.getVITAE().getPercentFull();
        int i = this.containerData.get(mobSlayerBlockEntity.getVITAE().getDataIndex());
        if (i == 0) {
            return 0;
        }
        return (float) i / (float) mobSlayerBlockEntity.getVITAE().getMax();
    }

    @OnlyIn(Dist.CLIENT)
    public float getProgressPercentage() {
//        return enchantRemoverBlockEntity.getVITAE().getProgressPercentage();
        int i = this.containerData.get(mobSlayerBlockEntity.getVITAE().getDataIndex()+1);
        if (i == 0) {
            return 0;
        }
        return (float) (mobSlayerBlockEntity.getVITAE().getProcessDuration() - i) / (float) mobSlayerBlockEntity.getVITAE().getProcessDuration();
    }

    @OnlyIn(Dist.CLIENT)
    public int geMaxVitae() {
        return mobSlayerBlockEntity.getVITAE().getMax();
    }
}
