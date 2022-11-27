package juuxel.loomquiltflower.impl.legacy;

import juuxel.loomquiltflower.impl.Zips;
import juuxel.loomquiltflower.impl.bridge.QfResultSaver;
import juuxel.loomquiltflower.impl.bridge.QfTinyJavadocProvider;
import juuxel.loomquiltflower.impl.bridge.QfThreadIdLogger;
import juuxel.loomquiltflower.impl.relocated.quiltflower.main.Fernflower;
import juuxel.loomquiltflower.impl.relocated.quiltflowerapi.IFabricJavadocProvider;

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

        Fernflower ff = new Fernflower(Zips::getBytes, new QfResultSaver(() -> output, () -> lineMap), options, new QfThreadIdLogger());

        for (File library : libraries) {
            ff.addLibrary(library);
        }

        ff.addSource(input);

        try {
            ff.decompileContext();
        } finally {
            ff.clearContext();
        }
    }
}
