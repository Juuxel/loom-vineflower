package juuxel.loomquiltflower;

import juuxel.loomquiltflower.bridge.QfResultSaver;
import juuxel.loomquiltflower.bridge.QfTinyJavadocProvider;
import juuxel.loomquiltflower.bridge.QfThreadIdLogger;
import juuxel.loomquiltflower.relocated.quiltflower.main.Fernflower;
import net.fabricmc.fernflower.api.IFabricJavadocProvider;
import net.fabricmc.loom.decompilers.fernflower.AbstractForkedFFExecutor;
import net.fabricmc.loom.decompilers.fernflower.FernFlowerUtils;
import net.fabricmc.loom.decompilers.fernflower.ThreadIDFFLogger;
import net.fabricmc.loom.decompilers.fernflower.ThreadSafeResultSaver;

import java.io.File;
import java.util.List;
import java.util.Map;

public class QuiltflowerExecutor extends AbstractForkedFFExecutor {
    public static void main(String[] args) {
        AbstractForkedFFExecutor.decompile(args, new QuiltflowerExecutor());
    }

    @Override
    public void runFF(Map<String, Object> options, List<File> libraries, File input, File output, File lineMap, File mappings) {
        options.put(IFabricJavadocProvider.PROPERTY_NAME, new QfTinyJavadocProvider(mappings));

        Fernflower ff = new Fernflower(FernFlowerUtils::getBytecode, new QfResultSaver(new ThreadSafeResultSaver(() -> output, () -> lineMap)), options, new QfThreadIdLogger(new ThreadIDFFLogger()));

        for (File library : libraries) {
            ff.addLibrary(library);
        }

        ff.addSource(input);
        ff.decompileContext();
    }
}
