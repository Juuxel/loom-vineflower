package juuxel.loomquiltflower.impl.module

import juuxel.loomquiltflower.api.QuiltflowerExtension
import juuxel.loomquiltflower.impl.legacy.LegacyQuiltflowerDecompiler
import org.gradle.api.Project

final class OldLoomSetup implements LqfModule {
    @Override
    void setup(Project project, QuiltflowerExtension extension) {
        project.extensions.getByName("loom").addDecompiler(new LegacyQuiltflowerDecompiler(project, extension))
    }
}
