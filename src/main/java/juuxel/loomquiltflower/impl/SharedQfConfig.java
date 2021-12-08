package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.impl.relocated.quiltflower.main.extern.IFernflowerPreferences;
import net.fabricmc.loom.api.decompilers.DecompilationMetadata;

import java.util.Map;

public final class SharedQfConfig {
    public static void configureCommonOptions(Map<String, ? super String> options, DecompilationMetadata metadata) {
        options.put(IFernflowerPreferences.DECOMPILE_GENERIC_SIGNATURES, "1");
        options.put(IFernflowerPreferences.BYTECODE_SOURCE_MAPPING, "1");
        options.put(IFernflowerPreferences.REMOVE_SYNTHETIC, "1");
        options.put(IFernflowerPreferences.LOG_LEVEL, "trace");
        options.put(IFernflowerPreferences.THREADS, Integer.toString(metadata.numberOfThreads()));
        options.putAll(ReflectionUtil.<Map<String, String>>maybeGetRecordComponent(metadata, "options").orElse(Map.of()));
    }
}
