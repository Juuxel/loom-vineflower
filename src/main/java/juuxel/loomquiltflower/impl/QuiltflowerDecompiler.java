package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.api.LoomQuiltflowerExtension;
import juuxel.loomquiltflower.core.Remapping;
import juuxel.loomquiltflower.impl.loom.AbstractFernFlowerDecompiler;
import net.fabricmc.loom.decompilers.fernflower.AbstractForkedFFExecutor;
import org.gradle.api.Project;
import org.gradle.process.JavaExecSpec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public final class QuiltflowerDecompiler extends AbstractFernFlowerDecompiler {
    private final Project project;
    private final LoomQuiltflowerExtension extension;

    public QuiltflowerDecompiler(Project project, LoomQuiltflowerExtension extension) {
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
        try {
            File qf = resolveQuiltflower(project, extension);
            spec.classpath(qf);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not resolve Quiltflower", e);
        }
    }

    private static File resolveQuiltflower(Project project, LoomQuiltflowerExtension extension) throws IOException {
        Path cache = project.getRootProject().getProjectDir().toPath().resolve(".gradle").resolve("loom-quiltflower-cache");

        if (Files.notExists(cache)) {
            Files.createDirectories(cache);
        }

        boolean refresh = project.getGradle().getStartParameter().isRefreshDependencies() || Boolean.getBoolean("loom-quiltflower.refresh");
        String version = extension.getQuiltflowerVersion().get();
        Path qfJar = cache.resolve("quiltflower-" + version + "-remapped.jar");

        if (Files.notExists(qfJar) || refresh) {
            Path baseQfJar = cache.resolve("quiltflower-" + version + ".jar");

            if (Files.notExists(baseQfJar) || refresh) {
                URL url = new URL(String.format("https://maven.quiltmc.org/repository/release/org/quiltmc/quiltflower/%s/quiltflower-%s.jar", version, version));

                try (InputStream in = url.openStream()) {
                    Files.copy(in, baseQfJar);
                }
            }

            Remapping.remapQuiltflower(baseQfJar.toFile(), qfJar.toFile(), Collections.emptySet());
        }

        return qfJar.toFile();
    }
}
