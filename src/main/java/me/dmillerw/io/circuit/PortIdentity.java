package me.dmillerw.io.circuit;

import net.minecraft.nbt.CompoundTag;
import org.checkerframework.checker.units.qual.C;

import java.util.Objects;

public class PortIdentity {

    public static PortIdentity fromNbt(CompoundTag tag) {
        return new PortIdentity(tag.getLong("position"), tag.getString("key"));
    }

    public final long position;
    public final String key;

    public PortIdentity(long position, String key) {
        this.position = position;
        this.key = key;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("position", this.position);
        tag.putString("key", this.key);
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortIdentity that = (PortIdentity) o;
        return position == that.position && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, key);
    }
}
