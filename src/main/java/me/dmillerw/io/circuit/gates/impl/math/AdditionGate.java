package me.dmillerw.io.circuit.gates.impl.math;

import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.data.Value;
import me.dmillerw.io.circuit.gates.BaseGate;
import me.dmillerw.io.circuit.gates.Compute;

import java.util.Arrays;

public class AdditionGate extends BaseGate implements Compute {

    private static final String[] INPUTS = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};

    public AdditionGate() {
        Arrays.stream(INPUTS).forEach(k -> {
            Port port = Port.create(k, DataType.NUMBER);
            registerInput(port);
        });
        registerOutput(Port.create("Out", DataType.NUMBER));
    }

    @Override
    public void computeChanges() {
        final double[] bucket = {0};
        Arrays.stream(INPUTS).forEach(k -> {
            Value value = getInput(k).getValue();
            bucket[0] += value.getNumber().doubleValue();
        });

        setOutput("Out", Value.of(DataType.NUMBER, bucket[0]));
    }
}
