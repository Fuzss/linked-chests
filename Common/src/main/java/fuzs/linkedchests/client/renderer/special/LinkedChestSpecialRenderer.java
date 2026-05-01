package fuzs.linkedchests.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.linkedchests.client.model.LinkedChestModel;
import fuzs.linkedchests.client.model.geom.ModModelLayers;
import fuzs.linkedchests.client.renderer.blockentity.LinkedChestModelSet;
import fuzs.linkedchests.init.ModRegistry;
import fuzs.linkedchests.world.level.block.entity.DyeChannel;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * @see net.minecraft.client.renderer.special.ChestSpecialRenderer
 */
public class LinkedChestSpecialRenderer implements SpecialModelRenderer<DyeChannel> {
    private final SpriteGetter sprites;
    private final LinkedChestModelSet<ChestModel> chestModels;
    private final SpriteId chestMaterial;
    private final SpriteId buttonsMaterial;
    private final float openness;

    public LinkedChestSpecialRenderer(SpriteGetter sprites, LinkedChestModelSet<ChestModel> chestModels, SpriteId chestMaterial, SpriteId buttonsMaterial, float openness) {
        this.sprites = sprites;
        this.chestModels = chestModels;
        this.chestMaterial = chestMaterial;
        this.buttonsMaterial = buttonsMaterial;
        this.openness = openness;
    }

    @Override
    public void submit(DyeChannel dyeChannel, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoilType, int outlineColor) {
        this.submitChestModel(poseStack,
                submitNodeCollector,
                packedLight,
                packedOverlay,
                outlineColor,
                this.chestModels.chest(),
                this.chestMaterial,
                -1);
        this.submitChestModel(poseStack,
                submitNodeCollector,
                packedLight,
                packedOverlay,
                outlineColor,
                this.chestModels.lock(),
                dyeChannel.uuid().isPresent() ? this.buttonsMaterial : this.chestMaterial,
                -1);
        int[] dyeColors = dyeChannel.dyeColors();
        for (int i = 0; i < dyeColors.length; i++) {
            this.submitChestModel(poseStack,
                    submitNodeCollector,
                    packedLight,
                    packedOverlay,
                    outlineColor,
                    this.chestModels.getButton(i),
                    this.buttonsMaterial,
                    dyeColors[i]);
        }
    }

    private void submitChestModel(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, int outlineColor, ChestModel chestModel, SpriteId material, int color) {
        submitNodeCollector.submitModel(chestModel,
                this.openness,
                poseStack,
                material.renderType(RenderTypes::entitySolid),
                packedLight,
                packedOverlay,
                color,
                this.sprites.get(material),
                outlineColor,
                null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.chestModels.chest().setupAnim(this.openness);
        this.chestModels.chest().root().getExtentsForGui(poseStack, consumer);
    }

    @Override
    public DyeChannel extractArgument(ItemStack itemStack) {
        return itemStack.getOrDefault(ModRegistry.DYE_CHANNEL_DATA_COMPONENT_TYPE.value(), DyeChannel.DEFAULT);
    }

    public record Unbaked(Identifier chestTexture,
                          Identifier buttonsTexture,
                          float openness) implements SpecialModelRenderer.Unbaked<DyeChannel> {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        Identifier.CODEC.fieldOf("chest_texture").forGetter(Unbaked::chestTexture),
                        Identifier.CODEC.fieldOf("buttons_texture").forGetter(Unbaked::buttonsTexture),
                        Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(Unbaked::openness))
                .apply(instance, Unbaked::new));

        public Unbaked(Identifier chestTexture, Identifier buttonsTexture) {
            this(chestTexture, buttonsTexture, 0.0F);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public LinkedChestSpecialRenderer bake(BakingContext context) {
            LinkedChestModelSet<ChestModel> chestModels = ModModelLayers.LINKED_CHEST_MODEL_LAYERS.map(context.entityModelSet()::bakeLayer)
                    .map(LinkedChestModel::new);
            return new LinkedChestSpecialRenderer(context.sprites(),
                    chestModels,
                    Sheets.CHEST_MAPPER.apply(this.chestTexture),
                    Sheets.CHEST_MAPPER.apply(this.buttonsTexture),
                    this.openness);
        }
    }
}
