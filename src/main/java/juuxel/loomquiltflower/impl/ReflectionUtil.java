package juuxel.loomquiltflower.impl;

import java.lang.reflect.Method;
import java.util.Optional;

public final class ReflectionUtil {
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> maybeGetRecordComponent(Object o, String name) {
        Class<?> c = o.getClass();

        try {
            try {
                Method accessor = c.getMethod(name);
                return Optional.ofNullable((T) accessor.invoke(o));
            } catch (NoSuchMethodException e) {
                return Optional.empty();
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not find property " + name + " in " + c, e);
        }
    }

    public static boolean classExists(String fqn) {
        try {
            Class.forName(fqn);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
