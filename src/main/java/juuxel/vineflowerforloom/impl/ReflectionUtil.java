package juuxel.vineflowerforloom.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public final class ReflectionUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getFieldOrRecordComponent(Object o, String name) {
        Class<?> c = o.getClass();

        try {
            try {
                Method accessor = c.getMethod(name);
                return (T) accessor.invoke(o);
            } catch (NoSuchMethodException e) {
                Field field = c.getField(name);
                return (T) field.get(o);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not find property " + name + " in " + c, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> maybeGetFieldOrRecordComponent(Object o, String name) {
        Class<?> c = o.getClass();

        try {
            try {
                Method accessor = c.getMethod(name);
                return Optional.ofNullable((T) accessor.invoke(o));
            } catch (NoSuchMethodException e) {
                try {
                    Field field = c.getField(name);
                    return Optional.ofNullable((T) field.get(o));
                } catch (NoSuchFieldException f) {
                    return Optional.empty();
                }
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

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> maybeInvokeNoArgsMethod(Object o, String name) {
        try {
            return Optional.ofNullable((T) o.getClass().getMethod(name).invoke(o));
        } catch (ReflectiveOperationException e) {
            return Optional.empty();
        }
    }
}
