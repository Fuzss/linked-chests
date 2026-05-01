package fuzs.linkedchests.common.data.client;

import fuzs.linkedchests.common.client.color.item.DyeChannelTintSource;
import fuzs.linkedchests.common.client.renderer.blockentity.LinkedChestBlockEntityRenderer;
import fuzs.linkedchests.common.client.renderer.item.properties.conditional.LinkedPouchOpenModelProperty;
import fuzs.linkedchests.common.client.renderer.item.properties.conditional.LinkedPouchPersonalModelProperty;
import fuzs.linkedchests.common.client.renderer.special.LinkedChestSpecialRenderer;
import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.common.api.client.data.v2.models.ItemModelGenerationHelper;
import fuzs.puzzleslib.common.api.client.data.v2.models.ModelLocationHelper;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

public class ModModelProvider extends AbstractModelProvider {
    public static final TextureSlot LAYER3_TEXTURE_SLOT = TextureSlot.create("layer3");
    public static final ModelTemplate FOUR_LAYERED_ITEM = ModelTemplates.createItem("generated",
            TextureSlot.LAYER0,
            TextureSlot.LAYER1,
            TextureSlot.LAYER2,
            LAYER3_TEXTURE_SLOT);

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        ItemModelGenerationHelper.generateChest(ModRegistry.LINKED_CHEST_BLOCK.value(),
                Blocks.END_STONE,
                LinkedChestBlockEntityRenderer.LINKED_CHEST_TEXTURE,
                false,
                (Identifier identifier) -> {
                    return new LinkedChestSpecialRenderer.Unbaked(identifier,
                            LinkedChestBlockEntityRenderer.LINKED_CHEST_BUTTONS_TEXTURE);
                },
                blockModelGenerators);
    }

    @Override
    public void addItemModels(ItemModelGenerators itemModelGenerators) {
        this.generateLinkedPouch(ModRegistry.LINKED_POUCH_ITEM.value(), itemModelGenerators);
    }

    public final void generateLinkedPouch(Item item, ItemModelGenerators itemModelGenerators) {
        ItemModel.Unbaked itemModel = this.createLinkedPouch(ModelLocationHelper.getItemModel(item),
                ModelLocationHelper.getItemTexture(item),
                ModelLocationHelper.getItemTexture(item),
                itemModelGenerators);
        ItemModel.Unbaked openModel = this.createLinkedPouch(ModelLocationHelper.getItemModel(item, "_open"),
                ModelLocationHelper.getItemTexture(item, "_open"),
                ModelLocationHelper.getItemTexture(item, "_open"),
                itemModelGenerators);
        ItemModel.Unbaked personalModel = this.createLinkedPouch(ModelLocationHelper.getItemModel(item, "_personal"),
                ModelLocationHelper.getItemTexture(item, "_personal"),
                ModelLocationHelper.getItemTexture(item),
                itemModelGenerators);
        ItemModel.Unbaked openPersonalModel = this.createLinkedPouch(ModelLocationHelper.getItemModel(item,
                        "_open_personal"),
                ModelLocationHelper.getItemTexture(item, "_open_personal"),
                ModelLocationHelper.getItemTexture(item, "_open"),
                itemModelGenerators);
        ItemModel.Unbaked falseModel = ItemModelUtils.conditional(new LinkedPouchOpenModelProperty(),
                openModel,
                itemModel);
        ItemModel.Unbaked trueModel = ItemModelUtils.conditional(new LinkedPouchOpenModelProperty(),
                openPersonalModel,
                personalModel);
        itemModelGenerators.generateBooleanDispatch(item,
                new LinkedPouchPersonalModelProperty(),
                trueModel,
                falseModel);
    }

    public final ItemModel.Unbaked createLinkedPouch(Identifier itemModel, Material baseLocation, Material dyeSlotLocation, ItemModelGenerators itemModelGenerators) {
        TextureMapping textureMapping = new TextureMapping().put(TextureSlot.LAYER0, baseLocation)
                .put(TextureSlot.LAYER1, new Material(dyeSlotLocation.sprite().withSuffix("_left_dye_slot")))
                .put(TextureSlot.LAYER2, new Material(dyeSlotLocation.sprite().withSuffix("_middle_dye_slot")))
                .put(LAYER3_TEXTURE_SLOT, new Material(dyeSlotLocation.sprite().withSuffix("_right_dye_slot")));
        return ItemModelUtils.tintedModel(FOUR_LAYERED_ITEM.create(itemModel,
                        textureMapping,
                        itemModelGenerators.modelOutput),
                ItemModelUtils.constantTint(-1),
                new DyeChannelTintSource(DyeChannelTintSource.DyeSlot.LEFT),
                new DyeChannelTintSource(DyeChannelTintSource.DyeSlot.MIDDLE),
                new DyeChannelTintSource(DyeChannelTintSource.DyeSlot.RIGHT));
    }
}
