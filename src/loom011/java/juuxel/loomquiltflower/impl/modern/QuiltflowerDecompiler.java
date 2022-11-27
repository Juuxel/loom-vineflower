package juuxel.loomquiltflower.impl.modern;

import juuxel.loomquiltflower.impl.SharedQfConfig;
import juuxel.loomquiltflower.impl.Zips;
import juuxel.loomquiltflower.impl.bridge.SimpleLogger;
import juuxel.loomquiltflower.impl.bridge.QfResultSaver;
import juuxel.loomquiltflower.impl.bridge.QfTinyJavadocProvider;
import juuxel.loomquiltflower.impl.relocated.quiltflower.main.Fernflower;
import juuxel.loomquiltflower.impl.relocated.quiltflower.main.extern.IFernflowerPreferences;
import juuxel.loomquiltflower.impl.relocated.quiltflowerapi.IFabricJavadocProvider;
import net.fabricmc.loom.api.decompilers.DecompilationMetadata;
import net.fabricmc.loom.api.decompilers.LoomDecompiler;
import net.fabricmc.loom.util.IOStringConsumer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class QuiltflowerDecompiler implements LoomDecompiler {
    @Override
    public void decompile(Path compiledJar, Path sourcesDestination, Path linemapDestination, DecompilationMetadata metaData) {
        Map<String, Object> options = new HashMap<>();
        options.put(IFernflowerPreferences.INDENT_STRING, "\t");
        SharedQfConfig.configureCommonOptions(options, metaData);
        options.put(IFabricJavadocProvider.PROPERTY_NAME, new QfTinyJavadocProvider(metaData.javaDocs().toFile()));

        PrintWriter logger = writerFromLoomLogger(metaData.logger());
        Fernflower ff = new Fernflower(Zips::getBytes, new QfResultSaver(sourcesDestination::toFile, linemapDestination::toFile), options, new SimpleLogger(logger));

        for (Path library : metaData.libraries()) {
            ff.addLibrary(library.toFile());
        }

        ff.addSource(compiledJar.toFile());
        ff.decompileContext();
        closeFs(compiledJar);
    }

    private void closeFs(Path compiledJar) {
        try {
            FileSystems.getFileSystem(URI.create("jar:" + compiledJar.toUri())).close();
        } catch (FileSystemNotFoundException e) {
            // Ignore
        } catch (IOException e) {
            throw new UncheckedIOException("Could not close file system " + compiledJar.toAbsolutePath(), e);
        }
    }

    private static PrintWriter writerFromLoomLogger(IOStringConsumer logger) {
        Writer parent = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                logger.accept(new String(cbuf, off, len));
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };

        return new PrintWriter(parent);
    }
}
