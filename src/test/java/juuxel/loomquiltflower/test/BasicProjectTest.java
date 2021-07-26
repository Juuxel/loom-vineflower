package juuxel.loomquiltflower.test;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BasicProjectTest extends ProjectTest {
    private static Map<String, String> getReplacements() {
        return Map.of("LOOM_ID", System.getProperty("loomId", "fabric-loom"));
    }

    @Test
    void test() throws IOException {
        // Set up
        setupProject("basic");
        String buildGradle = getProjectFileText("basic", "build.gradle");

        for (var entry : getReplacements().entrySet()) {
            buildGradle = buildGradle.replace('@' + entry.getKey() + '@', entry.getValue());
        }

        Files.writeString(projectDirectory.toPath().resolve("build.gradle"), buildGradle);

        // Run
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDirectory)
            .withArguments("genSourcesWithQuiltflower", "--stacktrace")
            .forwardOutput()
            .build();

        assertThat(result.task(":genSourcesWithQuiltflower").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }
}
