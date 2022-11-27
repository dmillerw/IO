package me.dmillerw.io.item.block;

import me.dmillerw.io.IO;
import me.dmillerw.io.block.ModBlocks;
import me.dmillerw.io.circuit.gates.GateRegistry;
import me.dmillerw.io.circuit.gates.impl.math.AdditionGate;
import me.dmillerw.io.world.GateSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GateItem extends BlockItem {

    private final GateRegistry.GateKey gate;
    public GateItem(Block block, GateRegistry.GateKey gate) {
        super(block, new Properties().tab(IO.CREATIVE_TAB));

        this.gate = gate;
    }

    @Override
    public void appendHoverText(ItemStack p_40572_, @Nullable Level p_40573_, List<Component> p_40574_, TooltipFlag p_40575_) {
        p_40574_.add(Component.translatable(gate.toString()));
    }

    @Override
    public @NotNull InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult result = super.place(ctx);
        if (!ctx.getLevel().isClientSide) {
            ServerLevel level = (ServerLevel) ctx.getLevel();
            GateSavedData gateSavedData = GateSavedData.get(level.getDataStorage());
            gateSavedData.runner.addGate(ctx.getClickedPos(), GateRegistry.instantiate(this.gate));
        }
        return result;
    }


}
