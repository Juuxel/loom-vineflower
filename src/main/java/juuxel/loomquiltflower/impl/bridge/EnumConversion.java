package juuxel.loomquiltflower.impl.bridge;

public class EnumConversion {
    public static <E extends Enum<E>, F extends Enum<F>> F convert(E from, Class<F> clazz) {
        return Enum.valueOf(clazz, from.name());
    }
}
