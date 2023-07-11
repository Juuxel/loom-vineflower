package juuxel.loomquiltflower.api;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.ApiStatus;

/**
 * @deprecated Replaced with {@link VineflowerExtension}.
 */
@Deprecated
@ApiStatus.NonExtendable
public interface QuiltflowerExtension extends VineflowerExtension {
    /**
     * The name of this extension in {@linkplain org.gradle.api.Project#getExtensions() the extension container}.
     */
    String NAME = "quiltflower";

    /**
     * {@return the current Quiltflower version}
     *
     * <p>Only used if the source is {@link #fromProjectRepositories()} or {@link #fromQuiltMaven()}.
     * @deprecated Use {@link #getToolVersion()} instead.
     */
    @Deprecated
    default Property<String> getQuiltflowerVersion() {
        return getToolVersion();
    }

    /**
     * {@return the current Quiltflower source}
     * @deprecated Use {@link #getToolSource()} instead.
     */
    @Deprecated
    Property<QuiltflowerSource> getSource();

    /**
     * {@return a factory creating various {@linkplain QuiltflowerSource Quiltflower sources}}
     */
    @Override
    SourceFactory getSourceFactory();

    /**
     * {@return the Quiltflower decompilation preferences}
     */
    @Override
    QuiltflowerPreferences getPreferences();
}
