package juuxel.vineflowerforloom.impl.arch;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import juuxel.vineflowerforloom.impl.legacy.LegacyVineflowerDecompiler;
import net.fabricmc.loom.api.decompilers.LoomDecompiler;
import net.fabricmc.loom.api.decompilers.architectury.ArchitecturyLoomDecompiler;
import org.gradle.api.Project;

public final class ArchVineflowerDecompiler implements ArchitecturyLoomDecompiler {
    private final String name;
    private final VineflowerExtension extension;

    public ArchVineflowerDecompiler(String name, VineflowerExtension extension) {
        this.name = name;
        this.extension = extension;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public LoomDecompiler create(Project project) {
        return new LegacyVineflowerDecompiler(project, name, extension);
    }
}
