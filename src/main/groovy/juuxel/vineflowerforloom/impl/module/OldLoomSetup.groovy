package juuxel.vineflowerforloom.impl.module

import juuxel.vineflowerforloom.api.VineflowerExtension
import juuxel.vineflowerforloom.impl.legacy.LegacyQuiltflowerDecompiler
import org.gradle.api.Project

final class OldLoomSetup implements LqfModule {
    @Override
    void setup(Project project, VineflowerExtension extension) {
        project.extensions.getByName("loom").addDecompiler(new LegacyQuiltflowerDecompiler(project, extension))
    }
}
