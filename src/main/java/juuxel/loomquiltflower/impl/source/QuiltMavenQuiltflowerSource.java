package juuxel.loomquiltflower.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.impl.util.Streams;
import juuxel.loomquiltflower.impl.util.XmlView;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.Nullable;
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
        String baseVersion = version.get();
        String artifactVersion = baseVersion;

        if (baseVersion.endsWith("-SNAPSHOT")) {
            @Nullable String snapshot = findLatestSnapshot(repository, baseVersion);
            if (snapshot != null) artifactVersion = snapshot;
        }

        URL url = new URL("%s/org/quiltmc/quiltflower/%s/quiltflower-%s.jar"
            .formatted(repository.url, baseVersion, artifactVersion));
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
        String url = "%s/org/quiltmc/quiltflower/maven-metadata.xml"
            .formatted(QuiltMavenQuiltflowerSource.SNAPSHOT_URL);
        Document document = readXmlDocument(url);
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

    private static Document readXmlDocument(String url) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            return DocumentBuilderFactory.newDefaultInstance()
                .newDocumentBuilder()
                .parse(in);
        } catch (Exception e) {
            throw new IOException("Could not read maven-metadata.xml for Quiltflower snapshots (" + url + ")", e);
        }
    }

    private static @Nullable String findLatestSnapshot(Repository repository, String baseVersion) throws IOException {
        Document document = readXmlDocument(
            "%s/org/quiltmc/quiltflower/%s/maven-metadata.xml"
                .formatted(repository.url, baseVersion)
        );
        return findLatestSnapshot(document);
    }

    /**
     * Finds the latest snapshot version in a version-level maven-metadata.xml.
     * This method only considers snapshot versions that have {@code <extension>jar</extension>} and no classifier.
     *
     * @param document the maven metadata document
     * @return the latest snapshot, or null if not found
     */
    @VisibleForTesting
    public static @Nullable String findLatestSnapshot(Document document) {
        var view = new XmlView(document.getChildNodes());

        return view
            .get("versioning")
            .get("snapshotVersions")
            .get("snapshotVersion")
            .filter(version -> "jar".equals(version.get("extension").getTextContent().orElse(null)) &&
                version.get("classifier").getTextContent().isEmpty())
            .get("value")
            .getTextContent()
            .orElse(null);
    }

    public enum Repository {
        RELEASE(RELEASE_URL),
        SNAPSHOT(SNAPSHOT_URL);

        private final String url;

        Repository(String url) {
            this.url = url;
        }
    }
}
