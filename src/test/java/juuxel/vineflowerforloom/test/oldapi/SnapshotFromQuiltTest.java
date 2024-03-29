package juuxel.vineflowerforloom.test.oldapi;

import juuxel.vineflowerforloom.test.ProjectTest;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnapshotFromQuiltTest extends ProjectTest {
    @Test
    void test() {
        // Set up
        setupProject("deprecated/snapshot-from-quilt");

        // Run
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDirectory)
            .withArguments("genSourcesWithQuiltflower", "--stacktrace")
            .forwardOutput()
            .withDebug(true)
            .build();

        assertThat(result.getOutput()).contains("Resolved: 1.9.0-20221030.213144-213");
        assertThat(result.task(":genSourcesWithQuiltflower").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }
}
