package juuxel.vineflowerforloom.impl.module;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import juuxel.vineflowerforloom.impl.arch.ArchVineflowerDecompiler;
import juuxel.vineflowerforloom.impl.legacy.LegacyVineflowerDecompiler;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.task.ArchitecturyGenerateSourcesTask;
import org.gradle.api.Project;

public final class ArchLoomSetup implements VflModule {
    @Override
    public void setup(Project project, VineflowerExtension extension) {
        var loom = (LoomGradleExtensionAPI) project.getExtensions().getByName("loom");
        loom.addArchDecompiler(new ArchVineflowerDecompiler(LegacyVineflowerDecompiler.NAME, extension));
        loom.addArchDecompiler(new ArchVineflowerDecompiler(LegacyVineflowerDecompiler.OLD_NAME, extension));
    }

    @Override
    public Class<?> getDecompileTaskClass() {
        return ArchitecturyGenerateSourcesTask.class;
    }
}
