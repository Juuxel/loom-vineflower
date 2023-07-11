package juuxel.vineflowerforloom.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.vineflowerforloom.api.DecompilerBrand;
import juuxel.vineflowerforloom.impl.DependencyCoordinates;
import juuxel.vineflowerforloom.impl.TimeMachine;
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

public final class MavenDecompilerSource implements QuiltflowerSource {
    @Language("XPath")
    private static final String LATEST_VERSION_XPATH = "/metadata/versioning/latest/text()";
    @Language("XPath")
    private static final String SNAPSHOT_VERSION_XPATH = "/metadata/versioning/snapshotVersions/snapshotVersion[not(classifier) and extension=\"jar\"]/value/text()";
    private final Provider<String> version;
    private final Provider<String> repository;
    private final Provider<@Nullable DecompilerBrand> brand;
    private @Nullable String artifactVersion;

    public MavenDecompilerSource(Provider<String> version, Provider<String> repository, Provider<@Nullable DecompilerBrand> brand) {
        this.version = version;
        this.repository = repository;
        this.brand = brand;
    }

    @Override
    public InputStream open() throws IOException {
        String baseVersion = version.get();
        String artifactVersion = getResolvedVersion();
        @Nullable DecompilerBrand brand = this.brand.getOrNull();
        if (brand == null) brand = TimeMachine.determineBrand(baseVersion);
        var coordinates = TimeMachine.getDependencyCoordinates(brand);

        URL url = new URL("%s/%s/%s/%s-%s.jar"
            .formatted(repository.get(), coordinates.asUrlPart(), baseVersion, coordinates.artifact(), artifactVersion));
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

        if (TimeMachine.isSnapshot(baseVersion)) {
            @Nullable String snapshot = findLatestSnapshot(repository.get(), brand.getOrNull(), baseVersion);
            if (snapshot != null) return snapshot;
        }

        return baseVersion;
    }

    @Override
    public String toString() {
        return "fromMaven[" + repository.get() + "]";
    }

    public static String findLatestSnapshot(String repository) throws Exception {
        String url = "%s/%s/maven-metadata.xml"
            .formatted(DependencyCoordinates.VINEFLOWER.asUrlPart(), repository);
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
            throw new IOException("Could not read maven-metadata.xml for Vineflower snapshots (" + url + ")", e);
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

    private static @Nullable String findLatestSnapshot(String repositoryUrl, @Nullable DecompilerBrand brand, String baseVersion) throws IOException {
        if (brand == null) brand = TimeMachine.determineBrand(baseVersion);
        String dependencyBase = TimeMachine.getDependencyCoordinates(brand).asUrlPart();
        Document document = readXmlDocument(
            "%s/%s/%s/maven-metadata.xml"
                .formatted(repositoryUrl, dependencyBase, baseVersion)
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
}
