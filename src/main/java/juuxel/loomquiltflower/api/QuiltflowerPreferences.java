package juuxel.loomquiltflower.api;

import juuxel.loomquiltflower.impl.relocated.quiltflower.main.extern.IFernflowerPreferences;
import kotlin.Pair;
import kotlin.collections.MapsKt;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Manages Quiltflower decompilation preferences.
 *
 * <p>This interface is experimental, and it or any contained methods may be removed in a minor release.
 *
 * @since 1.2.0
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface QuiltflowerPreferences {
    /**
     * {@return a map property containing the preferences}
     * Any changes made with this interface will be reflected in the property, and vice versa.
     */
    MapProperty<String, Object> asMap();

    /**
     * {@return a provider containing the preferences as a map of strings}
     * Any changes made with this interface or {@link #asMap()} will be reflected in the provider.
     */
    Provider<Map<String, String>> asStringMap();

    /**
     * Gets the value for a key from the underlying preference map.
     *
     * <p>Calling this method resolves the {@link #asMap()} property.
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
    @SuppressWarnings("unchecked")
    default void put(Pair<String, ?>... preferences) {
        put((Map<String, ?>) MapsKt.mapOf(preferences));
    }

    // Specific preferences

    /**
     * Sets the value of the {@code INLINE_SIMPLE_LAMBDAS} preference ({@code isl}).
     *
     * @param inlineSimpleLambdas the new value
     */
    default void inlineSimpleLambdas(Object inlineSimpleLambdas) {
        set(IFernflowerPreferences.INLINE_SIMPLE_LAMBDAS, inlineSimpleLambdas);
    }

    /**
     * Sets the value of the {@code USE_JAD_VARNAMING} preference ({@code jvn}).
     *
     * @param useJadVarnaming the new value
     */
    default void useJadVarnaming(Object useJadVarnaming) {
        set(IFernflowerPreferences.USE_JAD_VARNAMING, useJadVarnaming);
    }

    /**
     * Sets the value of the {@code PATTERN_MATCHING} preference ({@code pam}).
     *
     * @param patternMatching the new value
     * @since 1.4.0
     */
    default void patternMatching(Object patternMatching) {
        set(IFernflowerPreferences.PATTERN_MATCHING, patternMatching);
    }

    /**
     * Sets the value of the {@code EXPERIMENTAL_TRY_LOOP_FIX} preference ({@code tlf}).
     *
     * @param experimentalTryLoopFix the new value
     * @since 1.4.0
     */
    default void experimentalTryLoopFix(Object experimentalTryLoopFix) {
        set(IFernflowerPreferences.EXPERIMENTAL_TRY_LOOP_FIX, experimentalTryLoopFix);
    }
}
