package juuxel.loomquiltflower.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class QuiltMavenQuiltflowerSource implements QuiltflowerSource {
    private final String version;

    public QuiltMavenQuiltflowerSource(String version) {
        this.version = version;
    }

    @Override
    public InputStream open() throws IOException {
        URL url = new URL(String.format("https://maven.quiltmc.org/repository/release/org/quiltmc/quiltflower/%s/quiltflower-%s.jar", version, version));
        return url.openStream();
    }
}
