package me.dmillerw.io.network.packets.server;

import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.client.ClientPacketHandler;
import me.dmillerw.io.network.packets.client.GateInfoRequest;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.function.Supplier;

public class GateInfoResponse {

    public static GateInfoResponse decode(FriendlyByteBuf buffer) {
        int inputCount = buffer.readInt();
        int outputCount = buffer.readInt();

        GateInfoResponse packet = new GateInfoResponse();

        for (int i=0; i<inputCount; i++) {
            packet.inputs.add(Port.fromByteBuf(buffer));
        }

        for (int i=0; i<outputCount; i++) {
            packet.outputs.add(Port.fromByteBuf(buffer));
        }

        return packet;
    }

    public static void handle(GateInfoResponse packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleGateInfoResponsePacket(packet, ctx));
        });
        ctx.get().setPacketHandled(true);
    }

    public List<Port> inputs = Lists.newArrayList();
    public List<Port> outputs = Lists.newArrayList();

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(inputs.size());
        buffer.writeInt(outputs.size());

        inputs.forEach(p -> p.writeToByteBuf(buffer));
        outputs.forEach(p -> p.writeToByteBuf(buffer));
    }
}
