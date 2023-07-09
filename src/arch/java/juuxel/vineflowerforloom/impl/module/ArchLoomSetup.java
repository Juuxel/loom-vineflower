package juuxel.vineflowerforloom.impl.module;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import juuxel.vineflowerforloom.impl.arch.ArchQuiltflowerDecompiler;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.task.ArchitecturyGenerateSourcesTask;
import org.gradle.api.Project;

public final class ArchLoomSetup implements LqfModule {
    @Override
    public void setup(Project project, VineflowerExtension extension) {
        ((LoomGradleExtensionAPI) project.getExtensions().getByName("loom")).addArchDecompiler(new ArchQuiltflowerDecompiler(extension));
    }

    @Override
    public Class<?> getDecompileTaskClass() {
        return ArchitecturyGenerateSourcesTask.class;
    }
}
