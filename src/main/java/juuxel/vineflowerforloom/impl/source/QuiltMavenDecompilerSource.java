package juuxel.vineflowerforloom.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import org.gradle.api.provider.Provider;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;

public final class QuiltMavenDecompilerSource implements QuiltflowerSource {
    private static final String RELEASE_URL = "https://maven.quiltmc.org/repository/release";
    private static final String SNAPSHOT_URL = "https://maven.quiltmc.org/repository/snapshot";
    @Language("XPath")
    private static final String LATEST_VERSION_XPATH = "/metadata/versioning/latest/text()";
    @Language("XPath")
    private static final String SNAPSHOT_VERSION_XPATH = "/metadata/versioning/snapshotVersions/snapshotVersion[not(classifier) and extension=\"jar\"]/value/text()";
    private final Provider<String> version;
    private final Repository repository;
    private @Nullable String artifactVersion;

    public QuiltMavenDecompilerSource(Provider<String> version, Repository repository) {
        this.version = version;
        this.repository = repository;
    }

    @Override
    public InputStream open() throws IOException {
        String baseVersion = version.get();
        String artifactVersion = getResolvedVersion();

        URL url = new URL("%s/org/quiltmc/quiltflower/%s/quiltflower-%s.jar"
            .formatted(repository.url, baseVersion, artifactVersion));
        return url.openStream();
    }

    @Override
    public String getProvidedVersion() {
        return version.get();
    }

    @Override
    public String getResolvedVersion() throws IOException {
        if (artifactVersion == null) {
            return artifactVersion = computeArtifactVersion();
        }

        return artifactVersion;
    }

    private String computeArtifactVersion() throws IOException {
        String baseVersion = version.get();

        if (baseVersion.endsWith("-SNAPSHOT")) {
            @Nullable String snapshot = findLatestSnapshot(repository, baseVersion);
            if (snapshot != null) return snapshot;
        }

        return baseVersion;
    }

    @Override
    public String toString() {
        return "fromQuiltMaven";
    }

    public static String findLatestSnapshot() throws Exception {
        String url = "%s/org/quiltmc/quiltflower/maven-metadata.xml"
            .formatted(SNAPSHOT_URL);
        Document document = readXmlDocument(url);
        return getLatestVersion(document, url);
    }

    @VisibleForTesting
    public static String getLatestVersion(Document document, Object url) {
        var version = queryXpath(LATEST_VERSION_XPATH, document);
        if (version == null) {
            throw new NoSuchElementException("Could not find latest version in maven-metadata.xml (" + url + ")");
        }
        return version;
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

    private static @Nullable String queryXpath(@Language("XPath") String expression, Node context) {
        XPathFactory factory = XPathFactory.newDefaultInstance();
        XPath xp = factory.newXPath();
        try {
            var value = xp.evaluate(expression, context);
            return !value.isEmpty() ? value : null;
        } catch (XPathException e) {
            throw new RuntimeException(e);
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
        return queryXpath(SNAPSHOT_VERSION_XPATH, document);
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
