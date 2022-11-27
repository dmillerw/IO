package me.dmillerw.io.circuit.gates;

import com.google.common.collect.Maps;
import me.dmillerw.io.circuit.gates.impl.math.AdditionGate;
import me.dmillerw.io.circuit.gates.impl.util.ConstantValueGate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class GateRegistry {

    private static final Map<GateKey, Supplier<BaseGate>> registry = Maps.newHashMap();

    public static final GateKey MATH_ADDITION = register(Category.MATH, "add", AdditionGate::new);

    public static final GateKey UTIL_CONST = register(Category.UTIL, "const", ConstantValueGate::new);


    private static <G extends BaseGate> GateKey register(Category category, String key, Supplier<G> supplier) {
        GateKey gkey = new GateKey(category, key);
        registry.put(gkey, (Supplier<BaseGate>) supplier);
        return gkey;
    }

    public static Set<GateKey> getGates() {
        return registry.keySet();
    }

    public static BaseGate instantiate(GateKey key) {
        return registry.get(key).get();
    }

    public static class GateKey {

        public final Category category;
        public final String key;

        private GateKey(Category category, String key) {
            this.category = category;
            this.key = key;
        }

        @Override
        public String toString() {
//            return category.name() + "_" + key;
            return "gate_" + key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GateKey gateKey = (GateKey) o;
            return category == gateKey.category && Objects.equals(key, gateKey.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(category, key);
        }
    }
}
