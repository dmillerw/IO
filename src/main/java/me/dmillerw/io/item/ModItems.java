package me.dmillerw.io.item;

import me.dmillerw.io.block.ModBlocks;
import me.dmillerw.io.circuit.gates.GateRegistry;
import me.dmillerw.io.item.block.GateItem;
import me.dmillerw.io.lib.ModInfo;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModInfo.ID);

    public static final RegistryObject<Item> IO_TOOL = register("io_tool", IOToolItem::new);

    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return ITEMS.register(name, sup);
    }
}
