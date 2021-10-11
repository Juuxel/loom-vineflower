package juuxel.loomquiltflower.impl;

import net.fabricmc.stitch.util.StitchUtil;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class Zips {
    static byte[] getBytes(String outerPath, @Nullable String innerPath) throws IOException {
        if (innerPath == null) {
            return Files.readAllBytes(Path.of(outerPath));
        }

        try (StitchUtil.FileSystemDelegate fs = StitchUtil.getJarFileSystem(new File(outerPath), false)) {
            return Files.readAllBytes(fs.get().getPath(innerPath));
        }
    }
}
