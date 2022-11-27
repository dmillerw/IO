package me.dmillerw.io.network;

import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.network.packets.client.GateConnectionRequest;
import me.dmillerw.io.network.packets.client.GateInfoRequest;
import me.dmillerw.io.network.packets.server.GateInfoResponse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.IndexedMessageCodec;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {

    private static final String VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ModInfo.ID, "main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );

    private static int packetId = 0;

    private static <MSG> void registerPacket(Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        INSTANCE.registerMessage(
                packetId,
                messageType,
                encoder,
                decoder,
                messageConsumer
        );
        packetId++;
    }

    public static void initialize() {
        registerPacket(GateInfoRequest.class, GateInfoRequest::encode, GateInfoRequest::decode, GateInfoRequest::handle);
        registerPacket(GateInfoResponse.class, GateInfoResponse::encode, GateInfoResponse::decode, GateInfoResponse::handle);
        registerPacket(GateConnectionRequest.class, GateConnectionRequest::encode, GateConnectionRequest::decode, GateConnectionRequest::handle);
    }
}
