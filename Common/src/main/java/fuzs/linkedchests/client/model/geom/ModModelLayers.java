package fuzs.linkedchests.client.model.geom;

import fuzs.linkedchests.LinkedChests;
import fuzs.linkedchests.client.renderer.blockentity.LinkedChestModelSet;
import fuzs.puzzleslib.common.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModModelLayers {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(LinkedChests.MOD_ID);
    public static final ModelLayerLocation LINKED_CHEST_RIGHT_BUTTON_MODEL_LAYER = MODEL_LAYERS.registerModelLayer(
            "linked_chest",
            "right_button");
    public static final ModelLayerLocation LINKED_CHEST_MIDDLE_BUTTON_MODEL_LAYER = MODEL_LAYERS.registerModelLayer(
            "linked_chest",
            "middle_button");
    public static final ModelLayerLocation LINKED_CHEST_LEFT_BUTTON_MODEL_LAYER = MODEL_LAYERS.registerModelLayer(
            "linked_chest",
            "left_button");
    public static final ModelLayerLocation LINKED_CHEST_LOCK_MODEL_LAYER = MODEL_LAYERS.registerModelLayer(
            "linked_chest",
            "lock");
    public static final ModelLayerLocation LINKED_CHEST_MODEL_LAYER_LOCATION = MODEL_LAYERS.registerModelLayer(
            "linked_chest");
    public static final LinkedChestModelSet<ModelLayerLocation> LINKED_CHEST_MODEL_LAYERS = new LinkedChestModelSet<>(
            LINKED_CHEST_MODEL_LAYER_LOCATION,
            LINKED_CHEST_LOCK_MODEL_LAYER,
            LINKED_CHEST_LEFT_BUTTON_MODEL_LAYER,
            LINKED_CHEST_MIDDLE_BUTTON_MODEL_LAYER,
            LINKED_CHEST_RIGHT_BUTTON_MODEL_LAYER);
}
