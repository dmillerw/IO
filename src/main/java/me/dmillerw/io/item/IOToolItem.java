package me.dmillerw.io.item;

import me.dmillerw.io.IO;
import me.dmillerw.io.block.GateBlock;
import me.dmillerw.io.client.LocalGateInteractionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class IOToolItem extends Item {

    public IOToolItem() {
        super(new Properties().stacksTo(1).tab(IO.CREATIVE_TAB));
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (!ctx.getLevel().isClientSide)
            return InteractionResult.SUCCESS;

        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof GateBlock))
            return InteractionResult.SUCCESS;

        LocalGateInteractionHandler.select();

        return InteractionResult.SUCCESS;
    }
}
