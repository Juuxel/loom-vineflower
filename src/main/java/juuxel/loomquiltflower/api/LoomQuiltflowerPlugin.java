package juuxel.loomquiltflower.api;

import juuxel.vineflowerforloom.api.VineflowerPlugin;
import juuxel.vineflowerforloom.impl.DeprecationReporter;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @deprecated Apply {@link juuxel.vineflowerforloom.api.VineflowerPlugin} instead.
 */
@Deprecated
public class LoomQuiltflowerPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        DeprecationReporter.get(target).reportRename(
            "io.github.juuxel.loom-quiltflower",
            "io.github.juuxel.loom-vineflower",
            "plugin"
        );
        target.getPluginManager().apply(VineflowerPlugin.class);
    }
}
