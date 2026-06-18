package fuzs.linkedchests.common.data.tags;

import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ModItemTagProvider extends AbstractTagProvider<Item> {

    public ModItemTagProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ItemTags.VANISHING_ENCHANTABLE).add(ModRegistry.LINKED_POUCH_ITEM);
        this.tag(ModRegistry.DYE_CHANNEL_COLOR_PROVIDERS_ITEM_TAG).addOptionalTag("c:dyes");
        this.tag(ModRegistry.PERSONAL_CHANNEL_PROVIDERS_ITEM_TAG).add(ItemIds.DIAMOND);
    }
}
