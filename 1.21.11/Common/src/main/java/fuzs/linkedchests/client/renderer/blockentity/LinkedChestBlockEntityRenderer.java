package fuzs.linkedchests.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.linkedchests.LinkedChests;
import fuzs.linkedchests.client.model.LinkedChestModel;
import fuzs.linkedchests.client.model.geom.ModModelLayers;
import fuzs.linkedchests.client.renderer.blockentity.state.LinkedChestRenderState;
import fuzs.linkedchests.world.level.block.entity.LinkedChestBlockEntity;
import fuzs.puzzleslib.api.client.renderer.v1.SingleChestRenderer;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LinkedChestBlockEntityRenderer extends SingleChestRenderer<LinkedChestBlockEntity, ChestModel, LinkedChestRenderState> {
    public static final ResourceLocation LINKED_CHEST_TEXTURE = LinkedChests.id("linked");
    public static final ResourceLocation LINKED_CHEST_BUTTONS_TEXTURE = LinkedChests.id("linked_buttons");
    public static final Material LINKED_CHEST_MATERIAL = Sheets.CHEST_MAPPER.apply(LINKED_CHEST_TEXTURE);
    public static final Material LINKED_CHEST_BUTTONS_MATERIAL = Sheets.CHEST_MAPPER.apply(LINKED_CHEST_BUTTONS_TEXTURE);
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
    public void extractRenderState(LinkedChestBlockEntity blockEntity, ChestRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, crumblingOverlay);
        ((LinkedChestRenderState) renderState).buttonsMaterial = LINKED_CHEST_BUTTONS_MATERIAL;
        ((LinkedChestRenderState) renderState).isPersonal = blockEntity.getDyeChannel().uuid().isPresent();
        ((LinkedChestRenderState) renderState).slotColors = blockEntity.getDyeChannel().dyeColors();
    }

    @Override
    protected void submitChestModel(LinkedChestRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        super.submitChestModel(renderState, poseStack, submitNodeCollector);
        this.submitChestModel(renderState,
                poseStack,
                submitNodeCollector,
                this.chestModels.lock(),
                renderState.getLockMaterial(),
                -1);
        for (int i = 0; i < renderState.slotColors.length; i++) {
            this.submitChestModel(renderState,
                    poseStack,
                    submitNodeCollector,
                    this.chestModels.getButton(i),
                    renderState.buttonsMaterial,
                    renderState.slotColors[i]);
        }
    }

    private void submitChestModel(LinkedChestRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ChestModel chestModel, Material material, int color) {
        RenderType renderType = material.renderType(RenderType::entityCutout);
        TextureAtlasSprite textureAtlasSprite = this.materials.get(material);
        submitNodeCollector.submitModel(chestModel,
                renderState.getOpenness(),
                poseStack,
                renderType,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                color,
                textureAtlasSprite,
                0,
                renderState.breakProgress);
    }

    @Override
    protected Material getChestMaterial(LinkedChestBlockEntity blockEntity, boolean xmasTextures) {
        return xmasTextures ? Sheets.CHEST_XMAS_LOCATION : LINKED_CHEST_MATERIAL;
    }
}
