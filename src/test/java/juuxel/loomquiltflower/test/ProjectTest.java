package juuxel.loomquiltflower.test;

import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

abstract class ProjectTest {
    @TempDir
    protected File projectDirectory;

    protected Map<String, Map<String, String>> getReplacements() {
        return Map.of("build.gradle", Map.of("LOOM_ID", System.getProperty("loomId", "fabric-loom")));
    }

    protected final void setupProject(String name, String... extraFiles) {
        try {
            copyProjectFile(name, "build.gradle");
            copyProjectFile(name, "settings.gradle");

            for (String file : extraFiles) {
                copyProjectFile(name, file);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not set up test for project " + name, e);
        }
    }

    protected final InputStream getProjectFile(String projectName, String file) {
        return ProjectTest.class.getResourceAsStream("/projects/" + projectName + '/' + file);
    }

    protected final String getProjectFileText(String projectName, String file) throws IOException {
        try (InputStream in = getProjectFile(projectName, file)) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void copyProjectFile(String projectName, String file) throws IOException {
        copyProjectFile(projectName, file, file);
    }

    private void copyProjectFile(String projectName, String from, String to) throws IOException {
        Path target = projectDirectory.toPath().resolve(to);
        Files.createDirectories(target.getParent());
        var replacements = getReplacements();

        if (replacements.containsKey(from)) {
            String text = getProjectFileText(projectName, from);

            for (var entry : replacements.get(text).entrySet()) {
                text = text.replace('@' + entry.getKey() + '@', entry.getValue());
            }

            Files.writeString(target, text);
        } else {
            try (InputStream in = getProjectFile(projectName, from)) {
                Files.copy(in, target);
            }
        }
    }
}
