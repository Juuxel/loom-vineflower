package juuxel.vineflowerforloom.test.oldapi;

import juuxel.vineflowerforloom.test.ProjectTest;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnapshotFromRepositoriesTest extends ProjectTest {
    @Test
    void test() {
        // Set up
        setupProject("deprecated/snapshot-from-repositories");

        // Run
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDirectory)
            .withArguments("genSourcesWithQuiltflower", "--stacktrace")
            .forwardOutput()
            .withDebug(true)
            .build();

        assertThat(result.getOutput()).contains("Resolved: 1.8.1-20220429.000346-3");
        assertThat(result.task(":genSourcesWithQuiltflower").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }
}
