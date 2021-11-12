package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.impl.task.ResolveQuiltflower;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlugin;

import java.io.File;

public final class QuiltflowerResolving {
    private static final String TASK_NAME = "resolveQuiltflower";

    public static File getQuiltflowerJar(Project project) {
        ResolveQuiltflower task = (ResolveQuiltflower) project.getTasks().getByName(TASK_NAME);
        return task.getOutput().get().getAsFile();
    }

    public static void setup(Project project, QuiltflowerExtensionImpl extension) {
        var resolveQuiltflower = project.getTasks().register(TASK_NAME, ResolveQuiltflower.class, task -> {
            task.getSource().set(extension.getSource());
            task.getOutput().set(project.getLayout().file(project.provider(() -> extension.getCache().resolve("quiltflower-remapped.jar").toFile())));
        });

        project.afterEvaluate(p -> {
            p.getTasks().named("genSourcesWithQuiltflower", task -> task.dependsOn(resolveQuiltflower));
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
