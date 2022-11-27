package me.dmillerw.io.circuit.gates.impl.util;

import me.dmillerw.io.IO;
import me.dmillerw.io.circuit.data.DataType;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.circuit.data.Value;
import me.dmillerw.io.circuit.gates.BaseGate;
import me.dmillerw.io.circuit.gates.Interactable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ConstantValueGate extends BaseGate implements Interactable {

    public ConstantValueGate() {
        registerOutput(Port.create("Out", DataType.NUMBER));
    }

    @Override
    public boolean onRightClick(Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            setOutput("Out", Value.of(DataType.NUMBER, getOutput("Out").getValue().getNumber().intValue() - 1));
        } else {
            setOutput("Out", Value.of(DataType.NUMBER, getOutput("Out").getValue().getNumber().intValue() + 1));
        }

        IO.LOGGER.info(getOutput("Out").getValue().getNumber());

        return true;
    }
}
