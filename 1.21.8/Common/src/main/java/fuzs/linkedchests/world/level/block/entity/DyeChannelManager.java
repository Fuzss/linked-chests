package fuzs.linkedchests.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.linkedchests.LinkedChests;
import fuzs.puzzleslib.api.event.v1.server.ServerLifecycleEvents;
import fuzs.puzzleslib.api.event.v1.server.ServerTickEvents;
import fuzs.puzzleslib.api.util.v1.CodecExtras;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.Collections;
import java.util.Map;

public final class DyeChannelManager extends SavedData {
    public static final Codec<DyeChannelManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecExtras.mapOf(DyeChannel.CODEC, DyeChannelStorage.CODEC)
                    .optionalFieldOf("dye_channels", Collections.emptyMap())
                    .forGetter(DyeChannelManager::getDyeChannels)).apply(instance, DyeChannelManager::new));
    public static final SavedDataType<DyeChannelManager> TYPE = new SavedDataType<>(LinkedChests.id(
            "dye_channel_manager").toDebugFileName(), DyeChannelManager::new, CODEC, null);
    private static DyeChannelManager instance;

    private final Map<DyeChannel, DyeChannelStorage> dyeChannels;

    private DyeChannelManager() {
        this.dyeChannels = new Object2ObjectArrayMap<>();
    }

    private DyeChannelManager(Map<DyeChannel, DyeChannelStorage> dyeChannels) {
        this.dyeChannels = new Object2ObjectArrayMap<>(dyeChannels);
    }

    @Override
    public boolean isDirty() {
        // always save, it's not practical to track changes to the item lists
        return true;
    }

    private Map<DyeChannel, DyeChannelStorage> getDyeChannels() {
        return this.dyeChannels;
    }

    public static void registerEventHandlers() {
        ServerLifecycleEvents.STARTED.register((MinecraftServer minecraftServer) -> {
            instance = minecraftServer.overworld().getDataStorage().computeIfAbsent(DyeChannelManager.TYPE);
        });
        ServerLifecycleEvents.STOPPED.register((MinecraftServer minecraftServer) -> {
            instance = null;
        });
        ServerTickEvents.END.register((MinecraftServer minecraftServer) -> {
            // the vanilla recheck runs as a block tick every five ticks while a chest is open
            // since this always runs on the server make it 20
            if (minecraftServer.getTickCount() % 20 == 0) {
                DyeChannelManager channelManager = instance;
                if (channelManager != null) {
                    channelManager.dyeChannels.forEach((DyeChannel dyeChannel, DyeChannelStorage storage) -> {
                        storage.openersCounter().recheckOpeners(dyeChannel, minecraftServer);
                    });
                }
            }
        });
    }

    public static DyeChannelStorage getStorage(DyeChannel dyeChannel) {
        DyeChannelManager channelManager = instance;
        if (channelManager != null) {
            return channelManager.dyeChannels.computeIfAbsent(dyeChannel, DyeChannel::createStorage);
        } else {
            return new DyeChannelStorage(3);
        }
    }
}