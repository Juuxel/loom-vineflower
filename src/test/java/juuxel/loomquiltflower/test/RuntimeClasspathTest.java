package juuxel.loomquiltflower.test;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuntimeClasspathTest extends ProjectTest {
    @Disabled("you're not needed here")
    @Test
    void test() {
        // Set up
        setupProject("runtime-classpath");

        // Run
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDirectory)
            .withArguments("verifyRuntimeClasspath", "genSourcesWithQuiltflower", "--stacktrace")
            .forwardOutput()
            .withDebug(true)
            .build();

        assertThat(result.task(":genSourcesWithQuiltflower").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
        assertThat(result.task(":verifyRuntimeClasspath").getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }
}
