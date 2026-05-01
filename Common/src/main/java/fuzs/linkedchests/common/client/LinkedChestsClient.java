package fuzs.linkedchests.common.client;

import fuzs.linkedchests.common.LinkedChests;
import fuzs.linkedchests.common.client.color.item.DyeChannelTintSource;
import fuzs.linkedchests.common.client.handler.DyeChannelLidController;
import fuzs.linkedchests.common.client.model.LinkedChestModel;
import fuzs.linkedchests.common.client.model.geom.ModModelLayers;
import fuzs.linkedchests.common.client.renderer.blockentity.LinkedChestBlockEntityRenderer;
import fuzs.linkedchests.common.client.renderer.item.properties.conditional.LinkedPouchOpenModelProperty;
import fuzs.linkedchests.common.client.renderer.item.properties.conditional.LinkedPouchPersonalModelProperty;
import fuzs.linkedchests.common.client.renderer.special.LinkedChestSpecialRenderer;
import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.linkedchests.common.world.item.LinkedPouchItem;
import fuzs.linkedchests.common.world.level.block.HighlightShapeProvider;
import fuzs.linkedchests.common.world.level.block.LinkedChestBlock;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.common.api.client.core.v1.context.*;
import fuzs.puzzleslib.common.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.common.api.client.event.v1.entity.player.ClientPlayerNetworkEvents;
import fuzs.puzzleslib.common.api.client.event.v1.renderer.ExtractBlockOutlineCallback;
import fuzs.puzzleslib.common.api.client.gui.v2.tooltip.ItemTooltipRegistry;
import fuzs.puzzleslib.common.api.event.v1.core.EventResultHolder;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BuiltInBlockModels;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LinkedChestsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientTickEvents.END.register(DyeChannelLidController::onEndClientTick);
        ClientPlayerNetworkEvents.JOIN.register(DyeChannelLidController::onPlayerJoin);
        ExtractBlockOutlineCallback.EVENT.register((ClientLevel clientLevel, BlockPos blockPos, BlockState blockState, BlockHitResult hitResult, CollisionContext collisionContext) -> {
            if (blockState.getBlock() instanceof HighlightShapeProvider block && clientLevel.getWorldBorder()
                    .isWithinBounds(hitResult.getBlockPos())) {
                VoxelShape voxelShape = block.getHighlightShape(blockState,
                        clientLevel,
                        hitResult.getBlockPos(),
                        hitResult.getLocation());
                return EventResultHolder.interrupt(voxelShape);
            } else {
                return EventResultHolder.pass();
            }
        });
    }

    @Override
    public void onClientSetup() {
        ItemTooltipRegistry.BLOCK.registerItemTooltip(LinkedChestBlock.class,
                LinkedChestBlock::getDescriptionComponent);
        ItemTooltipRegistry.ITEM.registerItemTooltip(LinkedPouchItem.class, LinkedPouchItem::getDescriptionComponent);
    }

    @Override
    public void onRegisterItemModels(ItemModelsContext context) {
        context.registerItemTintSource(LinkedChests.id("dye_channel"), DyeChannelTintSource.MAP_CODEC);
        context.registerConditionalItemModelProperty(LinkedChests.id("linked_pouch/open"),
                LinkedPouchOpenModelProperty.MAP_CODEC);
        context.registerConditionalItemModelProperty(LinkedChests.id("linked_pouch/personal"),
                LinkedPouchPersonalModelProperty.MAP_CODEC);
        context.registerSpecialModelRenderer(LinkedChests.id("linked_chest"),
                LinkedChestSpecialRenderer.Unbaked.MAP_CODEC);
    }

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.LINKED_STORAGE_MENU_TYPE.value(), ContainerScreen::new);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ModModelLayers.LINKED_CHEST_MODEL_LAYERS.chest(),
                LinkedChestModel::createSingleBodyLayer);
        context.registerLayerDefinition(ModModelLayers.LINKED_CHEST_MODEL_LAYERS.lock(),
                LinkedChestModel::createLockLayer);
        context.registerLayerDefinition(ModModelLayers.LINKED_CHEST_MODEL_LAYERS.leftButton(),
                LinkedChestModel::createLeftButtonLayer);
        context.registerLayerDefinition(ModModelLayers.LINKED_CHEST_MODEL_LAYERS.middleButton(),
                LinkedChestModel::createMiddleButtonLayer);
        context.registerLayerDefinition(ModModelLayers.LINKED_CHEST_MODEL_LAYERS.rightButton(),
                LinkedChestModel::createRightButtonLayer);
    }

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.LINKED_CHEST_BLOCK_ENTITY.value(),
                LinkedChestBlockEntityRenderer::new);
    }

    @Override
    public void onRegisterBuiltInBlockModels(BuiltInBlockModelsContext context) {
        context.registerModelFactory(ModRegistry.LINKED_CHEST_BLOCK.value(),
                createSingletonChest(LinkedChestBlockEntityRenderer.LINKED_CHEST_TEXTURE));
    }

    /**
     * @see BuiltInBlockModels#createSingletonChest(Identifier)
     */
    public static BuiltInBlockModels.SpecialModelFactory createSingletonChest(Identifier texture) {
        return BuiltInBlockModels.specialModelWithPropertyDispatch(ChestBlock.FACING,
                (Direction facing) -> createChest(texture, facing));
    }

    /**
     * @see BuiltInBlockModels#createChest(Identifier, ChestType, Direction)
     */
    public static BlockModel.Unbaked createChest(Identifier texture, Direction facing) {
        return BuiltInBlockModels.special(new LinkedChestSpecialRenderer.Unbaked(texture,
                        LinkedChestBlockEntityRenderer.LINKED_CHEST_BUTTONS_TEXTURE),
                ChestRenderer.modelTransformation(facing));
    }
}
