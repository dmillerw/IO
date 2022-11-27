package me.dmillerw.io.world;

import me.dmillerw.io.circuit.GateRunner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class GateSavedData extends SavedData {

    public static GateSavedData get(DimensionDataStorage dataStorage) {
        return dataStorage.computeIfAbsent(GateSavedData::loadFromTag, GateSavedData::create, "io:gates");
    }

    public static GateSavedData create() {
        return new GateSavedData();
    }

    public static GateSavedData loadFromTag(CompoundTag tag) {
        GateSavedData data = create();
        data.load(tag);
        return data;
    }

    public final GateRunner runner = new GateRunner();

    public void load(CompoundTag tag) {
        runner.loadFromNbt(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        runner.saveToNbt(tag);
        return tag;
    }
}
