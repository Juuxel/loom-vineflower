package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.impl.bridge.QfResultSaver;
import juuxel.loomquiltflower.impl.bridge.QfTinyJavadocProvider;
import juuxel.loomquiltflower.impl.bridge.QfThreadIdLogger;
import juuxel.loomquiltflower.impl.legacy.AbstractForkedFFExecutor;
import juuxel.loomquiltflower.impl.relocated.quiltflower.main.Fernflower;
import net.fabricmc.fernflower.api.IFabricJavadocProvider;

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
        ff.decompileContext();
    }
}
