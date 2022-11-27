package me.dmillerw.io.circuit.data;

import com.mojang.math.Vector3d;
import io.netty.buffer.ByteBuf;
import me.dmillerw.io.IO;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;

/**
 * @author dmillerw
 */
public enum DataType {

    NULL(Value.class, NullValue.NULL),
    NUMBER(Number.class, 0.0D),
    STRING(String.class, ""),
    VECTOR(Vector3d.class, new Vector3d(0, 0, 0)),
    ENTITY(int.class, 0);

    public final Class<?> type;
    public final Object zero;

    private <T> DataType(Class<T> type, T zero) {
        this.type = type;
        this.zero = zero;
    }

    public Value zero() {
        return Value.of(this, this.zero);
    }

    public boolean isValid(Object value) {
        if (value == null)
            return false;

        Class<?> clazz = value.getClass();

        switch (this) {
            case NUMBER: {
                return clazz == int.class || clazz == float.class || clazz == double.class ||
                        clazz == Integer.class || clazz == Float.class || clazz == Double.class ||
                        clazz == Number.class;
            }
            case STRING:
                return value instanceof String;
            case VECTOR:
                return value instanceof Vector3d;
            case ENTITY:
                return clazz == int.class || clazz == Integer.class;
            default:
                return false;
        }
    }

    public static Tag getNbtTagFromValue(DataType dataType, Value value) {
        switch (dataType) {
            case NUMBER:
                return DoubleTag.valueOf(value.getNumber().doubleValue());
            case STRING:
                return StringTag.valueOf(value.getString());
            case VECTOR: {
                CompoundTag tag = new CompoundTag();
                tag.putDouble("X", value.getVector().x);
                tag.putDouble("Y", value.getVector().y);
                tag.putDouble("Z", value.getVector().z);
                return tag;
            }
            case ENTITY: {
                return IntTag.valueOf(value.getEntity());
            }
            case NULL:
                return ByteTag.valueOf((byte) 0);
            default: return null;
        }
    }

    public static Value getValueFromNbtTag(DataType dataType, Tag value) {
        switch (dataType) {
            case NUMBER:
                return Value.of(dataType, ((DoubleTag) value).getAsDouble());
            case STRING:
                return Value.of(dataType, ((StringTag) value).getAsString());
            case VECTOR: {
                CompoundTag tag = (CompoundTag) value;
                return Value.of(dataType, new Vector3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z")));
            }
            case ENTITY: {
                return Value.of(dataType, ((IntTag) value).getAsInt());
            }
            case NULL:
                return NullValue.NULL;
            default: return null;
        }
    }

    public static void writeValueToByteBuf(DataType dataType, Value value, FriendlyByteBuf buf) {
        IO.LOGGER.info("Writing " + dataType + " from buffer");
        switch (dataType) {
            case NUMBER:
                buf.writeDouble((double)value.getNumber().doubleValue());
            case STRING:
                buf.writeUtf(value.getString(), 8192);
            case VECTOR: {
                Vector3d vec = value.getVector();
                buf.writeDouble(vec.x);
                buf.writeDouble(vec.y);
                buf.writeDouble(vec.z);
            }
            case ENTITY: {
                buf.writeInt(value.getEntity());
            }
            case NULL:
                buf.writeBoolean(false);
            default: return;
        }
    }

    public static Value readValueFromByteBuf(DataType dataType, FriendlyByteBuf buf) {
        IO.LOGGER.info("Reading " + dataType + " from buffer");
        switch (dataType) {
            case NUMBER:
                return Value.of(dataType, buf.readDouble());
            case STRING:
                return Value.of(dataType, buf.readUtf(8192));
            case VECTOR:
                return Value.of(dataType, new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
            case ENTITY:
                return Value.of(dataType, buf.readInt());
            case NULL:
                buf.readBoolean();
                return NullValue.NULL;
            default: return null;
        }
    }
}