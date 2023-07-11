package juuxel.vineflowerforloom.test.oldapi;

import juuxel.vineflowerforloom.test.ProjectTest;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class PreferencesTest extends ProjectTest {
    @Test
    void test() {
        // Set up
        setupProject("deprecated/preferences", "gradle.properties");

        // Run
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectDirectory)
            .withArguments("printPreferences")
            .forwardOutput()
            .withDebug(true)
            .build();

        String preferencePattern = "^[a-z]{3}:.+$";
        Map<String, String> preferences = result.getOutput().lines()
            .filter(it -> it.matches(preferencePattern))
            .map(it -> it.split(":", 2))
            .collect(Collectors.toMap(split -> split[0], split -> split[1]));

        Map<String, String> expected = Map.of(
            // gradle.properties
            "abc", "2",
            "efg", "hello",
            "klm", "new",
            "nop", "new",
            // quiltflower.preferences block
            "hij", "1",
            // preset methods
            "jvn", "true",
            "isl", "false"
        );
        assertThat(preferences).isEqualTo(expected);
    }
}
