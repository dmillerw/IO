package me.dmillerw.io.circuit;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import me.dmillerw.io.IO;
import me.dmillerw.io.circuit.data.NullValue;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.gates.BaseGate;
import me.dmillerw.io.circuit.gates.Compute;
import me.dmillerw.io.circuit.gates.Interactable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GateRunner {

    private Map<Long, BaseGate> gates = Maps.newHashMap();

    // Registered as OUT -> IN
    private HashBiMap<PortIdentity, PortIdentity> connectionMap = HashBiMap.create();

    private BaseGate getGate(PortIdentity port) {
        return getGate(port.position);
    }

    public BaseGate getGate(BlockPos position) {
        return getGate(position.asLong());
    }

    private BaseGate getGate(long _long) {
        return gates.getOrDefault(_long, null);
    }

    private Port getInputPort(PortIdentity identity) {
        BaseGate gate = getGate(identity);
        if (gate == null)
            return null;

        return gate.getInput(identity.key);
    }

    private Port getOutputPort(PortIdentity identity) {
        BaseGate gate = getGate(identity);
        if (gate == null)
            return null;

        return gate.getOutput(identity.key);
    }

    public void addGate(BlockPos position, BaseGate gate) {
        // Check to see if gate already exists, and if so, remove before adding
        if (gates.containsKey(position.asLong())) {
            removeGate(position);
        }

        gates.put(position.asLong(), gate);
    }

    public void removeGate(BlockPos position) {
        // Find every connection that relies on information from this gate
        connectionMap.entrySet().stream().filter((e) -> e.getKey().position == position.asLong())
                .forEach((e) -> {
                    // Grab the gate relying on this gate, and clear its input for the specified port
                    BaseGate gate = getGate(e.getValue());
                    gate.setInput(e.getValue().key, NullValue.NULL);

                    // Remove the connection
                    connectionMap.remove(e.getKey());
                });

        // Remove the gate itself
        gates.remove(position.asLong());
    }

    public void addConnection(PortIdentity output, PortIdentity input) {
        // Validate the connection is indeed valid
        Port out = getOutputPort(output);
        Port in = getInputPort(input);

        if (out == null || in == null)
            return;

        if (out.getType() != in.getType())
            return;

        // Register the connection
        connectionMap.put(output, input);

        // Update the input port with outputs last value
        in.setValue(out.getValue());
    }

    public void removeConnection(PortIdentity output, PortIdentity input) {
        // Update the input port with null value
        Port out = getOutputPort(output);
        Port in = getInputPort(input);

        if (out == null || in == null)
            return;

        in.setValue(NullValue.NULL);

        // Remove the connection
        connectionMap.remove(output);
    }

    public boolean interactWithGate(Level level, BlockPos position, Player player, InteractionHand hand) {
        BaseGate gate = getGate(position);
        if (gate instanceof Interactable) {
            return ((Interactable) gate).onRightClick(level, position, player, hand);
        } else {
            return false;
        }
    }

    public void tick() {
        // Updates are simple. Every tick, propagate the changes from any dirty outputs to their inputs, then request
        // the gate to update, if changes have been made

//        IO.LOGGER.info(gates.size());

        connectionMap.forEach((key, value) -> {
            Port out = getOutputPort(key);

            // TODO Error correction, remove connection if port = null
            if (out == null || !out.isDirty())
                return;

            Port in = getInputPort(value);
            if (in == null)
                return;

            in.setValue(out.getValue());
            out.markClean();
        });

        gates.values().forEach((g) -> {
            if (!g.hasDirtyInputs())
                return;

            if (g instanceof Compute)
                ((Compute) g).computeChanges();
            g.markInputsClean();
        });
    }

    public void loadFromNbt(CompoundTag tag) {

    }

    public void saveToNbt(CompoundTag tag) {

    }
}
