package vapourdrive.enchantingextras.content.enchant_remover;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.vapourware.shared.base.AbstractBaseContainerScreen;
import vapourdrive.vapourware.shared.base.slots.AbstractMachineSlot;
import vapourdrive.vapourware.shared.utils.CompUtils;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

import java.util.ArrayList;
import java.util.List;

public class EnchantRemoverScreen extends AbstractBaseContainerScreen<EnchantRemoverMenu> {
    protected final EnchantRemoverMenu enchantMenu;
//    protected final Button startProcessButton;

    public EnchantRemoverScreen(EnchantRemoverMenu menu, Inventory inv, Component name) {
        super(menu, inv, name, new DeferredComponent(EnchantingExtras.MODID, "enchant_remover"), 158, 6,176,0 ,1, false);
        this.enchantMenu = menu;
//        this.startProcessButton = Button.builder(Component.literal("Process"), b -> this.enchantMenu.startProcess())
//                .size(50, 16).build();
    }

    @Override
    protected void init() {
        super.init();
//        if(ConfigSettings.ENCHANT_REMOVER_START_BUTTON.get()) {
//            this.startProcessButton.setPosition(this.leftPos+54, this.topPos+60);
//            this.startProcessButton.visible = !enchantMenu.isAuto();
//            addRenderableWidget(startProcessButton);
//        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
//        if(ConfigSettings.ENCHANT_REMOVER_START_BUTTON.get()) {
//            this.startProcessButton.visible = !enchantMenu.isAuto();
//        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        int m = (int) (enchantMenu.getVitaePercentage() * (64));
        graphics.blit(this.GUI, this.leftPos + 8, this.topPos + 11 + 64 - m, 188, 64 - m, 14, m);

        int p = (int) (enchantMenu.getProgressPercentage() * (49));
        graphics.blit(this.GUI, this.leftPos + 55, this.topPos + 53, 201, 0, p, 3);
        int cost = enchantMenu.getCurrentEnchantRemovalCost();
        if(cost > 0) {
            Component component = CompUtils.getArgComp(EnchantingExtras.MODID, "max_removal_cost", cost);
            int k = this.leftPos + 56;
            int top = this.topPos + 7;
            graphics.fill(k - 2, top, k+this.font.width(component)+2, top + 12, 1325400064);
            graphics.drawString(this.font, component, k, top + 2, 15192830);
        }

    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack stack = this.hoveredSlot.getItem();
            List<Component> tooltips = this.getTooltipFromContainerItem(stack);
            if(stack.isEnchanted()){
                ItemEnchantments.Mutable mutInEnchants = new ItemEnchantments.Mutable(stack.getTagEnchantments());
                int cost = EnchantRemoverBlockEntity.getMaxCost(mutInEnchants);
                tooltips.add(CompUtils.getArgComp(EnchantingExtras.MODID, "max_removal_cost", cost).withStyle(ChatFormatting.LIGHT_PURPLE));
            }

            graphics.renderTooltip(this.font, tooltips, stack.getTooltipImage(), stack, mouseX, mouseY);
        }

        boolean notCarrying = this.enchantMenu.getCarried().isEmpty();
        List<Component> hoveringText = new ArrayList<>();

        if (notCarrying && isInRect(this.leftPos + this.INFO_XPOS - 1, this.topPos + this.INFO_YPOS - 1, 14, 14, mouseX, mouseY)) {
            this.getAdditionalInfoHover(hoveringText);
        }

//        if(EnchantingExtras.isDebugMode() && this.hoveredSlot != null){
//            hoveringText.add(Component.literal("slot: "+hoveredSlot.index));
//        }

        if (this.hoveredSlot != null && !this.hoveredSlot.hasItem() && this.hoveredSlot instanceof AbstractMachineSlot machineSlot) {
            MutableComponent comp = machineSlot.getComp();
            if (comp != null) {
                hoveringText.add(comp.withStyle(ChatFormatting.GREEN));
            }
        }

        if (notCarrying && isInRect(this.leftPos + 7, this.topPos + 10, 14, 66, mouseX, mouseY)) {
            int i = this.enchantMenu.getCurrentVitae();
            hoveringText.add(Component.translatable("enchantingextras.vitae").append(": ").append(df.format(i) + "/" + df.format(enchantMenu.geMaxVitae())));
        }

        if (!hoveringText.isEmpty()) {
            graphics.renderComponentTooltip(this.font, hoveringText, mouseX, mouseY);
        }
    }

    @Override
    protected void getAdditionalInfoHover(List<Component> hoveringText) {
        hoveringText.add(Component.translatable("enchantingextras.enchant_remover.info"));
    }
}
