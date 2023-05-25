package juuxel.loomquiltflower.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.impl.util.Streams;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.VisibleForTesting;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;

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

    public static String findLatestSnapshot() throws Exception {
        URL url = new URL(
            "%s/org/quiltmc/quiltflower/maven-metadata.xml"
                .formatted(QuiltMavenQuiltflowerSource.SNAPSHOT_URL)
        );

        Document document;

        try (InputStream in = url.openStream()) {
            document = DocumentBuilderFactory.newDefaultInstance()
                .newDocumentBuilder()
                .parse(in);
        } catch (IOException e) {
            throw new IOException("Could not read maven-metadata.xml for Quiltflower snapshots (" + url + ")", e);
        }

        return getLatestVersion(document, url);
    }

    @VisibleForTesting
    public static String getLatestVersion(Document document, Object url) {
        return Streams.of(document.getElementsByTagName("metadata"))
            .flatMap(node -> Streams.of(((Element) node).getElementsByTagName("versioning")))
            .flatMap(node -> Streams.of(((Element) node).getElementsByTagName("latest")))
            .map(Node::getTextContent)
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(
                "Could not find latest version in maven-metadata.xml (" + url + ")"
            ));
    }

    public enum Repository {
        RELEASE,
        SNAPSHOT
    }
}
