package me.dmillerw.io.network;

import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.network.packet.client.*;
import me.dmillerw.io.network.packet.server.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author dmillerw
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID);

    private static int clientDiscriminator = -1;
    private static int serverDiscriminator = 1;

    private static <REQ extends IMessage, REPLY extends IMessage> void registerServerMessage(Class<REQ> message, Class<? extends IMessageHandler<REQ, REPLY>> handler) {
        registerMessage(message, handler, Side.SERVER);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerClientMessage(Class<REQ> message, Class<? extends IMessageHandler<REQ, REPLY>> handler) {
        registerMessage(message, handler, Side.CLIENT);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> message, Class<? extends IMessageHandler<REQ, REPLY>> handler, Side side) {
        if (side == Side.SERVER) {
            INSTANCE.registerMessage(handler, message, clientDiscriminator, side);
            clientDiscriminator--;
        } else if (side == Side.CLIENT) {
            INSTANCE.registerMessage(handler, message, serverDiscriminator, side);
            serverDiscriminator++;
        }
    }

    static {
        registerServerMessage(SCreateConnection.class, SCreateConnection.Handler.class);
        registerServerMessage(SResetConnection.class, SResetConnection.Handler.class);
        registerServerMessage(SUpdateLinkingTool.class, SUpdateLinkingTool.Handler.class);
        registerServerMessage(SRequestOutputGui.class, SRequestOutputGui.Handler.class);
        registerServerMessage(SUpdateConfig.class, SUpdateConfig.Handler.class);

        registerClientMessage(CUpdatePorts.class, CUpdatePorts.Handler.class);
        registerClientMessage(CAddListener.class, CAddListener.Handler.class);
        registerClientMessage(CRemoveListener.class, CRemoveListener.Handler.class);
        registerClientMessage(COpenOutputGui.class, COpenOutputGui.Handler.class);
        registerClientMessage(CUpdateConfig.class, CUpdateConfig.Handler.class);
    }

    public static void sendToAllWatching(IMessage message, TileEntity tile) {
        sendToAllWatching(message, tile.getWorld(), tile.getPos());
    }

    public static void sendToAllWatching(IMessage message, World world, BlockPos pos) {
        if (world instanceof WorldServer) {
            PlayerChunkMap manager = ((WorldServer) world).getPlayerChunkMap();
            for (EntityPlayer player : world.playerEntities) {
                if (manager.isPlayerWatchingChunk((EntityPlayerMP) player, pos.getX() >> 4, pos.getZ() >> 4)) {
                    INSTANCE.sendTo(message, (EntityPlayerMP) player);
                }
            }
        }
    }
}
