package juuxel.loomquiltflower.test;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasicProjectTest extends ProjectTest {
    @Test
    void test() {
        // Set up
        setupProject("basic");

        // Run
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDirectory)
            .withArguments("genSourcesWithFernFlower", "--stacktrace")
            .forwardOutput()
            .withDebug(true)
            .build();

        assertThat(result.task(":genSourcesWithQuiltflower").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }
}
