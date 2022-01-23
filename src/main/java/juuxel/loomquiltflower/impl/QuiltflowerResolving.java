package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.impl.module.LqfModule;
import juuxel.loomquiltflower.impl.task.ResolveQuiltflower;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.regex.Pattern;

public final class QuiltflowerResolving {
    public static final String TASK_NAME = "resolveQuiltflower";
    private static final Pattern DECOMPILE_TASK_NAME_REGEX = Pattern.compile("^gen(Common|ClientOnly)?SourcesWithQuiltflower$");

    public static File getQuiltflowerJar(Project project) {
        ResolveQuiltflower task = (ResolveQuiltflower) project.getTasks().getByName(TASK_NAME);
        return task.getOutput().get().getAsFile();
    }

    public static FileCollection getQuiltflowerJarFiles(Project project) {
        ResolveQuiltflower task = (ResolveQuiltflower) project.getTasks().getByName(TASK_NAME);
        return task.getOutputs().getFiles();
    }

    public static void setup(Project project, QuiltflowerExtensionImpl extension) {
        var resolveQuiltflower = project.getTasks().register(TASK_NAME, ResolveQuiltflower.class, task -> {
            task.getSource().set(extension.getSource());
            task.getOutput().set(project.getLayout().file(project.provider(() -> {
                extension.getSource().finalizeValue();
                String version = extension.getSource().get().getProvidedVersion();
                String fileName = String.format("quiltflower-remapped%s.jar", version != null ? "-" + version : "");
                return extension.getCache().resolve(fileName);
            }).map(Path::toFile)));
        });

        project.afterEvaluate(p -> {
            LqfModule module = extension.getActiveModule();

            if (module.shouldGenSourcesDependOnResolving()) {
                p.getTasks().configureEach(task -> {
                    var taskClass = module.getDecompileTaskClass();

                    if (taskClass.isInstance(task) && DECOMPILE_TASK_NAME_REGEX.matcher(task.getName()).matches()) {
                        // TODO: Add a test for 0.11 split jars
                        task.dependsOn(resolveQuiltflower);
                    }
                });
            }

            extension.getAddToRuntimeClasspath().finalizeValue();

            if (extension.getAddToRuntimeClasspath().get()) {
                Configuration configuration = project.getConfigurations().create("quiltflowerRuntime");
                configuration.setCanBeResolved(true);
                configuration.setCanBeConsumed(false);

                project.getConfigurations().getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(configuration);
                project.getDependencies().addProvider(configuration.getName(), resolveQuiltflower.map(task -> task.getOutputs().getFiles()));
            }
        });
    }
}
