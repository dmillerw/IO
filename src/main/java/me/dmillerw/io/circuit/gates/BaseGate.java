package me.dmillerw.io.circuit.gates;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.data.Value;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseGate {

    public GateRegistry.GateKey key;

    private final Map<String, Port> inputs = Maps.newHashMap();
    private final Map<String, Port> outputs = Maps.newHashMap();

    public final void setKey(GateRegistry.GateKey key) {
        this.key = key;
    }

    public final void registerInput(Port port) {
        this.inputs.put(port.getName(), port);
    }

    public final void registerOutput(Port port) {
        this.outputs.put(port.getName(), port);
    }

    public final ImmutableList<Port> getInputs() {
        return ImmutableList.<Port>builder().addAll(inputs.values()).build();
    }

    public final ImmutableList<Port> getOutputs() {
        return ImmutableList.<Port>builder().addAll(outputs.values()).build();
    }

    public final Port getInput(String key) {
        return inputs.get(key);
    }

    public final Port getOutput(String key) {
        return outputs.get(key);
    }

    public final void setInput(String key, Value value) {
        this.inputs.get(key).setValue(value);
    }

    public final void setOutput(String key, Value value) {
        this.outputs.get(key).setValue(value);
    }

    public final boolean hasDirtyInputs() {
        return inputs.values().stream().filter(Port::isDirty).toList().size() > 0;
    }

    public final boolean hasDirtyOutputs() {
        return outputs.values().stream().filter(Port::isDirty).toList().size() > 0;
    }

    public final void markInputsClean() {
        inputs.values().forEach(Port::markClean);
    }
}
