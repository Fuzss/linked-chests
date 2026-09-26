package fuzs.linkedchests.neoforge.client;

import fuzs.linkedchests.common.LinkedChests;
import fuzs.linkedchests.common.client.LinkedChestsClient;
import fuzs.linkedchests.common.data.client.ModLanguageProvider;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import fuzs.linkedchests.common.data.client.ModModelProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = LinkedChests.MOD_ID, dist = Dist.CLIENT)
public class LinkedChestsNeoForgeClient {

    public LinkedChestsNeoForgeClient() {
        ClientModConstructor.construct(LinkedChests.MOD_ID, LinkedChestsClient::new);
        DataProviderBuilder.of(LinkedChests.MOD_ID).addProvider(ModLanguageProvider::new, ModModelProvider::new);
    }
}
