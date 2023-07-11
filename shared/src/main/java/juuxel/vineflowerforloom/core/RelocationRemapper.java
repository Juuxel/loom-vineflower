package juuxel.vineflowerforloom.core;

import org.objectweb.asm.commons.Remapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class RelocationRemapper extends Remapper {
    private final Map<String, String> patterns;
    private final Set<String> excludes;

    public RelocationRemapper(Map<String, String> patterns, Set<String> excludes) {
        this.patterns = patterns;
        this.excludes = excludes;
    }

    public static RelocationRemapper createQuiltflowerRelocator() {
        Map<String, String> patterns = new HashMap<>();
        patterns.put("org/jetbrains/java/decompiler", "juuxel/vineflowerforloom/impl/relocated/vineflower");
        patterns.put("net/fabricmc/fernflower/api", "juuxel/vineflowerforloom/impl/relocated/vineflowerapi");
        Set<String> excludes = Collections.emptySet();

        return new RelocationRemapper(patterns, excludes);
    }

    @Override
    public String map(String internalName) {
        if (!excludes.contains(internalName)) {
            for (Map.Entry<String, String> entry : patterns.entrySet()) {
                String from = entry.getKey();
                String to = entry.getValue();

                if (internalName.startsWith(from + '/')) {
                    return to + internalName.substring(from.length());
                }
            }
        }

        return super.map(internalName);
    }
}
