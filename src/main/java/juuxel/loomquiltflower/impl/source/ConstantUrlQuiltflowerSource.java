package juuxel.loomquiltflower.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;

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
}
