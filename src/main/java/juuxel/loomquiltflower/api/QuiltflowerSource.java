package juuxel.loomquiltflower.api;

import java.io.IOException;
import java.io.InputStream;

public interface QuiltflowerSource {
    InputStream open() throws IOException;
}
