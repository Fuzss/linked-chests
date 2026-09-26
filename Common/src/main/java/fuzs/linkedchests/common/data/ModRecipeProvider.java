package fuzs.linkedchests.common.data;

import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.linkedchests.common.world.item.crafting.DyeChannelRecipe;
import fuzs.linkedchests.common.world.item.crafting.ShapedDyeChannelRecipe;
import fuzs.puzzleslib.common.api.data.v3.recipes.AbstractRecipeProvider;
import fuzs.puzzleslib.common.api.data.v3.recipes.TransformingRecipeOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    public void buildRecipes() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ModRegistry.LINKED_CHEST_ITEM.value())
                .define('@', Items.ENDER_EYE)
                .define('#', Items.END_STONE)
                .define('C', Items.CHEST)
                .define('W', ItemTags.WOOL)
                .pattern("@W@")
                .pattern("#C#")
                .pattern("@#@")
                .unlockedBy(getHasName(Items.ENDER_EYE), this.has(Items.ENDER_EYE))
                .save(TransformingRecipeOutput.transformed(this.output, (Recipe<?> recipe) -> {
                    return new ShapedDyeChannelRecipe((ShapedRecipe) recipe);
                }));
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.DECORATIONS, ModRegistry.LINKED_POUCH_ITEM.value())
                .define('@', Items.ENDER_EYE)
                .define('#', Items.LEATHER)
                .define('C', Items.CHEST)
                .define('W', ItemTags.WOOL)
                .pattern("@#@")
                .pattern("#C#")
                .pattern("@W@")
                .unlockedBy(getHasName(Items.ENDER_EYE), this.has(Items.ENDER_EYE))
                .save(TransformingRecipeOutput.transformed(this.output, (Recipe<?> recipe) -> {
                    return new ShapedDyeChannelRecipe((ShapedRecipe) recipe);
                }));
        SpecialRecipeBuilder.special(DyeChannelRecipe::new).save(this.output, "dye_channel");
    }
}
