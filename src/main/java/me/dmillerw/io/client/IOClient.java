package me.dmillerw.io.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.dmillerw.io.circuit.data.Port;
import me.dmillerw.io.lib.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = ModInfo.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class IOClient {

    @SubscribeEvent
    public static void setup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ClientEventHandler::onMouseScroll);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("io_tool_overlay", new IGuiOverlay() {
            @Override
            public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
                if (!gui.getMinecraft().options.hideGui && LocalGateInteractionHandler.currentlyHighlightingGate) {
                    gui.setupOverlayRenderState(true, false);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    int space = gui.getFont().lineHeight + 5;

                    if (LocalGateInteractionHandler.currentOutputPort == null) {
                        gui.getFont().draw(poseStack, "OUTPUT", 5, 5, 0xFFFFFFFF);

                        final int[] idx = {1};
                        LocalGateInteractionHandler.currentOutputs.forEach(p -> {
                            gui.getFont().draw(poseStack, getPortLabel(p), 5, 5 + space * idx[0], (idx[0] - 1) == LocalGateInteractionHandler.currentPortIndex ? 0xFF0000FF : 0xFFFFFFFF);
                            idx[0]++;
                        });
                    } else if (LocalGateInteractionHandler.currentInputPort == null) {
                        gui.getFont().draw(poseStack, "INPUT", 5, 5, 0xFFFFFFFF);

                        final int[] idx = {1};
                        LocalGateInteractionHandler.currentInputs.forEach(p -> {
                            gui.getFont().draw(poseStack, getPortLabel(p), 5, 5 + space * idx[0], (idx[0] - 1) == LocalGateInteractionHandler.currentPortIndex ? 0xFF0000FF : 0xFFFFFFFF);
                            idx[0]++;
                        });
                    }

                    RenderSystem.disableBlend();
                }
            }
        });
    }

    private static String getPortLabel(Port port) {
        return port.getName() + " | " + port.getType() + " | " + port.getValue().toString();
    }
}
