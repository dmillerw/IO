package me.dmillerw.io.network.packets.client;

import me.dmillerw.io.circuit.gates.BaseGate;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packets.server.GateInfoResponse;
import me.dmillerw.io.world.GateSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class GateInfoRequest {

    public static GateInfoRequest decode(FriendlyByteBuf buffer) {
        return new GateInfoRequest(buffer.readBlockPos());
    }

    public static void handle(GateInfoRequest packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            ServerLevel level = player.getLevel();
            GateSavedData gateSavedData = GateSavedData.get(level.getDataStorage());
            BaseGate gate = gateSavedData.runner.getGate(packet.pos);

            GateInfoResponse response = new GateInfoResponse();
            response.inputs = gate.getInputs();
            response.outputs = gate.getOutputs();

            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), response);
        });
        ctx.get().setPacketHandled(true);
    }

    public BlockPos pos;

    public GateInfoRequest() {}
    public GateInfoRequest(BlockPos pos) {
        this.pos = pos;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
    }
}
