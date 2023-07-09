package juuxel.loomquiltflower.api;

import juuxel.vineflowerforloom.api.VineflowerPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @deprecated Apply {@link juuxel.vineflowerforloom.api.VineflowerPlugin} instead.
 */
@Deprecated
public class LoomQuiltflowerPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        // TODO: Report deprecation
        target.getPluginManager().apply(VineflowerPlugin.class);
    }
}
