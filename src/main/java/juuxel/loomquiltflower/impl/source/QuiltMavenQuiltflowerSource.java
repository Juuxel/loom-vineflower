package juuxel.loomquiltflower.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import org.gradle.api.provider.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class QuiltMavenQuiltflowerSource implements QuiltflowerSource {
    private final Provider<String> version;

    public QuiltMavenQuiltflowerSource(Provider<String> version) {
        this.version = version;
    }

    @Override
    public InputStream open() throws IOException {
        String v = version.get();
        URL url = new URL(String.format("https://maven.quiltmc.org/repository/release/org/quiltmc/quiltflower/%s/quiltflower-%s.jar", v, v));
        return url.openStream();
    }
}
