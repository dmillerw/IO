package me.dmillerw.io;

import me.dmillerw.io.block.ModBlocks;
import me.dmillerw.io.block.tile.ModTiles;
import me.dmillerw.io.circuit.gates.GateRegistry;
import me.dmillerw.io.item.ModItems;
import me.dmillerw.io.lib.ModInfo;
import me.dmillerw.io.network.PacketHandler;
import me.dmillerw.io.world.GateSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModInfo.ID)
public class IO {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(ModInfo.ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.STONE);
        }
    };

    public IO() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::onLevelTick);

        ModBlocks.registerGateBlocks();

        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModTiles.TILES.register(modBus);

        PacketHandler.initialize();
    }

    private void setup(FMLCommonSetupEvent event) {

    }

    private void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide)
            return;

        if (!event.haveTime())
            return;

        if (event.phase != TickEvent.Phase.END)
            return;

        ServerLevel level = (ServerLevel) event.level;
        GateSavedData gateSavedData = GateSavedData.get(level.getDataStorage());
        gateSavedData.runner.tick();
    }
}
