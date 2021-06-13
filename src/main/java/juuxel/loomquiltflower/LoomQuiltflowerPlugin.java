package juuxel.loomquiltflower;

import juuxel.loomquiltflower.api.LoomQuiltflowerExtension;
import net.fabricmc.loom.LoomGradleExtension;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Arrays;
import java.util.List;

public class LoomQuiltflowerPlugin implements Plugin<Project> {
    private static final List<String> LOOMS = Arrays.asList("fabric-loom", "dev.architectury.loom");

    @Override
    public void apply(Project target) {
        if (LOOMS.stream().noneMatch(target.getPluginManager()::hasPlugin)) {
            throw new GradleException("Loom not found! Loom must be applied *before* loom-quiltflower.");
        }

        LoomQuiltflowerExtension extension = new LoomQuiltflowerExtension(target);
        target.getExtensions().add("loomQuiltflower", extension);

        LoomGradleExtension loom = target.getExtensions().getByType(LoomGradleExtension.class);
        loom.addDecompiler(new QuiltflowerDecompiler(target, extension));
    }
}
