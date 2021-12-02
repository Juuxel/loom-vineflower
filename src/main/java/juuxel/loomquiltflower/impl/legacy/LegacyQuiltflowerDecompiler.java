package juuxel.loomquiltflower.impl.legacy;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.impl.QuiltflowerResolving;
import juuxel.loomquiltflower.impl.ReflectionUtil;
import net.fabricmc.loom.api.decompilers.DecompilationMetadata;
import net.fabricmc.loom.api.decompilers.LoomDecompiler;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.process.ExecResult;
import org.gradle.process.JavaExecSpec;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiFunction;

public final class LegacyQuiltflowerDecompiler extends AbstractFernFlowerDecompiler implements LoomDecompiler {
    private final QuiltflowerExtension extension;

    public LegacyQuiltflowerDecompiler(Project project, QuiltflowerExtension extension) {
        super(project);
        this.extension = extension;
    }

    @Override
    public String name() {
        return "Quiltflower";
    }

    @Override
    public void decompile(Path compiledJar, Path sourcesDestination, Path linemapDestination, DecompilationMetadata metaData) {
        decompileInternal(compiledJar, sourcesDestination, linemapDestination, ReflectionUtil.getFieldOrRecordComponent(metaData, "numberOfThreads"),
            ReflectionUtil.getFieldOrRecordComponent(metaData, "javaDocs"), ReflectionUtil.getFieldOrRecordComponent(metaData, "libraries"));
    }

    @Override
    protected BiFunction<Project, Action<? super JavaExecSpec>, ExecResult> javaexec() {
        return ForkingJavaExec::javaexec;
    }

    @Override
    public Class<? extends AbstractForkedFFExecutor> fernFlowerExecutor() {
        return QuiltflowerExecutor.class;
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
