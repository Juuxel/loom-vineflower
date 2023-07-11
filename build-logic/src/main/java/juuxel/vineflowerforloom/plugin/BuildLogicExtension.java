package juuxel.vineflowerforloom.plugin;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.attributes.HasConfigurableAttributes;

public class BuildLogicExtension {
    private final Project project;

    public BuildLogicExtension(Project project) {
        this.project = project;
    }

    public Dependency vineflower() {
        Dependency dep = project.getDependencies().create("org.vineflower:vineflower:" + project.getRootProject().property("vineflower-version"));
        ((HasConfigurableAttributes<?>) dep).attributes(attributes -> attributes.attribute(RemapState.REMAP_STATE_ATTRIBUTE, RemapState.REMAPPED));
        return dep;
    }
}
