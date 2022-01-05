package juuxel.loomquiltflower.api;

import kotlin.Pair;
import org.gradle.api.Action;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@ApiStatus.NonExtendable
public interface QuiltflowerExtension {
    /**
     * The name of this extension in {@linkplain org.gradle.api.Project#getExtensions() the extension container}.
     */
    String NAME = "quiltflower";

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
     * {@return the Quiltflower decompilation preferences}
     *
     * <p>This method is experimental and may be removed in a minor release.
     */
    @ApiStatus.Experimental
    QuiltflowerPreferences getPreferences();

    /**
     * Configures Quiltflower decompilation preferences.
     *
     * <p>This method is experimental and may be removed in a minor release.
     *
     * @param action the configuration action
     */
    @ApiStatus.Experimental
    default void preferences(Action<QuiltflowerPreferences> action) {
        action.execute(getPreferences());
    }

    /**
     * Adds Quiltflower decompilation preferences.
     *
     * <p>This method is experimental and may be removed in a minor release.
     *
     * @param preferences the preferences as a map
     */
    @ApiStatus.Experimental
    default void preferences(Map<String, ?> preferences) {
        preferences(p -> p.put(preferences));
    }

    /**
     * Adds Quiltflower decompilation preferences.
     *
     * <p>This method is experimental and may be removed in a minor release.
     *
     * @param preferences the preferences as an array of key-value pairs
     */
    @ApiStatus.Experimental
    default void preferences(Pair<String, ?>... preferences) {
        preferences(p -> p.put(preferences));
    }

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

    /**
     * If true, Quiltflower will be added to the runtime classpath.
     * This is useful for debugging mixins with the {@code mixin.debug.decompile} system property.
     *
     * <p>The Quiltflower version will be from {@linkplain #getSource() the current source}.
     *
     * @return the property
     * @since 1.4.0
     */
    Property<Boolean> getAddToRuntimeClasspath();
}
