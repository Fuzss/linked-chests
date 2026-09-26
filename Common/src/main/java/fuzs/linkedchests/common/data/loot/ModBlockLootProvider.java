package fuzs.linkedchests.common.data.loot;

import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.data.v3.loot.AbstractBlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class ModBlockLootProvider extends AbstractBlockLootSubProvider {

    public ModBlockLootProvider(LootTableSubProvider.Context context) {
        super(context);
    }

    @Override
    public void generate() {
        this.add(ModRegistry.LINKED_CHEST_BLOCK.value(), this::createLinkedChestDrop);
    }

    public LootTable.Builder createLinkedChestDrop(Block block) {
        return LootTable.lootTable()
                .withPool(this.applyExplosionCondition(block,
                        LootPool.lootPool()
                                .setRolls(ContextIntProviders.exactly(1))
                                .add(LootItem.lootTableItem(block)
                                        .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                                .include(ModRegistry.DYE_CHANNEL_DATA_COMPONENT_TYPE.value())))));
    }
}
