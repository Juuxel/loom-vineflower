package juuxel.loomquiltflower.impl.module;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.impl.QuiltflowerResolving;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Project;

public final class Loom011Setup implements LqfModule {
    @Override
    public void setup(Project project, QuiltflowerExtension extension) {
        LoomGradleExtensionAPI loom = (LoomGradleExtensionAPI) project.getExtensions().getByName("loom");
        loom.getDecompilerOptions().register("quiltflower", options -> {
            options.getDecompilerClassname().set("juuxel.loomquiltflower.impl.modern.QuiltflowerDecompiler");
            options.getOptions().putAll(extension.getPreferences().asStringMap());
            options.getClasspath().from(QuiltflowerResolving.getQuiltflowerJarFiles(project));
        });
    }

    @Override
    public boolean shouldGenSourcesDependOnResolving() {
        return false;
    }
}
