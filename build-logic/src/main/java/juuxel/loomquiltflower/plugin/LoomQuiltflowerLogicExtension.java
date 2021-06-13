package juuxel.loomquiltflower.plugin;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.attributes.HasConfigurableAttributes;

public class LoomQuiltflowerLogicExtension {
    private final Project project;

    public LoomQuiltflowerLogicExtension(Project project) {
        this.project = project;
    }

    public Dependency quiltflower() {
        Dependency dep = project.getDependencies().create("org.quiltmc:quiltflower:" + project.getRootProject().property("quiltflower-version"));
        ((HasConfigurableAttributes<?>) dep).attributes(attributes -> attributes.attribute(RemapState.REMAP_STATE_ATTRIBUTE, RemapState.REMAPPED));
        return dep;
    }
}
