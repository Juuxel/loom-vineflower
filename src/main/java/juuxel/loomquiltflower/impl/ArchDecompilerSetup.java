package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;

public interface ArchDecompilerSetup {
    void setup(LoomGradleExtensionAPI loom, QuiltflowerExtension extension);
}
