package juuxel.loomquiltflower.impl.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class RcZipFileSystem implements Closeable {
    private static final Map<FileSystem, Integer> references = new HashMap<>();
    private final FileSystem fs;

    private RcZipFileSystem(FileSystem fs) {
        this.fs = fs;
    }

    public static RcZipFileSystem open(Path zip) throws IOException {
        var uri = URI.create("jar:" + zip.toUri());

        synchronized (references) {
            FileSystem fs;
            try {
                fs = FileSystems.getFileSystem(uri);
            } catch (FileSystemNotFoundException e) {
                fs = FileSystems.newFileSystem(uri, Map.of());
            }

            var refCount = references.getOrDefault(fs, 0);
            references.put(fs, refCount + 1);

            return new RcZipFileSystem(fs);
        }
    }

    public FileSystem get() {
        return fs;
    }

    @Override
    public void close() throws IOException {
        synchronized (references) {
            int refCount = references.get(fs);

            if (refCount <= 1) {
                references.remove(fs);
                fs.close();
            } else {
                references.put(fs, refCount - 1);
            }
        }
    }
}
