package vapourdrive.enchantingextras.setup.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.setup.Registration;

import java.util.concurrent.CompletableFuture;

import static vapourdrive.vapourware.shared.utils.RegistryUtils.getIngredientFromTag;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.VITAE_TABLET.get())
                .pattern("CCC").pattern("CAC").pattern("CCC")
                .define('A', Ingredient.of(Items.AMETHYST_BLOCK))
                .define('C', Ingredient.of(Items.CALCITE))
                .unlockedBy("has_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.ENCHANT_REMOVER_ITEM.get())
                .pattern("A A").pattern("CEC").pattern("III")
                .define('A', Ingredient.of(Items.AMETHYST_BLOCK))
                .define('C', Ingredient.of(Items.CRYING_OBSIDIAN))
                .define('E', Ingredient.of(Items.ENCHANTING_TABLE))
                .define('I', getIngredientFromTag("c", "storage_blocks/iron"))
                .unlockedBy("has_amethyst", has(Tags.Items.GEMS_AMETHYST))
                .save(output);

    }
}
