package fuzs.linkedchests.fabric.client;

import fuzs.linkedchests.LinkedChests;
import fuzs.linkedchests.client.LinkedChestsClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class LinkedChestsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(LinkedChests.MOD_ID, LinkedChestsClient::new);
    }
}
