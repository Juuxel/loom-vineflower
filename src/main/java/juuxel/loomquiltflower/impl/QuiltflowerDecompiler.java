package juuxel.loomquiltflower.impl;

import com.google.common.hash.Hashing;
import com.google.common.io.MoreFiles;
import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.core.Remapping;
import juuxel.loomquiltflower.impl.loom.AbstractFernFlowerDecompiler;
import net.fabricmc.loom.decompilers.fernflower.AbstractForkedFFExecutor;
import org.gradle.api.Project;
import org.gradle.process.JavaExecSpec;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public final class QuiltflowerDecompiler extends AbstractFernFlowerDecompiler {
    private final Project project;
    private final QuiltflowerExtension extension;

    public QuiltflowerDecompiler(Project project, QuiltflowerExtension extension) {
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

    private static File resolveQuiltflower(Project project, QuiltflowerExtension extension) throws IOException {
        Path cache = project.getRootProject().getProjectDir().toPath().resolve(".gradle").resolve("loom-quiltflower-cache");

        if (Files.notExists(cache)) {
            Files.createDirectories(cache);
        }

        boolean refresh = project.getGradle().getStartParameter().isRefreshDependencies() || Boolean.getBoolean("loom-quiltflower.refresh");
        QuiltflowerSource source = extension.getSource().get();
        @Nullable String version = source.getProvidedVersion();
        @Nullable Path qfJar = version != null ? cache.resolve("quiltflower-" + version + "-remapped.jar") : null;

        if (qfJar == null || Files.notExists(qfJar) || refresh) {
            Path baseQfJar = cache.resolve("quiltflower-unprocessed.jar");
            Files.deleteIfExists(baseQfJar);

            try (InputStream in = source.open()) {
                Files.copy(in, baseQfJar);
            } catch (Exception e) {
                throw new IOException("Source " + source + " could not resolve Quiltflower", e);
            }

            if (version == null) {
                version = MoreFiles.asByteSource(baseQfJar).hash(Hashing.sha256()).toString().substring(0, 16);
                qfJar = cache.resolve("quiltflower-" + version + "-remapped.jar");
            }

            Remapping.remapQuiltflower(baseQfJar.toFile(), qfJar.toFile(), Collections.emptySet());
        }

        return qfJar.toFile();
    }
}
