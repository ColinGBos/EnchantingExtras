package vapourdrive.enchantingextras.content.enchant_remover;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.content.slots.EnchantedItemSlot;
import vapourdrive.enchantingextras.content.slots.VitaeHolderSlot;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.base.AbstractBaseContainerMenu;
import vapourdrive.vapourware.shared.base.slots.SlotOutput;
import vapourdrive.vapourware.shared.base.slots.SpecificItemSlot;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

public class EnchantRemoverMenu extends AbstractBaseContainerMenu {
    public static final int PLAYER_INVENTORY_XPOS = 8;
    public static final int PLAYER_INVENTORY_YPOS = 84;
    protected final ContainerData containerData;
    protected final EnchantRemoverBlockEntity enchantRemoverBlockEntity;
    private int cost = 0;

    public EnchantRemoverMenu(int windowId, Level world, BlockPos pos, Inventory inv, Player player, ContainerData data) {
        super(windowId, world, pos, inv, player, Registration.ENCHANT_REMOVER_MENU.get());
        this.containerData = data;
        this.enchantRemoverBlockEntity = (EnchantRemoverBlockEntity) world.getBlockEntity(pos);
        addSplitDataSlots(containerData);

        layoutPlayerInventorySlots(PLAYER_INVENTORY_XPOS, PLAYER_INVENTORY_YPOS);
        if (tileEntity != null && tileEntity instanceof EnchantRemoverBlockEntity blockEntity) {
            IItemHandler handler = blockEntity.getItemHandler(null);
            addSlot(new VitaeHolderSlot(handler, 0, 26, 11, new DeferredComponent(EnchantingExtras.MODID, "vitae_slot_in")));
            addSlot(new VitaeHolderSlot(handler, 1, 26, 59, new DeferredComponent(EnchantingExtras.MODID, "vitae_slot_out")));
            addSlot(new EnchantedItemSlot(this, handler, 2, 55, 32));
            addSlot(new SpecificItemSlot(handler, 3, 87, 32, Items.BOOK));
            int outX = 116;
            int outY = 23;
            addSlot(new SlotOutput(handler, 4, outX, outY));
            addSlot(new SlotOutput(handler, 5, outX+18, outY));
            addSlot(new SlotOutput(handler, 6, outX+18*2, outY));
            addSlot(new SlotOutput(handler, 7, outX, outY+18));
            addSlot(new SlotOutput(handler, 8, outX+18, outY+18));
            addSlot(new SlotOutput(handler, 9, outX+18*2, outY+18));
            addSlot(new SlotOutput(handler, 10, outX, outY+18*2));
            addSlot(new SlotOutput(handler, 11, outX+18, outY+18*2));
            addSlot(new SlotOutput(handler, 12, outX+18*2, outY+18*2));

        }
    }

//    @Override
//    public void slotsChanged(@NotNull Container container) {
//        super.slotsChanged(container);
//        calculateCost();
//    }

    public void updateCost() {
        ItemStack stack = this.getItems().get(38);
        if(!this.getItems().get(38).isEmpty()){
            EnchantingExtras.debugLog("Updated cost");
            ItemEnchantments.Mutable mutInEnchants = new ItemEnchantments.Mutable(stack.getTagEnchantments());
            this.cost = EnchantRemoverBlockEntity.getMaxCost(mutInEnchants);
        } else if(this.cost > 0) {
            this.cost = 0;

        }
    }

    public int getCurrentEnchantRemovalCost() {
        return this.cost;
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

        //Furnace outputs to Inventory
        if (index >= 36 && index <= 48) {
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
                if (!this.moveItemStackTo(stack, 36, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.isEnchanted()) {
                if (!this.moveItemStackTo(stack, 38, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.is(Items.BOOK)) {
                if (!this.moveItemStackTo(stack, 39, 40, false)) {
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
        return this.containerData.get(enchantRemoverBlockEntity.VITAE.getDataIndex());
    }

    @OnlyIn(Dist.CLIENT)
    public float getVitaePercentage() {
//        return enchantRemoverBlockEntity.getVITAE().getPercentFull();
        int i = this.containerData.get(enchantRemoverBlockEntity.VITAE.getDataIndex());
        if (i == 0) {
            return 0;
        }
        return (float) i / (float) enchantRemoverBlockEntity.VITAE.getMax();
    }

    @OnlyIn(Dist.CLIENT)
    public float getProgressPercentage() {
//        return enchantRemoverBlockEntity.getVITAE().getProgressPercentage();
        int i = this.containerData.get(enchantRemoverBlockEntity.VITAE.getDataIndex()+1);
        if (i == 0) {
            return 0;
        }
        return (float) (enchantRemoverBlockEntity.VITAE.getProcessDuration() - i) / (float) enchantRemoverBlockEntity.VITAE.getProcessDuration();
    }

    @OnlyIn(Dist.CLIENT)
    public int geMaxVitae() {
        return enchantRemoverBlockEntity.VITAE.getMax();
    }

//    public void startProcess() {
//        PacketDistributor.sendToServer(new Network.StartProcessNotification(this.tileEntity.getBlockPos()));
//    }
//
//    public boolean canWork() {
//        return enchantRemoverBlockEntity.canWork();
//    }
//
//    public boolean isAuto() {
//        return enchantRemoverBlockEntity.isAuto();
//    }
}
