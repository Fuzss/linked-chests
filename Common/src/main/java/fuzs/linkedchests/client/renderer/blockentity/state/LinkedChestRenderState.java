package fuzs.linkedchests.client.renderer.blockentity.state;

import fuzs.linkedchests.client.renderer.blockentity.LinkedChestBlockEntityRenderer;
import fuzs.puzzleslib.common.api.client.renderer.v1.SingleChestRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;

public class LinkedChestRenderState extends SingleChestRenderer.SingleChestRenderState {
    public SpriteId buttonsMaterial = LinkedChestBlockEntityRenderer.LINKED_CHEST_BUTTONS_MATERIAL;
    public boolean isPersonal;
    public int[] slotColors = new int[3];

    public SpriteId getLockMaterial() {
        return this.isPersonal ? this.buttonsMaterial : this.chestMaterial;
    }
}
