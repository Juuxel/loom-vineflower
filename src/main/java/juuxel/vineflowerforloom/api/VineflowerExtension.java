package juuxel.vineflowerforloom.api;

import kotlin.Pair;
import org.gradle.api.Action;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

/**
 * The extension for configuring Vineflower.
 *
 * @since 1.11.0
 */
@ApiStatus.NonExtendable
public interface VineflowerExtension {
    /**
     * The name of this extension in the {@linkplain org.gradle.api.plugins.ExtensionContainer extension container},
     * {@value}.
     */
    String NAME = "vineflower";

    /**
     * {@return the current decompiler version}
     *
     * <p>Only used if the source is {@link #fromProjectRepositories()} or {@link #fromQuiltMaven()}.
     */
    Property<String> getToolVersion();

    /**
     * {@return the current Vineflower source}
     */
    Property<DecompilerSource> getToolSource();

    /**
     * {@return a factory creating various {@linkplain DecompilerSource decompiler sources}}
     */
    SourceFactory getSourceFactory();

    /**
     * {@return the Vineflower decompilation preferences}
     */
    DecompilerPreferences getPreferences();

    /**
     * Configures Vineflower decompilation preferences.
     *
     * @param action the configuration action
     */
    default void preferences(Action<DecompilerPreferences> action) {
        action.execute(getPreferences());
    }

    /**
     * Adds Vineflower decompilation preferences.
     *
     * @param preferences the preferences as a map
     */
    default void preferences(Map<String, ?> preferences) {
        preferences(p -> p.put(preferences));
    }

    /**
     * Adds Vineflower decompilation preferences.
     *
     * @param preferences the preferences as an array of key-value pairs
     */
    default void preferences(Pair<String, ?>... preferences) {
        preferences(p -> p.put(preferences));
    }

    /**
     * Sets the Vineflower source to read from the specified file.
     *
     * @param path the file path
     * @see SourceFactory#fromFile(Object)
     */
    default void fromFile(Object path) {
        getToolSource().set(getSourceFactory().fromFile(path));
    }

    /**
     * Sets the Vineflower source to read from the specified URL.
     *
     * @param url the url
     * @see SourceFactory#fromUrl(Object)
     */
    default void fromUrl(Object url) {
        getToolSource().set(getSourceFactory().fromUrl(url));
    }

    /**
     * Sets the Vineflower source to resolve Vineflower from the project repositories.
     * The version is set in {@link #getToolVersion()}.
     *
     * @see SourceFactory#fromProjectRepositories(Provider)
     */
    default void fromProjectRepositories() {
        getToolSource().set(getSourceFactory().fromProjectRepositories(getToolVersion()));
    }

    /**
     * Sets the Vineflower source to resolve Vineflower as a custom dependency
     * from the project repositories.
     *
     * @param dependencyNotation the dependency notation
     * @see SourceFactory#fromProjectRepositories(Provider)
     */
    default void fromDependency(Object dependencyNotation) {
        getToolSource().set(getSourceFactory().fromDependency(dependencyNotation));
    }

    /**
     * Sets the Vineflower source to download Vineflower from the QuiltMC release Maven repository.
     * The version is set in {@link #getToolVersion()}.
     *
     * @see SourceFactory#fromQuiltMaven(Provider)
     */
    void fromQuiltMaven();

    /**
     * Sets the Vineflower source to download Vineflower from the QuiltMC snapshot Maven repository.
     * The version is set in {@link #getToolVersion()}.
     *
     * @see SourceFactory#fromQuiltSnapshotMaven(Provider)
     * @see #fromLatestQuiltSnapshot()
     * @since 1.9.0
     */
    void fromQuiltSnapshotMaven();

    /**
     * Sets the Vineflower source to download Vineflower from the official repository for
     * the current {@linkplain #getBrand() brand}. The version is set in {@link #getToolVersion()}.
     *
     * <p>This is the default source for downloading Vineflower.
     *
     * @see SourceFactory#fromOfficialRepository(Provider, Provider)
     * @since 1.11.0
     */
    default void fromOfficialRepository() {
        getToolSource().set(getSourceFactory().fromOfficialRepository(getToolVersion(), getBrand()));
    }

    /**
     * Sets the Vineflower source to download the latest Vineflower snapshot from the QuiltMC snapshot Maven
     * repository.
     *
     * @see SourceFactory#fromQuiltSnapshotMaven(Provider)
     * @see #fromQuiltSnapshotMaven()
     * @since 1.9.0
     */
    void fromLatestQuiltSnapshot();

    /**
     * If true, Vineflower will be added to the runtime classpath.
     * This is useful for debugging mixins with the {@code mixin.debug.decompile} system property.
     *
     * <p>The Vineflower version will be from {@linkplain #getToolSource() the current source}.
     *
     * @return the property
     * @since 1.4.0
     */
    Property<Boolean> getAddToRuntimeClasspath();

    /**
     * The decompiler brand used for determining the dependency coordinates of Vineflower.
     * By default, it will be determined automatically from the {@linkplain #getToolVersion() tool version}.
     *
     * @return the property
     * @since 1.11.0
     */
    Property<DecompilerBrand> getBrand();
}
