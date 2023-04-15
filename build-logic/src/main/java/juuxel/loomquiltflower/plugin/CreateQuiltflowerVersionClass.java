package juuxel.loomquiltflower.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public abstract class CreateQuiltflowerVersionClass extends DefaultTask {
    @Input
    public abstract Property<String> getQuiltflowerVersion();

    @Input
    public abstract Property<String> getPackageName();

    @OutputDirectory
    public abstract DirectoryProperty getSourceDirectory();

    public CreateQuiltflowerVersionClass() {
        getQuiltflowerVersion().convention(
            getProject()
                .provider(() -> getProject().property("quiltflower-version"))
                .map(Objects::toString)
        );
    }

    @TaskAction
    public void run() throws IOException {
        String pkg = getPackageName().get();
        String source = """
            package %s;

            final class QuiltflowerVersion {
                static final String DEFAULT_VERSION = "%s";
            }
            """
            .formatted(pkg, getQuiltflowerVersion().get());

        Path directory = getSourceDirectory().get().getAsFile().toPath();
        Path sourceFile = directory.resolve(pkg.replace('.', File.separatorChar))
            .resolve("QuiltflowerVersion.java");
        Files.createDirectories(sourceFile.getParent());
        Files.writeString(sourceFile, source);
    }
}
