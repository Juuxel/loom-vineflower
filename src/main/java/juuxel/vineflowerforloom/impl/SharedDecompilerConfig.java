package juuxel.vineflowerforloom.impl;

import juuxel.vineflowerforloom.impl.relocated.vineflower.main.extern.IFernflowerPreferences;
import net.fabricmc.loom.api.decompilers.DecompilationMetadata;

import java.util.Map;

public final class SharedDecompilerConfig {
    public static void configureCommonOptions(Map<String, ? super String> options, DecompilationMetadata metadata) {
        options.put(IFernflowerPreferences.DECOMPILE_GENERIC_SIGNATURES, "1");
        options.put(IFernflowerPreferences.BYTECODE_SOURCE_MAPPING, "1");
        options.put(IFernflowerPreferences.REMOVE_SYNTHETIC, "1");
        options.put(IFernflowerPreferences.LOG_LEVEL, "trace");
        options.put(IFernflowerPreferences.THREADS, String.valueOf(ReflectionUtil.<Integer>getFieldOrRecordComponent(metadata, "numberOfThreads")));
        options.putAll(ReflectionUtil.<Map<String, String>>maybeGetFieldOrRecordComponent(metadata, "options").orElse(Map.of()));
    }
}
