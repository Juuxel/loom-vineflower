package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.impl.source.ConstantUrlQuiltflowerSource;
import juuxel.loomquiltflower.impl.source.QuiltMavenQuiltflowerSource;
import juuxel.loomquiltflower.impl.source.RepositoryQuiltflowerSource;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

import java.net.MalformedURLException;

public class QuiltflowerExtensionImpl implements QuiltflowerExtension {
    private static final String DEFAULT_QUILTFLOWER_VERSION = "CURRENT_QUILTFLOWER_VERSION";
    private final Project project;
    private final Property<String> quiltflowerVersion;
    private final Property<QuiltflowerSource> quiltflowerSource;

    public QuiltflowerExtensionImpl(Project project) {
        this.project = project;
        quiltflowerVersion = project.getObjects().property(String.class).convention(DEFAULT_QUILTFLOWER_VERSION);
        quiltflowerSource = project.getObjects().property(QuiltflowerSource.class).convention(fromQuiltMaven());
    }

    @Override
    public Property<String> getQuiltflowerVersion() {
        return quiltflowerVersion;
    }

    @Override
    public Property<QuiltflowerSource> getQuiltflowerSource() {
        return quiltflowerSource;
    }

    @Override
    public QuiltflowerSource fromFile(Object path) {
        return fromUrl(project.file(path));
    }

    @Override
    public QuiltflowerSource fromUrl(Object url) {
        try {
            return new ConstantUrlQuiltflowerSource(project.uri(url).toURL());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed url: " + url, e);
        }
    }

    @Override
    public QuiltflowerSource fromProjectRepositories() {
        return new RepositoryQuiltflowerSource(project, quiltflowerVersion);
    }

    /**
     * Creates a dependency-based source that is resolved from project repositories.
     *
     * @param dependencyNotation the dependency notation
     * @return the created source
     * @see org.gradle.api.artifacts.dsl.DependencyHandler dependency notation details
     */
    @Override
    public QuiltflowerSource fromDependency(Object dependencyNotation) {
        return new RepositoryQuiltflowerSource(project, dependencyNotation);
    }

    @Override
    public QuiltflowerSource fromQuiltMaven() {
        return new QuiltMavenQuiltflowerSource(quiltflowerVersion);
    }
}
