package juuxel.vineflowerforloom.api;

import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.ApiStatus;

/**
 * Creates various {@linkplain DecompilerSource decompiler sources}.
 *
 * @since 1.11.0
 */
@ApiStatus.NonExtendable
public interface SourceFactory {
    /**
     * Creates a file-based source.
     *
     * @param path the path to the file, resolved as per {@link org.gradle.api.Project#files(Object...)}.
     * @return the created source
     */
    DecompilerSource fromFile(Object path);

    /**
     * Creates an URL-based source.
     *
     * @param url the url to download or read, resolved as per {@link org.gradle.api.Project#uri(Object)}
     * @return the created source
     */
    DecompilerSource fromUrl(Object url);

    /**
     * Creates a dependency-based source that is resolved from project repositories.
     * The dependency resolved is {@code org.quiltmc:quiltflower:$version},
     * TODO: VF coordinates
     *
     * @param version the decompiler version
     * @return the created source
     */
    DecompilerSource fromProjectRepositories(Provider<String> version);

    /**
     * Creates a dependency-based source that is resolved from project repositories.
     *
     * @param dependencyNotation the dependency notation
     * @return the created source
     * @see org.gradle.api.artifacts.dsl.DependencyHandler dependency notation details
     */
    DecompilerSource fromDependency(Object dependencyNotation);

    // TODO: Should these be deprecated?

    /**
     * Creates a source that downloads the decompiler from the QuiltMC release Maven repository.
     *
     * @param version the decompiler version
     * @return the created source
     */
    DecompilerSource fromQuiltMaven(Provider<String> version);

    /**
     * Creates a source that downloads the decompiler from the QuiltMC snapshot Maven repository.
     *
     * @param version the decompiler version
     * @return the created sources
     * @since 1.9.0
     */
    DecompilerSource fromQuiltSnapshotMaven(Provider<String> version);
}
