package juuxel.loomquiltflower.impl.arch;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.impl.legacy.LegacyQuiltflowerDecompiler;
import net.fabricmc.loom.api.decompilers.LoomDecompiler;
import net.fabricmc.loom.api.decompilers.architectury.ArchitecturyLoomDecompiler;
import org.gradle.api.Project;

public final class ArchQuiltflowerDecompiler implements ArchitecturyLoomDecompiler {
    private final QuiltflowerExtension extension;

    public ArchQuiltflowerDecompiler(QuiltflowerExtension extension) {
        this.extension = extension;
    }

    @Override
    public String name() {
        return "Quiltflower";
    }

    @Override
    public LoomDecompiler create(Project project) {
        return new LegacyQuiltflowerDecompiler(project, extension);
    }
}
