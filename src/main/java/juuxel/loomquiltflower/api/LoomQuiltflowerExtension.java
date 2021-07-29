package juuxel.loomquiltflower.api;

import juuxel.loomquiltflower.impl.source.ConstantUrlQuiltflowerSource;
import juuxel.loomquiltflower.impl.source.QuiltMavenQuiltflowerSource;
import juuxel.loomquiltflower.impl.source.RepositoryQuiltflowerSource;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

import java.net.MalformedURLException;

public class LoomQuiltflowerExtension {
    private static final String DEFAULT_QUILTFLOWER_VERSION = "CURRENT_QUILTFLOWER_VERSION";
    private final Project project;
    private final Property<String> quiltflowerVersion;
    private final Property<QuiltflowerSource> quiltflowerSource;

    public LoomQuiltflowerExtension(Project project) {
        this.project = project;
        quiltflowerVersion = project.getObjects().property(String.class).convention(DEFAULT_QUILTFLOWER_VERSION);
        quiltflowerSource = project.getObjects().property(QuiltflowerSource.class).convention(fromQuiltMaven());
    }

    public Property<String> getQuiltflowerVersion() {
        return quiltflowerVersion;
    }

    public Property<QuiltflowerSource> getQuiltflowerSource() {
        return quiltflowerSource;
    }

    public QuiltflowerSource fromFile(Object path) {
        return fromUrl(project.file(path));
    }

    public QuiltflowerSource fromUrl(Object url) {
        try {
            return new ConstantUrlQuiltflowerSource(project.uri(url).toURL());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed url: " + url, e);
        }
    }

    public QuiltflowerSource fromProjectRepositories() {
        return new RepositoryQuiltflowerSource(project, quiltflowerVersion);
    }

    public QuiltflowerSource fromQuiltMaven() {
        return new QuiltMavenQuiltflowerSource(quiltflowerVersion);
    }
}
