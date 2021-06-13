package juuxel.loomquiltflower.api;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class LoomQuiltflowerExtension {
    private static final String DEFAULT_QUILTFLOWER_VERSION = "CURRENT_QUILTFLOWER_VERSION";
    private final Property<String> quiltflowerVersion;

    public LoomQuiltflowerExtension(Project project) {
        quiltflowerVersion = project.getObjects().property(String.class);
        quiltflowerVersion.set(DEFAULT_QUILTFLOWER_VERSION);
    }

    public Property<String> getQuiltflowerVersion() {
        return quiltflowerVersion;
    }
}
