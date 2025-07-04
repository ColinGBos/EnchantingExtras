package vapourdrive.enchantingextras.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import vapourdrive.enchantingextras.EnchantingExtras;

@EventBusSubscriber(modid = EnchantingExtras.MODID)
public final class Network {

    @SubscribeEvent
    private static void registerPayloadHandler(RegisterPayloadHandlersEvent evt) {
        PayloadRegistrar registrar = evt.registrar(EnchantingExtras.MODID).versioned("1");
        registrar.playToServer(StartProcessNotification.TYPE, StartProcessNotification.STREAM_CODEC, InvHandler::startProcess);
        EnchantingExtras.debugLog("registered Payload handler");
    }

    public record StartProcessNotification(BlockPos blockPos) implements CustomPacketPayload {

        static final Type<StartProcessNotification> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnchantingExtras.MODID, "start"));
        public static final StreamCodec<ByteBuf, StartProcessNotification> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC,
                StartProcessNotification::blockPos,
                StartProcessNotification::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}