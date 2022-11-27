package me.dmillerw.io.network.packets.client;

import me.dmillerw.io.circuit.PortIdentity;
import me.dmillerw.io.circuit.gates.BaseGate;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packets.server.GateInfoResponse;
import me.dmillerw.io.world.GateSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class GateConnectionRequest {

    public static GateConnectionRequest decode(FriendlyByteBuf buffer) {
        PortIdentity output = new PortIdentity(buffer.readLong(), buffer.readUtf());
        PortIdentity input = new PortIdentity(buffer.readLong(), buffer.readUtf());
        return new GateConnectionRequest(output, input);
    }

    public static void handle(GateConnectionRequest packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            ServerLevel level = player.getLevel();
            GateSavedData gateSavedData = GateSavedData.get(level.getDataStorage());

            gateSavedData.runner.addConnection(packet.output, packet.input);
        });
        ctx.get().setPacketHandled(true);
    }

    public PortIdentity output;
    public PortIdentity input;

    public GateConnectionRequest(PortIdentity output, PortIdentity input) {
        this.output = output;
        this.input = input;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeLong(output.position);
        buffer.writeUtf(output.key);
        buffer.writeLong(input.position);
        buffer.writeUtf(input.key);
    }
}
