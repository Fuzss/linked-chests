package fuzs.linkedchests.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.linkedchests.LinkedChests;
import fuzs.linkedchests.client.model.LinkedChestModel;
import fuzs.linkedchests.client.model.geom.ModModelLayers;
import fuzs.linkedchests.client.renderer.blockentity.state.LinkedChestRenderState;
import fuzs.linkedchests.world.level.block.entity.LinkedChestBlockEntity;
import fuzs.puzzleslib.common.api.client.renderer.v1.SingleChestRenderer;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class LinkedChestBlockEntityRenderer extends SingleChestRenderer<LinkedChestBlockEntity, ChestModel, LinkedChestRenderState> {
    public static final Identifier LINKED_CHEST_TEXTURE = LinkedChests.id("linked");
    public static final Identifier LINKED_CHEST_BUTTONS_TEXTURE = LinkedChests.id("linked_buttons");
    public static final SpriteId LINKED_CHEST_MATERIAL = Sheets.CHEST_MAPPER.apply(LINKED_CHEST_TEXTURE);
    public static final SpriteId LINKED_CHEST_BUTTONS_MATERIAL = Sheets.CHEST_MAPPER.apply(LINKED_CHEST_BUTTONS_TEXTURE);
    private final LinkedChestModelSet<ChestModel> chestModels;

    public LinkedChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this(context, ModModelLayers.LINKED_CHEST_MODEL_LAYERS.map(context::bakeLayer).map(LinkedChestModel::new));
    }

    public LinkedChestBlockEntityRenderer(BlockEntityRendererProvider.Context context, LinkedChestModelSet<ChestModel> chestModels) {
        super(context, chestModels.chest());
        this.chestModels = chestModels;
    }

    @Override
    public LinkedChestRenderState createRenderState() {
        return new LinkedChestRenderState();
    }

    @Override
    public void extractRenderState(LinkedChestBlockEntity blockEntity, ChestRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(blockEntity, state, partialTick, cameraPosition, crumblingOverlay);
        ((LinkedChestRenderState) state).buttonsMaterial = LINKED_CHEST_BUTTONS_MATERIAL;
        ((LinkedChestRenderState) state).isPersonal = blockEntity.getDyeChannel().uuid().isPresent();
        ((LinkedChestRenderState) state).slotColors = blockEntity.getDyeChannel().dyeColors();
    }

    @Override
    public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
        poseStack.pushPose();
        poseStack.mulPose(modelTransformation(state.facing));
        this.submitChestModel((LinkedChestRenderState) state,
                poseStack,
                submitNodeCollector,
                this.chestModels.lock(),
                ((LinkedChestRenderState) state).getLockMaterial(),
                -1);
        for (int i = 0; i < ((LinkedChestRenderState) state).slotColors.length; i++) {
            this.submitChestModel((LinkedChestRenderState) state,
                    poseStack,
                    submitNodeCollector,
                    this.chestModels.getButton(i),
                    ((LinkedChestRenderState) state).buttonsMaterial,
                    ((LinkedChestRenderState) state).slotColors[i]);
        }

        poseStack.popPose();
    }

    private void submitChestModel(LinkedChestRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ChestModel chestModel, SpriteId material, int color) {
        RenderType renderType = material.renderType(RenderTypes::entityCutout);
        TextureAtlasSprite textureAtlasSprite = this.sprites.get(material);
        submitNodeCollector.submitModel(chestModel,
                state.getOpenness(),
                poseStack,
                renderType,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                color,
                textureAtlasSprite,
                0,
                state.breakProgress);
    }

    @Override
    protected SpriteId getChestSprite(LinkedChestBlockEntity blockEntity) {
        return LINKED_CHEST_MATERIAL;
    }

    @Override
    protected boolean isXmas() {
        return false;
    }
}
