package me.dmillerw.io.circuit.data;

import com.mojang.math.Vector3d;
import io.netty.buffer.ByteBuf;
import me.dmillerw.io.IO;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

/**
 * @author dmillerw
 */
public class Port {

    public static Port create(String name, DataType type) {
        return new Port(name, type, type.zero());
    }

    public static Port fromNbt(CompoundTag tagCompound) {
        String name = tagCompound.getString("Name");
        DataType type = DataType.values()[tagCompound.getByte("Type")];
        Value value = DataType.getValueFromNbtTag(type, tagCompound.getCompound("Value"));
//        Value previousValue = DataType.getValueFromNbtTag(type, tagCompound.getCompound("PreviousValue"));

        Port port = new Port(name, type, value);
//        port.previousValue = previousValue;

        return port;
    }

    public static Port fromByteBuf(FriendlyByteBuf buf) {
        String name = buf.readUtf(256);
        DataType type = DataType.values()[buf.readInt()];
        Value value = DataType.readValueFromByteBuf(type, buf);
//        Value previousValue = DataType.readValueFromByteBuf(type, buf);

        Port port = new Port(name, type, value);
//        port.previousValue = previousValue;

//        Port port = new Port(name, type, NullValue.NULL);

        IO.LOGGER.info("Reading from buffer: " + port.toString());

        return port;
    }

    private final String name;
    private final DataType type;
    private Value value;
//    private Value previousValue = NullValue.NULL;
    private boolean isDirty = false;

    private Port(String name, DataType type, Value value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public void setValue(Value value) {
        if (!value.isNull())
            if (!value.isType(type))
                throw new IllegalArgumentException(value + " is not " + type.toString());

//        this.previousValue = this.value;
        this.value = value;
        this.isDirty = true;
    }

    public Value getValue() {
        return value;
    }

//    public Value getPreviousValue() {
//        return previousValue;
//    }

//    public boolean hasValueChanged() {
//        return !value.equals(previousValue);
//    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void markClean() {
        this.isDirty = false;
    }

    public void writeToTag(CompoundTag tagCompound) {
        tagCompound.putString("Name", name);
        tagCompound.putByte("Type", (byte) type.ordinal());
        tagCompound.put("Value", DataType.getNbtTagFromValue(type, value));
//        tagCompound.put("PreviousValue", DataType.getNbtTagFromValue(type, previousValue));
    }

    public void writeToByteBuf(FriendlyByteBuf buf) {
        IO.LOGGER.info("Writing to buffer: " + this.toString());
        buf.writeUtf(this.name, 256);
        buf.writeInt(type.ordinal());
        DataType.writeValueToByteBuf(type, value, buf);
//        DataType.writeValueToByteBuf(type, previousValue, buf);
    }

    // Passthrough methods to Value, for convenience
    public Number getNumber() {
        return value.getNumber();
    }

    public int getInt() {
        return getNumber().intValue();
    }

    public double getDouble() {
        return getNumber().doubleValue();
    }

    public String getString() {
        return value.getString();
    }

    public Vector3d getVector() {
        return value.getVector();
    }

    public int getEntity() {
        return value.getEntity();
    }

    @Override
    public String toString() {
        return "Port{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}