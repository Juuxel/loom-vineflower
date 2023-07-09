package juuxel.vineflowerforloom.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class ConstantUrlQuiltflowerSource implements QuiltflowerSource {
    private final URL url;

    public ConstantUrlQuiltflowerSource(URL url) {
        this.url = url;
    }

    @Override
    public InputStream open() throws IOException {
        return url.openStream();
    }

    @Override
    public @Nullable String getProvidedVersion() {
        return null;
    }

    @Override
    public String toString() {
        return "fromUrl(" + url + ")";
    }
}
