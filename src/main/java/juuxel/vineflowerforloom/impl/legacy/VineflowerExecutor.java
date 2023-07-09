package juuxel.vineflowerforloom.impl.legacy;

import juuxel.vineflowerforloom.impl.Zips;
import juuxel.vineflowerforloom.impl.bridge.VfResultSaver;
import juuxel.vineflowerforloom.impl.bridge.VfTinyJavadocProvider;
import juuxel.vineflowerforloom.impl.bridge.VfThreadIdLogger;
import juuxel.loomquiltflower.impl.relocated.quiltflower.main.Fernflower;
import juuxel.loomquiltflower.impl.relocated.quiltflowerapi.IFabricJavadocProvider;

import java.io.File;
import java.util.List;
import java.util.Map;

public class VineflowerExecutor extends AbstractForkedFFExecutor {
    public static void main(String[] args) {
        AbstractForkedFFExecutor.decompile(args, new VineflowerExecutor());
    }

    @Override
    public void runFF(Map<String, Object> options, List<File> libraries, File input, File output, File lineMap, File mappings) {
        options.put(IFabricJavadocProvider.PROPERTY_NAME, new VfTinyJavadocProvider(mappings));

        Fernflower ff = new Fernflower(Zips::getBytes, new VfResultSaver(() -> output, () -> lineMap), options, new VfThreadIdLogger());

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
