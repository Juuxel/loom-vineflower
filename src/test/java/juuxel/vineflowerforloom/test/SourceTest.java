package juuxel.vineflowerforloom.test;

import juuxel.vineflowerforloom.impl.source.MavenDecompilerSource;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SourceTest {
    private static Document readXml(String content) throws Exception {
        return DocumentBuilderFactory.newDefaultInstance()
            .newDocumentBuilder()
            .parse(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void findLatestVersion() throws Exception {
        String mavenMetadata = """
            <metadata>
                <groupId>org.quiltflower</groupId>
                <artifactId>quiltflower</artifactId>
                <versioning>
                  <latest>1.2.3</latest>
                  <versions>
                      <version>1.2.2</version>
                      <version>1.2.3</version>
                      <version>1.2.4</version>
                      <version>1.2.5</version>
                  </versions>
                </versioning>
                <lastUpdated>20230415181700</lastUpdated>
            </metadata>
            """;
        Document document = readXml(mavenMetadata);
        String version = MavenDecompilerSource.getLatestVersion(document, null);
        assertThat(version).isEqualTo("1.2.3");
    }

    @Test
    void missingLatestVersion() throws Exception {
        String mavenMetadata = """
            <metadata>
                <groupId>org.quiltflower</groupId>
                <artifactId>quiltflower</artifactId>
                <versioning>
                  <versions>
                      <version>1.2.2</version>
                      <version>1.2.3</version>
                      <version>1.2.4</version>
                      <version>1.2.5</version>
                  </versions>
                </versioning>
                <lastUpdated>20230415181700</lastUpdated>
            </metadata>
            """;
        Document document = readXml(mavenMetadata);
        String url = "https://example.com/maven-metadata.xml";
        assertThatThrownBy(() -> MavenDecompilerSource.getLatestVersion(document, url))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessageContaining(url);
    }

    @Test
    void resolveSnapshotVersion() throws Exception {
        String mavenMetadata = """
            <metadata>
                <groupId>org.quiltmc</groupId>
                <artifactId>quiltflower</artifactId>
                <version>1.2.3-SNAPSHOT</version>
                <versioning>
                    <lastUpdated>20230527142100</lastUpdated>
                    <snapshotVersions>
                        <snapshotVersion>
                            <extension>pom</extension>
                            <value>1.2.3-20230527.142100-3</value>
                            <updated>20230527142100</updated>
                        </snapshotVersion>
                        <snapshotVersion>
                            <extension>jar</extension>
                            <value>1.2.3-20230527.142100-2</value>
                            <updated>20230527142100</updated>
                        </snapshotVersion>
                        <snapshotVersion>
                            <extension>jar</extension>
                            <classifier>slim</classifier>
                            <value>1.2.3-20230527.142100-1</value>
                            <updated>20230527142100</updated>
                        </snapshotVersion>
                    </snapshotVersions>
                </versioning>
            </metadata>
            """;
        var document = readXml(mavenMetadata);
        assertThat(MavenDecompilerSource.findLatestSnapshot(document))
            .isEqualTo("1.2.3-20230527.142100-2");
    }

    @Test
    void resolveMissingSnapshotVersion() throws Exception {
        String mavenMetadata = """
            <metadata>
                <groupId>org.quiltmc</groupId>
                <artifactId>quiltflower</artifactId>
                <version>1.2.3-SNAPSHOT</version>
                <versioning>
                    <lastUpdated>20230527142100</lastUpdated>
                    <snapshotVersions>
                        <snapshotVersion>
                            <extension>pom</extension>
                            <value>1.2.3-20230527.142100-1</value>
                            <updated>20230527142100</updated>
                        </snapshotVersion>
                        <snapshotVersion>
                            <extension>jar</extension>
                            <classifier>slim</classifier>
                            <value>1.2.3-20230527.142100-1</value>
                            <updated>20230527142100</updated>
                        </snapshotVersion>
                    </snapshotVersions>
                </versioning>
            </metadata>
            """;
        var document = readXml(mavenMetadata);
        assertThat(MavenDecompilerSource.findLatestSnapshot(document))
            .isNull();
    }
}
