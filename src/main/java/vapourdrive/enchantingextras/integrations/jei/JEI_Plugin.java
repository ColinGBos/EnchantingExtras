package vapourdrive.enchantingextras.integrations.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IExtraIngredientRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.config.ConfigSettings;
import vapourdrive.enchantingextras.setup.Registration;
import vapourdrive.vapourware.shared.utils.DeferredComponent;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEI_Plugin implements IModPlugin {


    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(EnchantingExtras.MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
//        registry.addIngredientInfo(new ItemStack(Registration.PRIMITIVE_QUARRY_ITEM.get()), VanillaTypes.ITEM_STACK, Component.translatable("primitivequarry.primitive_quarry.info"));
        DeferredComponent comp = new DeferredComponent(EnchantingExtras.MODID, "enchant_remover.info",ConfigSettings.VITAE_TABLET_VITAE_STORAGE.get());
        registry.addIngredientInfo(Registration.ENCHANT_REMOVER_ITEM.get(), comp.get());

    }

    @Override
    public void registerExtraIngredients(@NotNull IExtraIngredientRegistration registry) {
        List<ItemStack> stacks = new ArrayList<>(List.of());
        ItemStack stack = new ItemStack(Registration.VITAE_TABLET.get());
        stack.set(Registration.VITAE_DATA, ConfigSettings.VITAE_TABLET_VITAE_STORAGE.get());
        stacks.add(stack);
        registry.addExtraItemStacks(stacks);
    }

}
