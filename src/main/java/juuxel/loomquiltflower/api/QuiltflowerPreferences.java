package juuxel.loomquiltflower.api;

import kotlin.Pair;
import kotlin.collections.MapsKt;
import org.gradle.api.provider.MapProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Manages Quiltflower decompilation preferences.
 *
 * <p>This interface is experimental, and it or any contained methods may be removed in a minor release.
 *
 * @since 2.0.0
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface QuiltflowerPreferences {
    /**
     * {@return a map property containing the preferences}
     * Any changes made with this interface will be reflected in the property.
     */
    MapProperty<String, Object> asMap();

    /**
     * Gets the value for a key from the underlying preference map.
     *
     * @param key the key
     * @return the value for the key, or null if not found
     */
    @Nullable
    default Object get(String key) {
        return asMap().get().get(key);
    }

    /**
     * Adds a Quiltflower decompilation preference.
     *
     * @param key   the preference key
     * @param value the preference value
     */
    default void set(String key, Object value) {
        asMap().put(key, value);
    }

    /**
     * Adds Quiltflower decompilation preferences.
     *
     * @param preferences the preferences as a map
     */
    default void put(Map<String, ?> preferences) {
        asMap().putAll(preferences);
    }

    /**
     * Adds Quiltflower decompilation preferences.
     *
     * <p>This method is experimental and may be removed in a minor release.
     *
     * @param preferences the preferences as an array of key-value pairs
     */
    default void put(Pair<String, ?>... preferences) {
        put(MapsKt.mapOf(preferences));
    }
}
