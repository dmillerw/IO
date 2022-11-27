package me.dmillerw.io.block;

import me.dmillerw.io.circuit.gates.GateRegistry;
import me.dmillerw.io.item.ModItems;
import me.dmillerw.io.item.block.GateItem;
import me.dmillerw.io.lib.ModInfo;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModInfo.ID);
    public static final DeferredRegister<Item> ITEMS = ModItems.ITEMS;

    public static void registerGateBlocks() {
        for (GateRegistry.GateKey gate : GateRegistry.getGates()) {
            registerGate(gate);
        }
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup) {
        return register(name, sup, ModBlocks::item);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
        RegistryObject<T> ret = registerNoItem(name, sup);
        ITEMS.register(name, itemCreator.apply(ret));
        return ret;
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
        return BLOCKS.register(name, sup);
    }

    private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    }

    private static void registerGate(GateRegistry.GateKey key) {
        RegistryObject<Block> ret = registerNoItem(key.toString(), () -> new GateBlock(key));
        ITEMS.register(key.toString(), gateItem(ret, key));
    }

    private static Supplier<BlockItem> gateItem(final RegistryObject<? extends Block> block, GateRegistry.GateKey key) {
        return () -> new GateItem(block.get(), key);
    }
}
