package vapourdrive.enchantingextras.setup.data.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.setup.Registration;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EnchantingExtras.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        itemModelBase(Registration.VITAE_TABLET.get());

    }

    public void itemToolModel(Item item) {
        String name = itemName(item);
        withExistingParent(name, ResourceLocation.withDefaultNamespace("item/handheld")).texture("layer0", "item/"+name);
    }

    public void itemModelBase(Item item) {
        String name = itemName(item);
        withExistingParent(name, ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0", "item/"+name);
    }

    private String itemName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }
}
