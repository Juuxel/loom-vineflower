package juuxel.loomquiltflower.core;

import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;
import org.gradle.api.GradleException;

import java.io.File;
import java.io.IOException;

public final class Remapping {
    public static void remapQuiltflower(File input, File output, Iterable<File> inputDependencies) {
        try {
            TinyRemapper remapper = TinyRemapper.newRemapper()
                .extraRemapper(RelocationRemapper.createQuiltflowerRelocator())
                .build();

            try (OutputConsumerPath consumer = new OutputConsumerPath.Builder(output.toPath()).build()) {
                consumer.addNonClassFiles(input.toPath());
                remapper.readInputs(input.toPath());

                for (File library : inputDependencies) {
                    remapper.readClassPath(library.toPath());
                }

                remapper.apply(consumer);
            }  finally {
                remapper.finish();
                System.out.println();
            }
        } catch (IOException e) {
            throw new GradleException("Could not remap Quiltflower from " + input.getName() + " to " + output.getName(), e);
        }
    }
}
