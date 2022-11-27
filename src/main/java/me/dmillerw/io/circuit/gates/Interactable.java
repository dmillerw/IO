package me.dmillerw.io.circuit.gates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface Interactable {

    public boolean onRightClick(Level level, BlockPos pos, Player player, InteractionHand hand);
}
