package juuxel.loomquiltflower.impl.modern;

import juuxel.loomquiltflower.impl.ReflectionUtil;
import juuxel.loomquiltflower.impl.Zips;
import juuxel.loomquiltflower.impl.bridge.QfResultSaver;
import juuxel.loomquiltflower.impl.bridge.QfThreadIdLogger;
import juuxel.loomquiltflower.impl.bridge.QfTinyJavadocProvider;
import juuxel.loomquiltflower.impl.relocated.quiltflower.main.Fernflower;
import net.fabricmc.fernflower.api.IFabricJavadocProvider;
import net.fabricmc.loom.api.decompilers.DecompilationMetadata;
import net.fabricmc.loom.api.decompilers.LoomDecompiler;
import org.jetbrains.java.decompiler.main.extern.IFernflowerPreferences;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class QuiltflowerDecompiler implements LoomDecompiler {
    @Override
    public String name() {
        return "Quiltflower";
    }

    @Override
    public void decompile(Path compiledJar, Path sourcesDestination, Path linemapDestination, DecompilationMetadata metaData) {
        Map<String, Object> options = new HashMap<>();
        options.put(IFernflowerPreferences.INDENT_STRING, "\t");
        // FIXME: configureOptions(options);
        options.put(IFernflowerPreferences.DECOMPILE_GENERIC_SIGNATURES, "1");
        options.put(IFernflowerPreferences.BYTECODE_SOURCE_MAPPING, "1");
        options.put(IFernflowerPreferences.REMOVE_SYNTHETIC, "1");
        options.put(IFernflowerPreferences.LOG_LEVEL, "trace");
        options.put(IFernflowerPreferences.THREADS, ReflectionUtil.getFieldOrRecordComponent(metaData, "numberOfThreads"));
        options.put(IFabricJavadocProvider.PROPERTY_NAME, new QfTinyJavadocProvider(metaData.javaDocs.toFile()));

        Fernflower ff = new Fernflower(Zips::getBytes, new QfResultSaver(sourcesDestination::toFile, linemapDestination::toFile), options, new QfThreadIdLogger());

        for (Path library : metaData.libraries) {
            ff.addLibrary(library.toFile());
        }

        ff.addSource(compiledJar.toFile());
        ff.decompileContext();
    }
}
