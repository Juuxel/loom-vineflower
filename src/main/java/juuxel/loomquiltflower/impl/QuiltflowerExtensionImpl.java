package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.api.QuiltflowerPreferences;
import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.api.SourceFactory;
import juuxel.loomquiltflower.impl.source.ConstantUrlQuiltflowerSource;
import juuxel.loomquiltflower.impl.source.QuiltMavenQuiltflowerSource;
import juuxel.loomquiltflower.impl.source.RepositoryQuiltflowerSource;
import org.gradle.api.Project;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.net.MalformedURLException;

public class QuiltflowerExtensionImpl implements QuiltflowerExtension {
    private static final String DEFAULT_QUILTFLOWER_VERSION = "CURRENT_QUILTFLOWER_VERSION";
    private final Project project;
    private final Property<String> quiltflowerVersion;
    private final Property<QuiltflowerSource> source;
    private final SourceFactory sourceFactory = new SourceFactoryImpl();
    private final MapProperty<String, Object> preferenceMap;
    private final QuiltflowerPreferences preferences;

    public QuiltflowerExtensionImpl(Project project) {
        this.project = project;
        quiltflowerVersion = project.getObjects().property(String.class).convention(DEFAULT_QUILTFLOWER_VERSION);
        source = project.getObjects().property(QuiltflowerSource.class).convention(sourceFactory.fromQuiltMaven(quiltflowerVersion));
        preferenceMap = project.getObjects().mapProperty(String.class, Object.class).empty();
        preferences = project.getObjects().newInstance(PreferencesImpl.class, this);
    }

    @Override
    public Property<String> getQuiltflowerVersion() {
        return quiltflowerVersion;
    }

    @Override
    public Property<QuiltflowerSource> getSource() {
        return source;
    }

    @Override
    public SourceFactory getSourceFactory() {
        return sourceFactory;
    }

    @Override
    public QuiltflowerPreferences getPreferences() {
        return preferences;
    }

    private final class SourceFactoryImpl implements SourceFactory {
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
        public QuiltflowerSource fromProjectRepositories(Provider<String> version) {
            return new RepositoryQuiltflowerSource(project, version);
        }

        @Override
        public QuiltflowerSource fromDependency(Object dependencyNotation) {
            return new RepositoryQuiltflowerSource(project, dependencyNotation);
        }

        @Override
        public QuiltflowerSource fromQuiltMaven(Provider<String> version) {
            return new QuiltMavenQuiltflowerSource(version);
        }
    }

    public static class PreferencesImpl implements QuiltflowerPreferences {
        private final QuiltflowerExtensionImpl extension;

        @Inject
        public PreferencesImpl(QuiltflowerExtensionImpl extension) {
            this.extension = extension;
        }

        @Override
        public MapProperty<String, Object> asMap() {
            return extension.preferenceMap;
        }

        // For the Groovy DSL

        @SuppressWarnings("unused")
        @Nullable
        public Object propertyMissing(String key) {
            return get(key);
        }

        @SuppressWarnings("unused")
        public void propertyMissing(String key, Object value) {
            set(key, value);
        }
    }
}
