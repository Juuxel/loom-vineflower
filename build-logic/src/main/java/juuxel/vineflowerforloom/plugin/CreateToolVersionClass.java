package juuxel.vineflowerforloom.plugin;

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

public abstract class CreateToolVersionClass extends DefaultTask {
    @Input
    public abstract Property<String> getToolVersion();

    @Input
    public abstract Property<String> getPackageName();

    @Input
    public abstract Property<String> getClassName();

    @OutputDirectory
    public abstract DirectoryProperty getSourceDirectory();

    public CreateToolVersionClass() {
        getToolVersion().convention(
            getProject()
                .provider(() -> getProject().property("vineflower-version"))
                .map(Objects::toString)
        );
        getClassName().convention("VineflowerVersion");
    }

    @TaskAction
    public void run() throws IOException {
        String pkg = getPackageName().get();
        String className = getClassName().get();
        String source = """
            package %s;

            final class %s {
                static final String DEFAULT_VERSION = "%s";
            }
            """
            .formatted(pkg, className, getToolVersion().get());

        Path directory = getSourceDirectory().get().getAsFile().toPath();
        Path sourceFile = directory.resolve(pkg.replace('.', File.separatorChar))
            .resolve(className + ".java");
        Files.createDirectories(sourceFile.getParent());
        Files.writeString(sourceFile, source);
    }
}
