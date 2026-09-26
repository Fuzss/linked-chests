package fuzs.linkedchests.common.data.client;

import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.linkedchests.common.world.level.block.LinkedChestBlock;
import fuzs.puzzleslib.common.api.client.data.v3.language.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations() {
        this.addBlock(ModRegistry.LINKED_CHEST_BLOCK, "Linked Chest");
        this.addItem(ModRegistry.LINKED_POUCH_ITEM, "Linked Pouch");
        this.add(((LinkedChestBlock) ModRegistry.LINKED_CHEST_BLOCK.value()).getDescriptionComponent(),
                "Grants access to items stored in interdimensional realms.");
    }
}
