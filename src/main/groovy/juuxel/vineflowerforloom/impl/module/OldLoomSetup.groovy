package juuxel.vineflowerforloom.impl.module

import juuxel.vineflowerforloom.api.VineflowerExtension
import juuxel.vineflowerforloom.impl.legacy.LegacyVineflowerDecompiler
import org.gradle.api.Project

final class OldLoomSetup implements VflModule {
    @Override
    void setup(Project project, VineflowerExtension extension) {
        def loom = project.extensions.getByName("loom")
        loom.addDecompiler(new LegacyVineflowerDecompiler(project, LegacyVineflowerDecompiler.NAME, extension))
        loom.addDecompiler(new LegacyVineflowerDecompiler(project, LegacyVineflowerDecompiler.OLD_NAME, extension))
    }
}
