package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.impl.util.RcZipFileSystem;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Zips {
    public static byte[] getBytes(String outerPath, @Nullable String innerPath) throws IOException {
        if (innerPath == null) {
            return Files.readAllBytes(Path.of(outerPath));
        }

        try (var fs = RcZipFileSystem.open(Path.of(outerPath))) {
            return Files.readAllBytes(fs.get().getPath(innerPath));
        }
    }
}
