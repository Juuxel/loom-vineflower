package juuxel.loomquiltflower.impl.task;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.core.Remapping;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class ResolveQuiltflower extends DefaultTask {
    private final Property<QuiltflowerSource> source = getProject().getObjects().property(QuiltflowerSource.class);
    private final RegularFileProperty output = getProject().getObjects().fileProperty();

    public ResolveQuiltflower() {
        getOutputs().upToDateWhen(t -> !(getProject().getGradle().getStartParameter().isRefreshDependencies() || Boolean.getBoolean("loom-quiltflower.refresh")));
    }

    @Internal
    public Property<QuiltflowerSource> getSource() {
        return source;
    }

    @OutputFile
    public RegularFileProperty getOutput() {
        return output;
    }

    @TaskAction
    public void resolve() throws IOException {
        QuiltflowerSource source = this.source.get();
        Path tempDir = Files.createTempDirectory("quiltflower");
        Path baseQfJar = tempDir.resolve("quiltflower-unprocessed.jar");

        try (InputStream in = source.open()) {
            Files.copy(in, baseQfJar);
        } catch (Exception e) {
            throw new IOException("Source " + source + " could not resolve Quiltflower", e);
        }

        File output = this.output.get().getAsFile();
        Path outputPath = output.toPath();
        Path parent = outputPath.getParent();

        if (Files.notExists(parent)) {
            Files.createDirectory(parent);
        } else {
            Files.deleteIfExists(outputPath);
        }

        Remapping.remapQuiltflower(baseQfJar.toFile(), output, Collections.emptySet());
    }
}
