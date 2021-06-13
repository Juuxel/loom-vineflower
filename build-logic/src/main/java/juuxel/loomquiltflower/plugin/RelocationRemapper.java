package juuxel.loomquiltflower.plugin;

import org.objectweb.asm.commons.Remapper;

import java.util.Map;
import java.util.Set;

final class RelocationRemapper extends Remapper {
    private final Map<String, String> patterns;
    private final Set<String> excludes;

    RelocationRemapper(Map<String, String> patterns, Set<String> excludes) {
        this.patterns = patterns;
        this.excludes = excludes;
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
