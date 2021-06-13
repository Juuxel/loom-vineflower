package juuxel.loomquiltflower.plugin;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.type.ArtifactTypeDefinition;

public final class RemapSetup {
    public static void setup(Project project) {
        project.getPlugins().apply("java");

        DependencyHandler d = project.getDependencies();
        d.attributesSchema(schema -> schema.attribute(RemapState.REMAP_STATE_ATTRIBUTE));
        d.getArtifactTypes().getByName(ArtifactTypeDefinition.JAR_TYPE, definition -> {
            definition.getAttributes().attribute(RemapState.REMAP_STATE_ATTRIBUTE, RemapState.UNTOUCHED);
        });

        d.registerTransform(RemapAction.class, spec -> {
            spec.getFrom().attribute(RemapState.REMAP_STATE_ATTRIBUTE, RemapState.UNTOUCHED);
            spec.getTo().attribute(RemapState.REMAP_STATE_ATTRIBUTE, RemapState.REMAPPED);
        });
    }
}
