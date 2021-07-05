package juuxel.loomquiltflower.api;

import juuxel.loomquiltflower.impl.source.ConstantUrlQuiltflowerSource;
import juuxel.loomquiltflower.impl.source.QuiltMavenQuiltflowerSource;
import juuxel.loomquiltflower.impl.source.RepositoryQuiltflowerSource;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;

import java.net.URL;

public class LoomQuiltflowerExtension {
    private static final String DEFAULT_QUILTFLOWER_VERSION = "CURRENT_QUILTFLOWER_VERSION";
    private final Project project;
    private final Property<String> quiltflowerVersion;
    private final Property<QuiltflowerSource> quiltflowerSource;

    public LoomQuiltflowerExtension(Project project) {
        quiltflowerVersion = project.getObjects().property(String.class);
        this.project = project;
        quiltflowerVersion.set(DEFAULT_QUILTFLOWER_VERSION);
        quiltflowerSource = project.getObjects().property(QuiltflowerSource.class);
        quiltflowerSource.set(project.provider(() -> new QuiltMavenQuiltflowerSource(quiltflowerVersion.get())));
    }

    public Property<String> getQuiltflowerVersion() {
        return quiltflowerVersion;
    }

    public Property<QuiltflowerSource> getQuiltflowerSource() {
        return quiltflowerSource;
    }

    public Provider<QuiltflowerSource> fromFile(Object path) {
        return project.provider(() -> new ConstantUrlQuiltflowerSource(project.file(path).toURI().toURL()));
    }

    public Provider<QuiltflowerSource> fromUrl(URL url) {
        return project.provider(() -> new ConstantUrlQuiltflowerSource(url));
    }

    public Provider<QuiltflowerSource> fromProjectRepositories() {
        return project.provider(() -> new RepositoryQuiltflowerSource(project, quiltflowerVersion.get()));
    }
}
