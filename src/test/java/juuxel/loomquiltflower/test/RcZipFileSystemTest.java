package juuxel.loomquiltflower.test;

import juuxel.loomquiltflower.impl.util.RcZipFileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RcZipFileSystemTest {
    private static final String ZIP_FILE = "test.zip";
    private static final String TEXT_FILE = "test.txt";
    private static final String TEXT_FILE_CONTENTS = "tiny potato";

    @TempDir
    Path zipDir;

    @BeforeEach
    void setup() throws IOException {
        try (var fs = FileSystems.newFileSystem(URI.create("jar:" + zipDir.resolve(ZIP_FILE).toUri()), Map.of("create", true))) {
            Files.writeString(fs.getPath(TEXT_FILE), "tiny potato", StandardCharsets.UTF_8);
        }
    }

    @Test
    void basicFunctionality() throws IOException {
        assertThrows(
            FileSystemNotFoundException.class,
            () -> FileSystems.getFileSystem(URI.create("jar:" + zipDir.resolve(ZIP_FILE).toUri())),
            "file system should be closed"
        );

        try (var fs = RcZipFileSystem.open(zipDir.resolve(ZIP_FILE))) {
            assertEquals(TEXT_FILE_CONTENTS, Files.readString(fs.get().getPath(TEXT_FILE), StandardCharsets.UTF_8));
        }
    }

    // A opens -> B opens -> A closes -> B closes
    @Test
    void abClosing() throws IOException {
        var zip = zipDir.resolve(ZIP_FILE);
        var refA = RcZipFileSystem.open(zip);
        var refB = RcZipFileSystem.open(zip);
        try {
            refA.close();
            assertEquals(refA.get(), refB.get());
            assertTrue(refB.get().isOpen(), "refB must be open");
        } finally {
            refB.close();
        }
    }
}
