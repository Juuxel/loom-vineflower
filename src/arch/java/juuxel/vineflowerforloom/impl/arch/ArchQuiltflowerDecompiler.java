package juuxel.vineflowerforloom.impl.arch;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import juuxel.vineflowerforloom.impl.legacy.LegacyVineflowerDecompiler;
import net.fabricmc.loom.api.decompilers.LoomDecompiler;
import net.fabricmc.loom.api.decompilers.architectury.ArchitecturyLoomDecompiler;
import org.gradle.api.Project;

public final class ArchQuiltflowerDecompiler implements ArchitecturyLoomDecompiler {
    private final VineflowerExtension extension;

    public ArchQuiltflowerDecompiler(VineflowerExtension extension) {
        this.extension = extension;
    }

    @Override
    public String name() {
        return "Quiltflower";
    }

    @Override
    public LoomDecompiler create(Project project) {
        return new LegacyVineflowerDecompiler(project, extension);
    }
}
