package juuxel.loomquiltflower.impl.module;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.impl.arch.ArchQuiltflowerDecompiler;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Project;

public final class ArchLoomSetup implements LqfModule {
    @Override
    public void setup(Project project, QuiltflowerExtension extension) {
        ((LoomGradleExtensionAPI) project.getExtensions().getByName("loom")).addArchDecompiler(new ArchQuiltflowerDecompiler(extension));
    }
}
