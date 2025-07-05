package vapourdrive.enchantingextras.content.mob_slayer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.vapourware.shared.base.AbstractBaseMachineScreen;
import vapourdrive.vapourware.shared.utils.CompUtils;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

import java.util.ArrayList;
import java.util.List;

public class MobSlayerScreen extends AbstractBaseMachineScreen<MobSlayerMenu> {
    protected final MobSlayerMenu mobSlayerMenu;

    public MobSlayerScreen(MobSlayerMenu container, Inventory inv, Component name) {
        super(container, inv, name, new DeferredComponent(EnchantingExtras.MODID, "mob_slayer"), 12, 8, 46, 138, 6, 1, true);
        this.mobSlayerMenu = container;
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        if(EnchantingExtras.isDebugMode()) {
            graphics.drawString(this.font, "Progress: " + mobSlayerMenu.getProgressPercentage(), -70, this.topPos, 16777215);
            graphics.drawString(this.font, "Fuel: " + mobSlayerMenu.getFuelPercentage(), -70, this.topPos + 20, 16777215);
            graphics.drawString(this.font, "Vitae: " + mobSlayerMenu.getVitaePercentage(), -70, this.topPos + 40, 16777215);
        }
    }

    @Override
    protected void getAdditionalInfoHover(List<Component> hoveringText) {
        super.getAdditionalInfoHover(hoveringText);
        hoveringText.add(CompUtils.getComp(comp.getMod(), comp.getTail() + ".info"));
        hoveringText.add(CompUtils.getComp(comp.getMod(), comp.getTail() + ".player").withStyle(ChatFormatting.GOLD));
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics,partialTicks,mouseX,mouseY);
        int m = (int) (mobSlayerMenu.getVitaePercentage() * (46));
        graphics.blit(this.GUI, this.leftPos + 154, this.topPos + 8 + 46 - m, 196, 46 - m, 12, m);

        int p = (int) (mobSlayerMenu.getProgressPercentage() * (49));
        graphics.blit(this.GUI, this.leftPos + 55, this.topPos + 53, 201, 0, p, 3);

    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        boolean notCarrying = this.menu.getCarried().isEmpty();
        List<Component> hoveringText = new ArrayList<>();

        if(EnchantingExtras.isDebugMode() && this.hoveredSlot != null){
            hoveringText.add(Component.literal("slot: "+hoveredSlot.index));
        }

        if (notCarrying && isInRect(this.leftPos + 153, this.topPos + 7, 13, 48, mouseX, mouseY)) {
            int i = this.mobSlayerMenu.getCurrentVitae();
            hoveringText.add(Component.translatable("enchantingextras.vitae").append(": ").append(df.format(i) + "/" + df.format(mobSlayerMenu.geMaxVitae())));
        }

        if (!hoveringText.isEmpty()) {
            graphics.renderComponentTooltip(this.font, hoveringText, mouseX, mouseY);
        }
    }

}
