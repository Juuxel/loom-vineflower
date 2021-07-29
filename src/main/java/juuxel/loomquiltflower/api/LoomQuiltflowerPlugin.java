package juuxel.loomquiltflower.api;

import juuxel.loomquiltflower.impl.QuiltflowerExtensionImpl;
import juuxel.loomquiltflower.impl.QuiltflowerDecompiler;
import net.fabricmc.loom.api.decompilers.LoomDecompiler;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class LoomQuiltflowerPlugin implements Plugin<Project> {
    private static final List<String> LOOMS = Arrays.asList("fabric-loom", "dev.architectury.loom");
    private boolean applied = false;

    @Override
    public void apply(Project target) {
        QuiltflowerExtensionImpl extension = new QuiltflowerExtensionImpl(target);
        target.getExtensions().add(QuiltflowerExtension.class, "quiltflower", extension);

        for (String loomId : LOOMS) {
            target.getPluginManager().withPlugin(loomId, p -> {
                try {
                    Object loom = target.getExtensions().getByName("loom");
                    Method addDecompiler = loom.getClass().getMethod("addDecompiler", LoomDecompiler.class);
                    addDecompiler.invoke(loom, new QuiltflowerDecompiler(target, extension));
                    applied = true;
                } catch (ReflectiveOperationException e) {
                    throw new GradleException("Could not add Quiltflower decompiler", e);
                }
            });
        }

        target.afterEvaluate(p -> {
            if (!applied) {
                throw new GradleException("loom-quiltflower requires Loom! (One of " + LOOMS + ")");
            }
        });
    }
}
