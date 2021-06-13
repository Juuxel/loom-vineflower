package juuxel.loomquiltflower;

import juuxel.loomquiltflower.bridge.QResultSaver;
import juuxel.loomquiltflower.bridge.ThreadIdQfLogger;
import juuxel.loomquiltflower.relocated.quiltflower.main.Fernflower;
import net.fabricmc.fernflower.api.IFabricJavadocProvider;
import net.fabricmc.loom.decompilers.fernflower.AbstractForkedFFExecutor;
import net.fabricmc.loom.decompilers.fernflower.FernFlowerUtils;
import net.fabricmc.loom.decompilers.fernflower.ThreadIDFFLogger;
import net.fabricmc.loom.decompilers.fernflower.ThreadSafeResultSaver;
import net.fabricmc.loom.decompilers.fernflower.TinyJavadocProvider;

import java.io.File;
import java.util.List;
import java.util.Map;

public class QuiltflowerExecutor extends AbstractForkedFFExecutor {
    public static void main(String[] args) {
        AbstractForkedFFExecutor.decompile(args, new QuiltflowerExecutor());
    }

    @Override
    public void runFF(Map<String, Object> options, List<File> libraries, File input, File output, File lineMap, File mappings) {
        options.put(IFabricJavadocProvider.PROPERTY_NAME, new TinyJavadocProvider(mappings));

        Fernflower ff = new Fernflower(FernFlowerUtils::getBytecode, new QResultSaver(new ThreadSafeResultSaver(() -> output, () -> lineMap)), options, new ThreadIdQfLogger(new ThreadIDFFLogger()));

        for (File library : libraries) {
            ff.addLibrary(library);
        }

        ff.addSource(input);
        ff.decompileContext();
    }
}
