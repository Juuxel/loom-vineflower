package juuxel.vineflowerforloom.impl.module;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import juuxel.vineflowerforloom.impl.arch.ArchVineflowerDecompiler;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.task.ArchitecturyGenerateSourcesTask;
import org.gradle.api.Project;

public final class ArchLoomSetup implements VflModule {
    @Override
    public void setup(Project project, VineflowerExtension extension) {
        var loom = (LoomGradleExtensionAPI) project.getExtensions().getByName("loom");
        loom.addArchDecompiler(new ArchVineflowerDecompiler("Vineflower", extension));
        // TODO: Report deprecations
        loom.addArchDecompiler(new ArchVineflowerDecompiler("Quiltflower", extension));
    }

    @Override
    public Class<?> getDecompileTaskClass() {
        return ArchitecturyGenerateSourcesTask.class;
    }
}
