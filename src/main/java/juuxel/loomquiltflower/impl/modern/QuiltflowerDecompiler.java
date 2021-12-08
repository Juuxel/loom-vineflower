package juuxel.loomquiltflower.impl.modern;

import juuxel.loomquiltflower.impl.ReflectionUtil;
import juuxel.loomquiltflower.impl.SharedQfConfig;
import juuxel.loomquiltflower.impl.Zips;
import juuxel.loomquiltflower.impl.bridge.QfResultSaver;
import juuxel.loomquiltflower.impl.bridge.QfThreadIdLogger;
import juuxel.loomquiltflower.impl.bridge.QfTinyJavadocProvider;
import juuxel.loomquiltflower.impl.relocated.quiltflower.main.Fernflower;
import juuxel.loomquiltflower.impl.relocated.quiltflower.main.extern.IFernflowerPreferences;
import juuxel.loomquiltflower.impl.relocated.quiltflowerapi.IFabricJavadocProvider;
import net.fabricmc.loom.api.decompilers.DecompilationMetadata;
import net.fabricmc.loom.api.decompilers.LoomDecompiler;

import java.nio.file.Path;
import java.util.Collection;
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
        SharedQfConfig.configureCommonOptions(options, metaData);
        options.put(IFabricJavadocProvider.PROPERTY_NAME, new QfTinyJavadocProvider(ReflectionUtil.<Path>getFieldOrRecordComponent(metaData, "javaDocs").toFile()));

        Fernflower ff = new Fernflower(Zips::getBytes, new QfResultSaver(sourcesDestination::toFile, linemapDestination::toFile), options, new QfThreadIdLogger());

        for (Path library : ReflectionUtil.<Collection<Path>>getFieldOrRecordComponent(metaData, "libraries")) {
            ff.addLibrary(library.toFile());
        }

        ff.addSource(compiledJar.toFile());
        ff.decompileContext();
    }
}
