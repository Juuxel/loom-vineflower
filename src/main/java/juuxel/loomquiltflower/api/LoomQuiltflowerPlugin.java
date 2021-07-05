package juuxel.loomquiltflower.api;

import juuxel.loomquiltflower.impl.QuiltflowerDecompiler;
import net.fabricmc.loom.LoomGradleExtension;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Arrays;
import java.util.List;

public class LoomQuiltflowerPlugin implements Plugin<Project> {
    private static final List<String> LOOMS = Arrays.asList("fabric-loom", "dev.architectury.loom");
    private boolean applied = false;

    @Override
    public void apply(Project target) {
        LoomQuiltflowerExtension extension = new LoomQuiltflowerExtension(target);
        target.getExtensions().add("loomQuiltflower", extension);

        for (String loomId : LOOMS) {
            target.getPluginManager().withPlugin(loomId, p -> {
                LoomGradleExtension loom = target.getExtensions().getByType(LoomGradleExtension.class);
                loom.addDecompiler(new QuiltflowerDecompiler(target, extension));
                applied = true;
            });
        }

        target.afterEvaluate(p -> {
            if (!applied) {
                throw new GradleException("loom-quiltflower requires Loom! (One of " + LOOMS + ")");
            }
        });
    }
}
