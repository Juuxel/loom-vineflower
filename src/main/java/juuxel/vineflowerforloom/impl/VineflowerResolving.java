package juuxel.vineflowerforloom.impl;

import juuxel.vineflowerforloom.impl.module.VflModule;
import juuxel.vineflowerforloom.impl.task.ResolveVineflower;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.RegularFile;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;

import java.io.File;
import java.util.regex.Pattern;

public final class VineflowerResolving {
    public static final String TASK_NAME = "resolveVineflower";
    private static final Pattern DECOMPILE_TASK_NAME_REGEX = Pattern.compile("^gen.?SourcesWith(Quilt|Vine)flower$");

    public static File getVineflowerJar(Project project) {
        return getResolveVineflowerTask(project).get().getRemappedOutput().get().getAsFile();
    }

    public static TaskProvider<ResolveVineflower> getResolveVineflowerTask(Project project) {
        return project.getTasks().named(TASK_NAME, ResolveVineflower.class);
    }

    private static Provider<RegularFile> getVineflowerJarPath(Project project, VineflowerExtensionImpl extension, String suffix) {
        return project.getLayout().file(project.provider(() -> {
            extension.getToolSource().finalizeValue();
            String version = extension.getToolSource().get().getResolvedVersion();
            String fileName = String.format("vineflower-%s%s.jar", suffix, version != null ? "-" + version : "");
            return extension.getCache().resolve(fileName).toFile();
        }));
    }

    public static void setup(Project project, VineflowerExtensionImpl extension) {
        var resolveVineflower = project.getTasks().register(TASK_NAME, ResolveVineflower.class, task -> {
            task.getSource().set(extension.getToolSource());
            task.getUnprocessedOutput().set(getVineflowerJarPath(project, extension, "unprocessed"));
            task.getRemappedOutput().set(getVineflowerJarPath(project, extension, "remapped"));
        });

        project.afterEvaluate(p -> {
            VflModule module = extension.getActiveModule();

            if (module.shouldGenSourcesDependOnResolving()) {
                p.getTasks().configureEach(task -> {
                    var taskClass = module.getDecompileTaskClass();

                    if (taskClass.isInstance(task) && DECOMPILE_TASK_NAME_REGEX.matcher(task.getName()).matches()) {
                        task.dependsOn(resolveVineflower);
                    }
                });
            }

            extension.getAddToRuntimeClasspath().finalizeValue();

            if (extension.getAddToRuntimeClasspath().get()) {
                Configuration configuration = project.getConfigurations().create("vineflowerRuntime");
                configuration.setCanBeResolved(true);
                configuration.setCanBeConsumed(false);

                project.getConfigurations().getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(configuration);
                ConfigurableFileCollection decompilerFiles = project.files();
                decompilerFiles.builtBy(resolveVineflower);
                decompilerFiles.from(resolveVineflower.flatMap(ResolveVineflower::getUnprocessedOutput));
                project.getDependencies().add(configuration.getName(), decompilerFiles);
            }
        });
    }
}
