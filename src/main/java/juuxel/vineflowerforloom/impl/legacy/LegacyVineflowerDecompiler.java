package juuxel.vineflowerforloom.impl.legacy;

import juuxel.vineflowerforloom.impl.VineflowerResolving;
import juuxel.vineflowerforloom.api.VineflowerExtension;
import org.gradle.api.Project;
import org.gradle.process.JavaExecSpec;

import java.io.File;
import java.util.Map;

public final class LegacyVineflowerDecompiler extends AbstractFernFlowerDecompiler {
    private final Project project;
    private final String name;
    private final VineflowerExtension extension;

    public LegacyVineflowerDecompiler(Project project, String name, VineflowerExtension extension) {
        super(project);
        this.project = project;
        this.name = name;
        this.extension = extension;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<? extends AbstractForkedFFExecutor> fernFlowerExecutor() {
        return VineflowerExecutor.class;
    }

    @Override
    protected void configureJavaExec(JavaExecSpec spec) {
        File decompilerJar = VineflowerResolving.getVineflowerJar(project);
        spec.classpath(decompilerJar);
    }

    @Override
    protected void configureOptions(Map<String, Object> options) {
        options.putAll(extension.getPreferences().asMap().get());
    }
}
