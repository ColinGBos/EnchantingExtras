package vapourdrive.enchantingextras.setup.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vapourdrive.enchantingextras.EnchantingExtras;
import vapourdrive.enchantingextras.setup.Registration;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, EnchantingExtras.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.registerCommonTags();
        this.registerMinecraftTags();
    }

    private void registerMinecraftTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.ENCHANT_REMOVER_BLOCK.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(Registration.ENCHANT_REMOVER_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Registration.MOB_SLAYER_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL).add(Registration.MOB_SLAYER_BLOCK.get());
    }

    private void registerCommonTags() {

    }

}
