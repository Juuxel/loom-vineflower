package juuxel.loomquiltflower.impl.modern.architectury;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.impl.QuiltflowerResolving;
import juuxel.loomquiltflower.impl.legacy.AbstractFernFlowerDecompiler;
import juuxel.loomquiltflower.impl.legacy.AbstractForkedFFExecutor;
import juuxel.loomquiltflower.impl.legacy.QuiltflowerExecutor;
import net.fabricmc.loom.api.decompilers.architectury.ArchitecturyLoomDecompiler;
import net.fabricmc.loom.task.GenerateSourcesTask;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.logging.Logger;
import org.gradle.process.ExecResult;
import org.gradle.process.JavaExecSpec;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class QuiltflowerArchDecompiler extends AbstractFernFlowerDecompiler implements ArchitecturyLoomDecompiler {
    private String pluginId;
    private final QuiltflowerExtension extension;

    @Inject
    public QuiltflowerArchDecompiler(Project project, String pluginId, QuiltflowerExtension extension) {
        super(project);
        this.pluginId = pluginId;
        this.extension = extension;
    }

    @Override
    public String name() {
        return "Quiltflower";
    }

    @Override
    protected BiFunction<Project, Action<? super JavaExecSpec>, ExecResult> javaexec() {
        return ArchitecturyLoomDecompiler::javaexec;
    }

    @Override
    public Class<? extends AbstractForkedFFExecutor> fernFlowerExecutor() {
        return QuiltflowerExecutor.class;
    }

    @Override
    public void decompile(Logger logger, GenerateSourcesTask.DecompileParams params) {
        decompileInternal(toPath(params.getInputJar()), toPath(params.getSourcesDestinationJar()),
            toPath(params.getLinemap()), Runtime.getRuntime().availableProcessors(),
            toPath(params.getMappings()), params.getClassPath().getFiles().stream()
                .map(File::toPath)
                .collect(Collectors.toList()));
    }

    private static Path toPath(RegularFileProperty property) {
        return property.get().getAsFile().toPath();
    }

    @Override
    protected void configureJavaExec(JavaExecSpec spec) {
        File qf = QuiltflowerResolving.getQuiltflowerJar(project);
        spec.classpath(qf);
    }

    @Override
    protected void configureOptions(Map<String, Object> options) {
        options.putAll(extension.getPreferences().asMap().get());
    }
}
