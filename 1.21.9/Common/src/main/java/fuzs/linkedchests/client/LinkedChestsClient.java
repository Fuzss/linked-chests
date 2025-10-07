package fuzs.linkedchests.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.linkedchests.LinkedChests;
import fuzs.linkedchests.client.color.item.DyeChannelTintSource;
import fuzs.linkedchests.client.handler.DyeChannelLidController;
import fuzs.linkedchests.client.model.LinkedChestModel;
import fuzs.linkedchests.client.model.geom.ModModelLayers;
import fuzs.linkedchests.client.renderer.blockentity.LinkedChestBlockEntityRenderer;
import fuzs.linkedchests.client.renderer.item.properties.conditional.LinkedPouchOpenModelProperty;
import fuzs.linkedchests.client.renderer.item.properties.conditional.LinkedPouchPersonalModelProperty;
import fuzs.linkedchests.client.renderer.special.LinkedChestSpecialRenderer;
import fuzs.linkedchests.init.ModRegistry;
import fuzs.linkedchests.world.item.LinkedPouchItem;
import fuzs.linkedchests.world.level.block.HighlightShapeProvider;
import fuzs.linkedchests.world.level.block.LinkedChestBlock;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelsContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.core.v1.context.MenuScreensContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ClientPlayerNetworkEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.SubmitBlockOutlineCallback;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.ItemTooltipRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.world.level.block.state.BlockState;
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
        ClientPlayerNetworkEvents.LOGGED_IN.register(DyeChannelLidController::onLoggedIn);
        SubmitBlockOutlineCallback.EVENT.register((LevelRenderer levelRenderer, ClientLevel clientLevel, BlockState blockState, BlockHitResult hitResult, CollisionContext collisionContext, Camera camera) -> {
            if (blockState.getBlock() instanceof HighlightShapeProvider block && clientLevel.getWorldBorder()
                    .isWithinBounds(hitResult.getBlockPos())) {
                return EventResultHolder.allow((BlockOutlineRenderState renderState, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean isTranslucent, LevelRenderState levelRenderState) -> {
                    VoxelShape voxelShape = block.getHighlightShape(blockState,
                            clientLevel,
                            hitResult.getBlockPos(),
                            hitResult.getLocation());
                    renderState = new BlockOutlineRenderState(renderState.pos(),
                            renderState.isTranslucent(),
                            renderState.highContrast(),
                            voxelShape,
                            renderState.collisionShape(),
                            renderState.occlusionShape(),
                            renderState.interactionShape());
                    SubmitBlockOutlineCallback.CustomBlockOutlineRenderer.renderVanillaBlockOutline(renderState,
                            bufferSource,
                            poseStack,
                            isTranslucent,
                            levelRenderState);
                    return true;
                });
            }

            return EventResultHolder.pass();
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
}
