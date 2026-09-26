package fuzs.linkedchests.neoforge;

import fuzs.linkedchests.common.LinkedChests;
import fuzs.linkedchests.common.data.ModRecipeProvider;
import fuzs.linkedchests.common.data.loot.ModBlockLootProvider;
import fuzs.linkedchests.common.data.tags.ModBlockTagProvider;
import fuzs.linkedchests.common.data.tags.ModItemTagProvider;
import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.linkedchests.common.world.level.block.entity.DyeChannel;
import fuzs.linkedchests.common.world.level.block.entity.DyeChannelManager;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import fuzs.puzzleslib.neoforge.api.init.v3.capability.NeoForgeCapabilityHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

@Mod(LinkedChests.MOD_ID)
public class LinkedChestsNeoForge {

    public LinkedChestsNeoForge() {
        ModConstructor.construct(LinkedChests.MOD_ID, LinkedChests::new);
        NeoForgeCapabilityHelper.registerBlockEntityContainer(ModRegistry.LINKED_CHEST_BLOCK_ENTITY);
        NeoForgeCapabilityHelper.registerItemContainer((ItemStack itemStack, ItemAccess itemAccess) -> {
            DyeChannel dyeChannel = itemStack.getOrDefault(ModRegistry.DYE_CHANNEL_DATA_COMPONENT_TYPE.value(),
                    DyeChannel.DEFAULT);
            return new ItemStacksResourceHandler(DyeChannelManager.getStorage(dyeChannel).items());
        }, ModRegistry.LINKED_POUCH_ITEM);
        DataProviderBuilder.of(LinkedChests.MOD_ID)
                .addLootProvider(ModBlockLootProvider::new, LootContextParamSets.BLOCK)
                .addProvider(ModBlockTagProvider::new, ModItemTagProvider::new)
                .addRecipeProvider(ModRecipeProvider::new);
    }
}
