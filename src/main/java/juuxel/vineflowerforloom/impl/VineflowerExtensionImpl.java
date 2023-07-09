package juuxel.vineflowerforloom.impl;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.api.QuiltflowerPreferences;
import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.api.SourceFactory;
import juuxel.vineflowerforloom.impl.module.LqfModule;
import juuxel.vineflowerforloom.impl.source.ConstantUrlQuiltflowerSource;
import juuxel.vineflowerforloom.impl.source.QuiltMavenQuiltflowerSource;
import juuxel.vineflowerforloom.impl.source.RepositoryQuiltflowerSource;
import juuxel.vineflowerforloom.api.DecompilerSource;
import juuxel.vineflowerforloom.api.VineflowerExtension;
import org.gradle.api.Project;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class VineflowerExtensionImpl implements VineflowerExtension, QuiltflowerExtension {
    private final Project project;
    private final Property<String> toolVersion;
    private final Property<DecompilerSource> toolSource;
    private final Property<QuiltflowerSource> source;
    private final SourceFactory sourceFactory = new SourceFactoryImpl();
    private final MapProperty<String, Object> preferenceMap;
    private final QuiltflowerPreferences preferences;
    private final Property<Boolean> addToRuntimeClasspath;
    private final Path cache;
    private LqfModule activeModule;

    public VineflowerExtensionImpl(Project project) {
        this.project = project;
        toolVersion = project.getObjects().property(String.class).convention(VineflowerVersion.DEFAULT_VERSION);
        source = project.getObjects().property(QuiltflowerSource.class).convention(sourceFactory.fromQuiltMaven(toolVersion));
        toolSource = project.getObjects().property(DecompilerSource.class).convention(source);
        preferenceMap = project.getObjects().mapProperty(String.class, Object.class).empty();
        preferences = project.getObjects().newInstance(PreferencesImpl.class, this);
        addToRuntimeClasspath = project.getObjects().property(Boolean.class).convention(false);
        cache = project.file(".gradle/loom-quiltflower-cache").toPath();
    }

    public LqfModule getActiveModule() {
        if (activeModule == null) {
            throw new IllegalStateException("vinerflower-for-loom module not initialised. Please report this!");
        }

        return activeModule;
    }

    public void setActiveModule(LqfModule activeModule) {
        this.activeModule = activeModule;
    }

    public Path getCache() {
        if (Files.notExists(cache)) {
            try {
                Files.createDirectories(cache);
            } catch (IOException e) {
                throw new UncheckedIOException("Could not create cache at " + cache.toAbsolutePath(), e);
            }
        }

        return cache;
    }

    @Override
    public Property<String> getToolVersion() {
        return toolVersion;
    }

    @Override
    public Property<DecompilerSource> getToolSource() {
        return toolSource;
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
    public void fromLatestQuiltSnapshot() {
        Provider<String> latestSnapshotVersion = project.provider(QuiltMavenQuiltflowerSource::findLatestSnapshot);
        this.getToolSource().set(getSourceFactory().fromQuiltSnapshotMaven(latestSnapshotVersion));
    }

    @Override
    public QuiltflowerPreferences getPreferences() {
        return preferences;
    }

    @Override
    public Property<Boolean> getAddToRuntimeClasspath() {
        return addToRuntimeClasspath;
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
            return new QuiltMavenQuiltflowerSource(version, QuiltMavenQuiltflowerSource.Repository.RELEASE);
        }

        @Override
        public QuiltflowerSource fromQuiltSnapshotMaven(Provider<String> version) {
            return new QuiltMavenQuiltflowerSource(version, QuiltMavenQuiltflowerSource.Repository.SNAPSHOT);
        }
    }

    public static class PreferencesImpl implements QuiltflowerPreferences {
        private final VineflowerExtensionImpl extension;

        @Inject
        public PreferencesImpl(VineflowerExtensionImpl extension) {
            this.extension = extension;
        }

        @Override
        public MapProperty<String, Object> asMap() {
            return extension.preferenceMap;
        }

        @Override
        public Provider<Map<String, String>> asStringMap() {
            return asMap().map(map -> {
                Map<String, String> output = new HashMap<>();

                map.forEach((key, value) -> {
                    // convert booleans to strings
                    if (value instanceof Boolean) {
                        value = ((Boolean) value) ? "1" : "0";
                    }

                    output.put(key, String.valueOf(value));
                });

                return output;
            });
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