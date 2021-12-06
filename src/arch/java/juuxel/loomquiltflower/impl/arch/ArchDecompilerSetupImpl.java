package juuxel.loomquiltflower.impl.arch;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.impl.ArchDecompilerSetup;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;

public final class ArchDecompilerSetupImpl implements ArchDecompilerSetup {
    @Override
    public void setup(LoomGradleExtensionAPI loom, QuiltflowerExtension extension) {
        loom.addArchDecompiler(new ArchQuiltflowerDecompiler(extension));
    }
}
