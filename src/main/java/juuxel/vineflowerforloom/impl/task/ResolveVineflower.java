package juuxel.vineflowerforloom.impl.task;

import juuxel.loomquiltflower.core.Remapping;
import juuxel.vineflowerforloom.api.DecompilerSource;
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
import java.nio.file.StandardCopyOption;
import java.util.Collections;

public class ResolveVineflower extends DefaultTask {
    private static final String REFRESH_SYSTEM_PROPERTY = "vineflower.refresh";
    private static final String OLD_REFRESH_SYSTEM_PROPERTY = "loom-quiltflower.refresh";
    private final Property<DecompilerSource> source = getProject().getObjects().property(DecompilerSource.class);
    private final RegularFileProperty unprocessedOutput = getProject().getObjects().fileProperty();
    private final RegularFileProperty remappedOutput = getProject().getObjects().fileProperty();

    public ResolveVineflower() {
        getOutputs().upToDateWhen(t -> !(getProject().getGradle().getStartParameter().isRefreshDependencies()
            || Boolean.getBoolean(REFRESH_SYSTEM_PROPERTY)
            || Boolean.getBoolean(OLD_REFRESH_SYSTEM_PROPERTY)));
    }

    @Internal
    public Property<DecompilerSource> getSource() {
        return source;
    }

    @OutputFile
    public RegularFileProperty getUnprocessedOutput() {
        return unprocessedOutput;
    }

    @OutputFile
    public RegularFileProperty getRemappedOutput() {
        return remappedOutput;
    }

    @TaskAction
    public void resolve() throws IOException {
        DecompilerSource source = this.source.get();
        File unprocessedOutput = this.unprocessedOutput.get().getAsFile();

        try (InputStream in = source.open()) {
            Files.copy(in, unprocessedOutput.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new IOException("Source " + source + " could not resolve Vineflower", e);
        }

        File output = this.remappedOutput.get().getAsFile();
        Path outputPath = output.toPath();
        Path parent = outputPath.getParent();

        if (Files.notExists(parent)) {
            Files.createDirectory(parent);
        } else {
            Files.deleteIfExists(outputPath);
        }

        Remapping.remapQuiltflower(unprocessedOutput, output, Collections.emptySet());
    }
}
