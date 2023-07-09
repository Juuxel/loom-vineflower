package juuxel.vineflowerforloom.impl.legacy;

import juuxel.vineflowerforloom.impl.VineflowerResolving;
import juuxel.vineflowerforloom.api.VineflowerExtension;
import org.gradle.api.Project;
import org.gradle.process.JavaExecSpec;

import java.io.File;
import java.util.Map;

public final class LegacyQuiltflowerDecompiler extends AbstractFernFlowerDecompiler {
    private final Project project;
    private final VineflowerExtension extension;

    public LegacyQuiltflowerDecompiler(Project project, VineflowerExtension extension) {
        super(project);
        this.project = project;
        this.extension = extension;
    }

    @Override
    public String name() {
        return "Quiltflower";
    }

    @Override
    public Class<? extends AbstractForkedFFExecutor> fernFlowerExecutor() {
        return QuiltflowerExecutor.class;
    }

    @Override
    protected void configureJavaExec(JavaExecSpec spec) {
        File qf = VineflowerResolving.getVineflowerJar(project);
        spec.classpath(qf);
    }

    @Override
    protected void configureOptions(Map<String, Object> options) {
        options.putAll(extension.getPreferences().asMap().get());
    }
}
