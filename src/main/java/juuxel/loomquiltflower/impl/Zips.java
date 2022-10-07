package juuxel.loomquiltflower.impl;

import net.fabricmc.tinyremapper.FileSystemReference;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Zips {
    public static byte[] getBytes(String outerPath, @Nullable String innerPath) throws IOException {
        if (innerPath == null) {
            return Files.readAllBytes(Path.of(outerPath));
        }

        try (var fs = FileSystemReference.openJar(Path.of(outerPath), false)) {
            return Files.readAllBytes(fs.getPath(innerPath));
        }
    }
}
