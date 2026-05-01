package fuzs.linkedchests.world.inventory;

import fuzs.linkedchests.world.level.block.entity.DyeChannel;
import fuzs.linkedchests.world.level.block.entity.DyeChannelManager;
import fuzs.linkedchests.world.level.block.entity.DyeChannelStorage;
import fuzs.puzzleslib.common.api.container.v1.ListBackedContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.item.ItemStack;

public final class DyeChannelContainer implements ListBackedContainer {
    private final DyeChannel dyeChannel;
    private final DyeChannelStorage storage;

    public DyeChannelContainer(DyeChannel dyeChannel) {
        this.dyeChannel = dyeChannel;
        this.storage = DyeChannelManager.getStorage(dyeChannel);
    }

    @Override
    public void startOpen(ContainerUser containerUser) {
        if (containerUser instanceof ServerPlayer serverPlayer && !serverPlayer.isSpectator()) {
            this.storage.openersCounter().incrementOpeners(this.dyeChannel, serverPlayer);
        }
    }

    @Override
    public void stopOpen(ContainerUser containerUser) {
        if (containerUser instanceof ServerPlayer serverPlayer && !serverPlayer.isSpectator()) {
            this.storage.openersCounter().decrementOpeners(this.dyeChannel, serverPlayer);
        }
    }

    @Override
    public NonNullList<ItemStack> getContainerItems() {
        return this.storage.items();
    }
}
