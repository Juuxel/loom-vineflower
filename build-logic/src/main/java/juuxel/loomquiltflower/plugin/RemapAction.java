package juuxel.loomquiltflower.plugin;

import com.google.common.base.Stopwatch;
import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;
import org.gradle.api.GradleException;
import org.gradle.api.artifacts.transform.InputArtifact;
import org.gradle.api.artifacts.transform.InputArtifactDependencies;
import org.gradle.api.artifacts.transform.TransformAction;
import org.gradle.api.artifacts.transform.TransformOutputs;
import org.gradle.api.artifacts.transform.TransformParameters;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.provider.Provider;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class RemapAction implements TransformAction<TransformParameters.None> {
    @InputArtifact
    protected abstract Provider<FileSystemLocation> getInputArtifact();

    @InputArtifactDependencies
    protected abstract FileCollection getInputDependencies();

    @Override
    public void transform(TransformOutputs outputs) {
        System.out.print(":transforming quiltflower");
        Stopwatch stopwatch = Stopwatch.createStarted();

        File input = getInputArtifact().get().getAsFile();
        File output = outputs.file("remapped-" + input.getName());

        try {
            Map<String, String> patterns = new HashMap<>();
            patterns.put("org/jetbrains/java/decompiler", "juuxel/loomquiltflower/relocated/quiltflower");
            patterns.put("net/fabricmc/fernflower/api", "juuxel/loomquiltflower/relocated/quiltflowerapi");
            Set<String> excludes = Collections.singleton("net/fabricmc/fernflower/api/IFabricJavadocProvider");

            TinyRemapper remapper = TinyRemapper.newRemapper()
                .extraRemapper(new RelocationRemapper(patterns, excludes))
                .build();

            try (OutputConsumerPath consumer = new OutputConsumerPath.Builder(output.toPath()).build()) {
                consumer.addNonClassFiles(input.toPath());
                remapper.readInputs(input.toPath());

                for (File library : getInputDependencies()) {
                    remapper.readClassPath(library.toPath());
                }

                remapper.apply(consumer);
                stopwatch.stop();
                System.out.printf(" (%s ms)", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            }  finally {
                remapper.finish();
                System.out.println();
            }
        } catch (IOException e) {
            throw new GradleException("Could not remap dependency " + input.getName(), e);
        }
    }
}
