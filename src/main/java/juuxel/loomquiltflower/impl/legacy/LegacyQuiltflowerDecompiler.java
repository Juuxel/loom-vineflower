package juuxel.loomquiltflower.impl.legacy;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.impl.QuiltflowerResolving;
import org.gradle.api.Project;
import org.gradle.process.JavaExecSpec;

import java.io.File;
import java.util.Map;

public final class LegacyQuiltflowerDecompiler extends AbstractFernFlowerDecompiler {
    private final Project project;
    private final QuiltflowerExtension extension;

    public LegacyQuiltflowerDecompiler(Project project) {
        super(project);
        this.project = project;
        this.extension = project.getExtensions().getByType(QuiltflowerExtension.class);
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
        File qf = QuiltflowerResolving.getQuiltflowerJar(project);
        spec.classpath(qf);
    }

    @Override
    protected void configureOptions(Map<String, Object> options) {
        options.putAll(extension.getPreferences().asMap().get());
    }
}
