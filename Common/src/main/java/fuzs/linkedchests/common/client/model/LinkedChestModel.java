package fuzs.linkedchests.common.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.object.chest.ChestModel;

import java.util.Set;

public class LinkedChestModel extends ChestModel {
    private final ModelPart lid;
    private final ModelPart[] buttons = new ModelPart[3];

    public LinkedChestModel(ModelPart root) {
        super(root);
        this.lid = root.getChild("lid");
        this.buttons[0] = root.getChild("left_button");
        this.buttons[1] = root.getChild("middle_button");
        this.buttons[2] = root.getChild("right_button");
    }

    public static LayerDefinition createSingleBodyLayer() {
        return createBodyLayer(Set.of("bottom", "lid"));
    }

    public static LayerDefinition createLockLayer() {
        return createBodyLayer(Set.of("lock"));
    }

    public static LayerDefinition createLeftButtonLayer() {
        return createBodyLayer(Set.of("left_button"));
    }

    public static LayerDefinition createMiddleButtonLayer() {
        return createBodyLayer(Set.of("middle_button"));
    }

    public static LayerDefinition createRightButtonLayer() {
        return createBodyLayer(Set.of("right_button"));
    }

    private static LayerDefinition createBodyLayer(Set<String> parts) {
        return ChestModel.createSingleBodyLayer()
                .apply(LinkedChestModel::applyLinkedChestTransformation)
                .apply((MeshDefinition meshDefinition) -> {
                    meshDefinition.getRoot().retainExactParts(parts);
                    return meshDefinition;
                });
    }

    private static MeshDefinition applyLinkedChestTransformation(MeshDefinition meshDefinition) {
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("left_button",
                CubeListBuilder.create().texOffs(0, 5).addBox(4.0F, 5.0F, 5.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 9.0F, 1.0F));
        partDefinition.addOrReplaceChild("middle_button",
                CubeListBuilder.create().texOffs(0, 10).addBox(7.0F, 5.0F, 5.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 9.0F, 1.0F));
        partDefinition.addOrReplaceChild("right_button",
                CubeListBuilder.create().texOffs(0, 15).addBox(10.0F, 5.0F, 5.0F, 2.0F, 1.0F, 4.0F),
                PartPose.offset(0.0F, 9.0F, 1.0F));
        return meshDefinition;
    }

    @Override
    public void setupAnim(Float openness) {
        super.setupAnim(openness);
        for (ModelPart modelPart : this.buttons) {
            modelPart.xRot = this.lid.xRot;
        }
    }
}
