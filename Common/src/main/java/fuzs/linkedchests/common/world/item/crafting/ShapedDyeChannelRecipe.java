package fuzs.linkedchests.common.world.item.crafting;

import com.mojang.serialization.MapCodec;
import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.linkedchests.common.world.level.block.entity.DyeChannel;
import net.minecraft.data.loot.packs.LootData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShapedDyeChannelRecipe extends ShapedRecipe {
    private static final Map<Item, DyeColor> DYE_BY_ITEM = LootData.WOOL_ITEM_BY_DYE.entrySet()
            .stream()
            .collect(Collectors.toMap((Map.Entry<DyeColor, ItemLike> entry) -> entry.getValue().asItem(),
                    Map.Entry::getKey,
                    (o1, o2) -> o2,
                    IdentityHashMap::new));
    public static final MapCodec<ShapedDyeChannelRecipe> MAP_CODEC = ShapedRecipe.MAP_CODEC.xmap(ShapedDyeChannelRecipe::new,
            Function.identity());
    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedDyeChannelRecipe> STREAM_CODEC = ShapedRecipe.STREAM_CODEC.map(
            ShapedDyeChannelRecipe::new,
            Function.identity());
    public static final RecipeSerializer<ShapedDyeChannelRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC,
            STREAM_CODEC);

    public ShapedDyeChannelRecipe(ShapedRecipe recipe) {
        super(recipe.commonInfo, recipe.bookInfo, recipe.pattern, recipe.result);
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput) {
        ItemStack itemStack = super.assemble(craftingInput);
        // if there is a wool block somewhere in here copy the color from that for the dye channel data
        for (ItemStack input : craftingInput.items()) {
            DyeColor dyeColor = DYE_BY_ITEM.get(input.getItem());
            if (dyeColor != null) {
                itemStack.set(ModRegistry.DYE_CHANNEL_DATA_COMPONENT_TYPE.value(), new DyeChannel(dyeColor));
                break;
            }
        }

        return itemStack;
    }

    @Override
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return (RecipeSerializer<ShapedRecipe>) (RecipeSerializer<?>) ModRegistry.SHAPED_DYE_CHANNEL_RECIPE_SERIALIZER.value();
    }
}
