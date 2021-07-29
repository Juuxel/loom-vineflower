package juuxel.loomquiltflower.api;

import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface QuiltflowerExtension {
    /**
     * {@return the current Quiltflower version}
     *
     * <p>Only used if the source is {@link #fromProjectRepositories()} or {@link #fromQuiltMaven()}.
     */
    Property<String> getQuiltflowerVersion();

    /**
     * {@return the current Quiltflower source}
     */
    Property<QuiltflowerSource> getSource();

    /**
     * {@return a factory creating various {@linkplain QuiltflowerSource Quiltflower sources}}
     */
    SourceFactory getSourceFactory();

    /**
     * Sets the Quiltflower source to read from the specified file.
     *
     * @param path the file path
     * @see SourceFactory#fromFile(Object)
     */
    default void fromFile(Object path) {
        getSource().set(getSourceFactory().fromFile(path));
    }

    /**
     * Sets the Quiltflower source to read from the specified URL.
     *
     * @param url the url
     * @see SourceFactory#fromUrl(Object)
     */
    default void fromUrl(Object url) {
        getSource().set(getSourceFactory().fromUrl(url));
    }

    /**
     * Sets the Quiltflower source to resolve Quiltflower from the project repositories.
     * The version is set in {@link #getQuiltflowerVersion()}.
     *
     * @see SourceFactory#fromProjectRepositories(Provider)
     */
    default void fromProjectRepositories() {
        getSource().set(getSourceFactory().fromProjectRepositories(getQuiltflowerVersion()));
    }

    /**
     * Sets the Quiltflower source to resolve Quiltflower as a custom dependency
     * from the project repositories.
     *
     * @param dependencyNotation the dependency notation
     * @see SourceFactory#fromProjectRepositories(Provider)
     */
    default void fromDependency(Object dependencyNotation) {
        getSource().set(getSourceFactory().fromDependency(dependencyNotation));
    }

    /**
     * Sets the Quiltflower source to download Quiltflower from the QuiltMC Maven repository.
     * The version is set in {@link #getQuiltflowerVersion()}.
     *
     * <p>This is the default source for downloading Quiltflower.
     *
     * @see SourceFactory#fromQuiltMaven(Provider)
     */
    default void fromQuiltMaven() {
        getSource().set(getSourceFactory().fromQuiltMaven(getQuiltflowerVersion()));
    }
}
