package me.dmillerw.io.block;

import me.dmillerw.io.circuit.gates.GateRegistry;
import me.dmillerw.io.world.GateSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class GateBlock extends Block {

    private final GateRegistry.GateKey gate;

    public GateBlock(GateRegistry.GateKey gate) {
        super(Properties.of(Material.METAL).strength(2f));

        this.gate = gate;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        ServerLevel serverLevel = (ServerLevel) level;
        GateSavedData gateSavedData = GateSavedData.get(serverLevel.getDataStorage());
        return gateSavedData.runner.interactWithGate(level, position, player, hand) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos position, BlockState newStae, boolean p_60519_) {
        super.onRemove(oldState, level, position, newStae, p_60519_);

        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            GateSavedData gateSavedData = GateSavedData.get(serverLevel.getDataStorage());
            gateSavedData.runner.removeGate(position);
        }
    }
}
