package fuzs.linkedchests.common;

import fuzs.linkedchests.common.config.ServerConfig;
import fuzs.linkedchests.common.init.ModRegistry;
import fuzs.linkedchests.common.network.ClientboundUpdateLidControllerMessage;
import fuzs.linkedchests.common.world.level.block.entity.DyeChannelManager;
import fuzs.puzzleslib.common.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.common.api.event.v1.BuildCreativeModeTabContentsCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedChests implements ModConstructor {
    public static final String MOD_ID = "linkedchests";
    public static final String MOD_NAME = "Linked Chests";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        DyeChannelManager.registerEventHandlers();
        BuildCreativeModeTabContentsCallback.buildCreativeModeTabContents(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .register((CreativeModeTab creativeModeTab, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) -> {
                    output.accept(ModRegistry.LINKED_CHEST_ITEM.value());
                    output.accept(ModRegistry.LINKED_POUCH_ITEM.value());
                });
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.playToClient(ClientboundUpdateLidControllerMessage.class,
                ClientboundUpdateLidControllerMessage.STREAM_CODEC);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
