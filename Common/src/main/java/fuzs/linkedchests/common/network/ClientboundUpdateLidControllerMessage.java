package fuzs.linkedchests.common.network;

import fuzs.linkedchests.common.client.handler.DyeChannelLidController;
import fuzs.linkedchests.common.world.level.block.entity.DyeChannel;
import fuzs.puzzleslib.common.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.common.api.network.v4.message.play.ClientboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundUpdateLidControllerMessage(DyeChannel dyeChannel,
                                                    boolean shouldBeOpen) implements ClientboundPlayMessage {
    public static final StreamCodec<ByteBuf, ClientboundUpdateLidControllerMessage> STREAM_CODEC = StreamCodec.composite(DyeChannel.STREAM_CODEC,
            ClientboundUpdateLidControllerMessage::dyeChannel,
            ByteBufCodecs.BOOL,
            ClientboundUpdateLidControllerMessage::shouldBeOpen,
            ClientboundUpdateLidControllerMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                DyeChannelLidController.getChestLidController(ClientboundUpdateLidControllerMessage.this.dyeChannel)
                        .shouldBeOpen(ClientboundUpdateLidControllerMessage.this.shouldBeOpen);
            }
        };
    }
}
