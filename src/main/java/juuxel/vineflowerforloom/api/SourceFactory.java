package juuxel.vineflowerforloom.api;

import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

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
     * The dependency resolved is {@code org.vineflower:vineflower:$version} or {@code org.quiltmc:quiltflower:$version}
     * depending on the brand of the version.
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

    /**
     * Creates a source that downloads the decompiler from the official release or snapshot repository for
     * the provided brand.
     *
     * @param version the decompiler version
     * @param brand   the decompiler brand, will be computed automatically if the value is null
     * @return the created source
     * @since 1.11.0
     */
    DecompilerSource fromOfficialRepository(Provider<String> version, Provider<@Nullable DecompilerBrand> brand);

    /**
     * Creates a source that downloads the decompiler from the official release or snapshot repository for that version.
     *
     * @param version the decompiler version
     * @return the created source
     * @since 1.11.0
     */
    DecompilerSource fromOfficialRepository(Provider<String> version);

    // TODO: Should these be deprecated?

    /**
     * Creates a source that downloads the decompiler from the QuiltMC release Maven repository.
     *
     * @param version the decompiler version
     * @return the created source
     */
    @Deprecated
    DecompilerSource fromQuiltMaven(Provider<String> version);

    /**
     * Creates a source that downloads the decompiler from the QuiltMC snapshot Maven repository.
     *
     * @param version the decompiler version
     * @return the created sources
     * @since 1.9.0
     */
    @Deprecated
    DecompilerSource fromQuiltSnapshotMaven(Provider<String> version);
}
