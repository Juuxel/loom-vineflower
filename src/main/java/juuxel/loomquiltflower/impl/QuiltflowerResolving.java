package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.impl.module.LqfModule;
import juuxel.loomquiltflower.impl.task.ResolveQuiltflower;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.RegularFile;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;

import java.io.File;
import java.util.regex.Pattern;

public final class QuiltflowerResolving {
    public static final String TASK_NAME = "resolveQuiltflower";
    private static final Pattern DECOMPILE_TASK_NAME_REGEX = Pattern.compile("^gen.?SourcesWithQuiltflower$");

    public static File getQuiltflowerJar(Project project) {
        return getResolveQuiltflowerTask(project).get().getRemappedOutput().get().getAsFile();
    }

    public static TaskProvider<ResolveQuiltflower> getResolveQuiltflowerTask(Project project) {
        return project.getTasks().named(TASK_NAME, ResolveQuiltflower.class);
    }

    private static Provider<RegularFile> getQuiltflowerJarPath(Project project, QuiltflowerExtensionImpl extension, String suffix) {
        return project.getLayout().file(project.provider(() -> {
            extension.getSource().finalizeValue();
            String version = extension.getSource().get().getResolvedVersion();
            String fileName = String.format("quiltflower-%s%s.jar", suffix, version != null ? "-" + version : "");
            return extension.getCache().resolve(fileName).toFile();
        }));
    }

    public static void setup(Project project, QuiltflowerExtensionImpl extension) {
        var resolveQuiltflower = project.getTasks().register(TASK_NAME, ResolveQuiltflower.class, task -> {
            task.getSource().set(extension.getSource());
            task.getUnprocessedOutput().set(getQuiltflowerJarPath(project, extension, "unprocessed"));
            task.getRemappedOutput().set(getQuiltflowerJarPath(project, extension, "remapped"));
        });

        project.afterEvaluate(p -> {
            LqfModule module = extension.getActiveModule();

            if (module.shouldGenSourcesDependOnResolving()) {
                p.getTasks().configureEach(task -> {
                    var taskClass = module.getDecompileTaskClass();

                    if (taskClass.isInstance(task) && DECOMPILE_TASK_NAME_REGEX.matcher(task.getName()).matches()) {
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
                ConfigurableFileCollection qfFiles = project.files();
                qfFiles.builtBy(resolveQuiltflower);
                qfFiles.from(resolveQuiltflower.flatMap(ResolveQuiltflower::getUnprocessedOutput));
                project.getDependencies().add(configuration.getName(), qfFiles);
            }
        });
    }
}
