package juuxel.loomquiltflower.plugin;

import juuxel.loomquiltflower.core.Remapping;
import org.gradle.api.artifacts.transform.InputArtifact;
import org.gradle.api.artifacts.transform.InputArtifactDependencies;
import org.gradle.api.artifacts.transform.TransformAction;
import org.gradle.api.artifacts.transform.TransformOutputs;
import org.gradle.api.artifacts.transform.TransformParameters;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.provider.Provider;

import java.io.File;

public abstract class RemapAction implements TransformAction<TransformParameters.None> {
    @InputArtifact
    protected abstract Provider<FileSystemLocation> getInputArtifact();

    @InputArtifactDependencies
    protected abstract FileCollection getInputDependencies();

    @Override
    public void transform(TransformOutputs outputs) {
        File input = getInputArtifact().get().getAsFile();
        File output = outputs.file("remapped-" + input.getName());
        Remapping.remapQuiltflower(input, output, getInputDependencies());
    }
}
