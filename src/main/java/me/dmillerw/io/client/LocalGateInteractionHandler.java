package me.dmillerw.io.client;

import me.dmillerw.io.IO;
import me.dmillerw.io.circuit.PortIdentity;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packets.client.GateConnectionRequest;
import net.minecraft.core.BlockPos;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class LocalGateInteractionHandler {

    public static BlockPos currentBlockPos = BlockPos.ZERO;

    public static List<Port> currentInputs = Lists.newArrayList();
    public static List<Port> currentOutputs = Lists.newArrayList();

    public static int currentPortIndex = 0;

    public static boolean currentlyHighlightingGate = false;
    public static boolean currentlyRequestingData = false;

    public static PortIdentity currentOutputPort = null;
    public static PortIdentity currentInputPort = null;

    public static void handleScrollDelta(double scrollDelta) {
        int change = scrollDelta > 0 ? 1 : scrollDelta < 0 ? -1 : 0;

        IO.LOGGER.info(change);

        if (currentOutputPort == null) {
            currentPortIndex += change;
            if (currentPortIndex < 0)
                currentPortIndex = 0;
            if (currentPortIndex > currentOutputs.size() - 1)
                currentPortIndex = currentOutputs.size() - 1;
        } else if (currentInputPort == null) {
            currentPortIndex += change;
            if (currentPortIndex < 0)
                currentPortIndex = 0;
            if (currentPortIndex > currentInputs.size() - 1)
                currentPortIndex = currentInputs.size() - 1;
        }
    }

    public static void select() {
        if (currentOutputPort ==  null) {
            currentOutputPort = new PortIdentity(currentBlockPos.asLong(), currentOutputs.get(currentPortIndex).getName());
        } else if (currentInputPort == null) {
            currentInputPort = new PortIdentity(currentBlockPos.asLong(), currentInputs.get(currentPortIndex).getName());

            GateConnectionRequest packet = new GateConnectionRequest(currentOutputPort, currentInputPort);
            PacketHandler.INSTANCE.sendToServer(packet);

            currentOutputPort = null;
            currentInputPort = null;
        }
    }
}
