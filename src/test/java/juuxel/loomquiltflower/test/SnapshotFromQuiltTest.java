package juuxel.loomquiltflower.test;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnapshotFromQuiltTest extends ProjectTest {
    @Test
    void test() {
        // Set up
        setupProject("snapshot-from-quilt");

        // Run
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDirectory)
            .withArguments("help", "--stacktrace")
            .forwardOutput()
            .withDebug(true)
            .build();

        assertThat(result.getOutput()).contains("Resolved: 1.9.0-20221030.213144-213");
        assertThat(result.task(":help").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }
}
