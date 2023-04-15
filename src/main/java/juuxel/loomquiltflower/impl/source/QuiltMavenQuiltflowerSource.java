package juuxel.loomquiltflower.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import org.gradle.api.provider.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class QuiltMavenQuiltflowerSource implements QuiltflowerSource {
    private static final String RELEASE_URL = "https://maven.quiltmc.org/repository/release";
    private static final String SNAPSHOT_URL = "https://maven.quiltmc.org/repository/snapshot";
    private final Provider<String> version;
    private final Repository repository;

    public QuiltMavenQuiltflowerSource(Provider<String> version, Repository repository) {
        this.version = version;
        this.repository = repository;
    }

    @Override
    public InputStream open() throws IOException {
        String repositoryUrl = switch (repository) {
            case RELEASE -> RELEASE_URL;
            case SNAPSHOT -> SNAPSHOT_URL;
        };
        String v = version.get();
        URL url = new URL(String.format("%s/org/quiltmc/quiltflower/%s/quiltflower-%s.jar", repositoryUrl, v, v));
        return url.openStream();
    }

    @Override
    public String getProvidedVersion() {
        return version.get();
    }

    @Override
    public String toString() {
        return "fromQuiltMaven";
    }

    public enum Repository {
        RELEASE,
        SNAPSHOT
    }
}
