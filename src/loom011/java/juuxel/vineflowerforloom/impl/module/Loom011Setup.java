package juuxel.vineflowerforloom.impl.module;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.vineflowerforloom.impl.VineflowerResolving;
import juuxel.vineflowerforloom.impl.module.LqfModule;
import juuxel.vineflowerforloom.impl.task.ResolveVineflower;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Project;

public final class Loom011Setup implements LqfModule {
    @Override
    public void setup(Project project, QuiltflowerExtension extension) {
        LoomGradleExtensionAPI loom = (LoomGradleExtensionAPI) project.getExtensions().getByName("loom");
        loom.getDecompilerOptions().register("quiltflower", options -> {
            options.getDecompilerClassName().set("juuxel.vineflowerforloom.impl.modern.QuiltflowerDecompiler");
            options.getOptions().putAll(extension.getPreferences().asStringMap());
            options.getClasspath().from(VineflowerResolving.getResolveVineflowerTask(project).flatMap(ResolveVineflower::getRemappedOutput));
            options.getClasspath().builtBy(VineflowerResolving.getResolveVineflowerTask(project));
        });
    }

    @Override
    public boolean shouldGenSourcesDependOnResolving() {
        return false;
    }
}
