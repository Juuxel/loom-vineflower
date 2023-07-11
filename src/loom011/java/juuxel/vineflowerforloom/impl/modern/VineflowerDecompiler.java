package juuxel.vineflowerforloom.impl.modern;

import juuxel.vineflowerforloom.impl.SharedDecompilerConfig;
import juuxel.vineflowerforloom.impl.Zips;
import juuxel.vineflowerforloom.impl.bridge.SimpleLogger;
import juuxel.vineflowerforloom.impl.bridge.VfResultSaver;
import juuxel.vineflowerforloom.impl.bridge.VfTinyJavadocProvider;
import juuxel.vineflowerforloom.impl.relocated.vineflower.main.Fernflower;
import juuxel.vineflowerforloom.impl.relocated.vineflower.main.extern.IFernflowerPreferences;
import juuxel.vineflowerforloom.impl.relocated.vineflowerapi.IFabricJavadocProvider;
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

public class VineflowerDecompiler implements LoomDecompiler {
    @Override
    public void decompile(Path compiledJar, Path sourcesDestination, Path linemapDestination, DecompilationMetadata metaData) {
        Map<String, Object> options = new HashMap<>();
        options.put(IFernflowerPreferences.INDENT_STRING, "\t");
        SharedDecompilerConfig.configureCommonOptions(options, metaData);
        options.put(IFabricJavadocProvider.PROPERTY_NAME, new VfTinyJavadocProvider(metaData.javaDocs().toFile()));

        PrintWriter logger = writerFromLoomLogger(metaData.logger());
        // Note: We use the deprecated API because this needs to work on VF <1.9.0.
        Fernflower ff = new Fernflower(Zips::getBytes, new VfResultSaver(sourcesDestination::toFile, linemapDestination::toFile), options, new SimpleLogger(logger));

        for (Path library : metaData.libraries()) {
            ff.addLibrary(library.toFile());
        }

        ff.addSource(compiledJar.toFile());

        try {
            ff.decompileContext();
        } finally {
            ff.clearContext();
        }

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

    @Deprecated
    public final static class DeprecatedQuiltflower extends VineflowerDecompiler {
        @Override
        public void decompile(Path compiledJar, Path sourcesDestination, Path linemapDestination, DecompilationMetadata metaData) {
            System.err.println("decompiler 'quiltflower' has been replaced with 'vineflower' and will be removed in a future release");
            super.decompile(compiledJar, sourcesDestination, linemapDestination, metaData);
        }
    }
}
