package me.dmillerw.io.client;

import me.dmillerw.io.network.packets.server.GateInfoResponse;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handleGateInfoResponsePacket(GateInfoResponse packet, Supplier<NetworkEvent.Context> ctx) {
        LocalGateInteractionHandler.currentlyRequestingData = false;
        LocalGateInteractionHandler.currentInputs = packet.inputs;
        LocalGateInteractionHandler.currentOutputs = packet.outputs;
        LocalGateInteractionHandler.currentPortIndex = 0;
    }
}
