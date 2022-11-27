package me.dmillerw.io.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.dmillerw.io.block.GateBlock;
import me.dmillerw.io.block.ModBlocks;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.network.packets.client.GateInfoRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;

public class ClientEventHandler {

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();

        Level level = mc.level;
        if (level == null)
            return;

        HitResult result = mc.hitResult;

        if (result == null || result.getType() != HitResult.Type.BLOCK)
            return;

        BlockPos pos = new BlockPos(result.getLocation());

        if (!pos.equals(LocalGateInteractionHandler.currentBlockPos)) {
            LocalGateInteractionHandler.currentBlockPos = pos;
            LocalGateInteractionHandler.currentlyRequestingData = true;

            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof GateBlock)) {
                LocalGateInteractionHandler.currentlyHighlightingGate = false;
                return;
            }

            LocalGateInteractionHandler.currentlyHighlightingGate = true;

            GateInfoRequest packet = new GateInfoRequest(pos);
            PacketHandler.INSTANCE.sendToServer(packet);
        }
    }

    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (!LocalGateInteractionHandler.currentlyHighlightingGate)
            return;

        LocalGateInteractionHandler.handleScrollDelta(event.getScrollDelta());
        event.setCanceled(true);
    }
}
