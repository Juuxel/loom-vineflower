package juuxel.loomquiltflower.api;

import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.ApiStatus;

/**
 * Creates various {@linkplain QuiltflowerSource Quiltflower sources}.
 *
 * @since 1.2.0
 */
@ApiStatus.NonExtendable
public interface SourceFactory {
    /**
     * Creates a file-based source.
     *
     * @param path the path to the file, resolved as per {@link org.gradle.api.Project#files(Object...)}.
     * @return the created source
     */
    QuiltflowerSource fromFile(Object path);

    /**
     * Creates an URL-based source.
     *
     * @param url the url to download or read, resolved as per {@link org.gradle.api.Project#uri(Object)}
     * @return the created source
     */
    QuiltflowerSource fromUrl(Object url);

    /**
     * Creates a dependency-based source that is resolved from project repositories.
     * The dependency resolved is {@code org.quiltmc:quiltflower:$version},
     *
     * @param version the Quiltflower version
     * @return the created source
     */
    QuiltflowerSource fromProjectRepositories(Provider<String> version);

    /**
     * Creates a dependency-based source that is resolved from project repositories.
     *
     * @param dependencyNotation the dependency notation
     * @return the created source
     * @see org.gradle.api.artifacts.dsl.DependencyHandler dependency notation details
     */
    QuiltflowerSource fromDependency(Object dependencyNotation);

    /**
     * Creates a source that downloads Quiltflower from the QuiltMC release Maven repository.
     *
     * @param version the Quiltflower version
     * @return the created source
     */
    QuiltflowerSource fromQuiltMaven(Provider<String> version);

    /**
     * Creates a source that downloads Quiltflower from the QuiltMC snapshot Maven repository.
     *
     * @param version the Quiltflower version
     * @return the created sources
     * @since 1.9.0
     */
    QuiltflowerSource fromQuiltSnapshotMaven(Provider<String> version);
}
