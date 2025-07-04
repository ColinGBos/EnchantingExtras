package vapourdrive.enchantingextras.content;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.config.ConfigSettings;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.VapourWare;
import vapourdrive.vapourware.shared.base.BaseInfoItem;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

import java.util.List;

public class VitaeTablet extends BaseInfoItem {
    public VitaeTablet(Properties pProperties) {
        super(pProperties, new DeferredComponent(EnchantingExtras.MODID, "vitae_tablet"));
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        setVitae(pPlayer.getItemInHand(pUsedHand), getMaxVitae()-1000);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        String vitae = VapourWare.decimalFormat.format(getVitae(stack)) + "/" + VapourWare.decimalFormat.format(getMaxVitae());
        tooltipComponents.add(Component.translatable("enchantingextras.vitae").append(": ").append(vitae).withStyle(ChatFormatting.LIGHT_PURPLE));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        return Math.round((float) getVitae(pStack) / (float) getMaxVitae() * 13.0F);
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        float f = (float) getVitae(pStack) / (float) getMaxVitae() * 0.3f;
        return Mth.hsvToRgb(0.75F, 0.4F + f, 1F + f);
    }

    public int getVitae(ItemStack stack) {
        return stack.getOrDefault(Registration.VITAE_DATA, 0);
    }

    private void setVitae(ItemStack stack, int toSet) {
        stack.set(Registration.VITAE_DATA, Math.min(Math.max(0, toSet), getMaxVitae()));
    }

    public int addVitae(ItemStack stack, int toAdd, boolean simulate) {
        int leftOver = Math.max(0, getVitae(stack)+toAdd-getMaxVitae());
        if(!simulate){
            setVitae(stack, getVitae(stack)+toAdd);
        }
        return leftOver;
    }

    public int consumeVitae(ItemStack stack, int toConsume, boolean simulate) {
        int cantFulfill = Math.max(0, toConsume-getVitae(stack));
        if(!simulate){
            setVitae(stack, getVitae(stack)-toConsume);
        }
        return cantFulfill;
    }

    public static int getMaxVitae() {
        return ConfigSettings.VITAE_TABLET_VITAE_STORAGE.get();
    }
}
