package juuxel.vineflowerforloom.api;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.vineflowerforloom.impl.DeprecatedQuiltflowerExtension;
import juuxel.vineflowerforloom.impl.PreferenceScanner;
import juuxel.vineflowerforloom.impl.VineflowerResolving;
import juuxel.vineflowerforloom.impl.ReflectionUtil;
import juuxel.vineflowerforloom.impl.module.VflModule;
import juuxel.vineflowerforloom.impl.VineflowerExtensionImpl;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.List;

/**
 * The Vineflower for Loom plugin.
 */
public final class VineflowerPlugin implements Plugin<Project> {
    private static final List<String> LOOMS = List.of(new String[] {
        "fabric-loom",
        "dev.architectury.loom",
        "org.quiltmc.loom",
        "gg.essential.loom", // https://github.com/Sk1erLLC/architectury-loom
        "babric-loom", // https://github.com/babric/fabric-loom
        "ornithe-loom", // https://github.com/OrnitheMC/ornithe-loom
    });
    private boolean applied = false;

    @Override
    public void apply(Project target) {
        // Use create to allow Gradle to decorate our extension
        var extension = target.getExtensions().create(VineflowerExtension.class, VineflowerExtension.NAME, VineflowerExtensionImpl.class, target);
        // Add the deprecated 'quiltflower' and 'loomQuiltflower' extensions
        target.getExtensions().create(QuiltflowerExtension.class, QuiltflowerExtension.NAME, DeprecatedQuiltflowerExtension.class, target, extension, QuiltflowerExtension.NAME);
        target.getExtensions().create(QuiltflowerExtension.class, "loomQuiltflower", DeprecatedQuiltflowerExtension.class, target, extension, "loomQuiltflower");

        // Scan for preferences declared in gradle.properties
        PreferenceScanner.scan(target, extension);

        // Setup resolving and runtime classpath
        VineflowerResolving.setup(target, (VineflowerExtensionImpl) extension);

        for (String loomId : LOOMS) {
            target.getPluginManager().withPlugin(loomId, p -> {
                if (applied) return;
                String moduleClass;

                if (isNewLoom()) {
                    moduleClass = "juuxel.vineflowerforloom.impl.module.Loom011Setup";
                } else if (isNewArchLoom()) {
                    moduleClass = "juuxel.vineflowerforloom.impl.module.ArchLoomSetup";
                } else if (isOldLoom()) {
                    moduleClass = "juuxel.vineflowerforloom.impl.module.OldLoomSetup";
                } else {
                    String message = "Vineflower for Loom is not supported on this Loom version!\nReplace with loom-quiltflower-mini: https://github.com/Juuxel/loom-quiltflower-mini";
                    target.getLogger().error(message);
                    throw new UnsupportedOperationException(message);
                }

                try {
                    VflModule module = VflModule.get(moduleClass);
                    module.setup(target, extension);
                    ((VineflowerExtensionImpl) extension).setActiveModule(module);
                } catch (ReflectiveOperationException e) {
                    throw new GradleException("Could not find Vineflower for Loom module " + moduleClass + ". Please report this!", e);
                }

                applied = true;
            });
        }

        target.afterEvaluate(p -> {
            if (!applied) {
                throw new GradleException("Vineflower for Loom requires Loom! (One of " + LOOMS + ")");
            }
        });
    }

    private static boolean isOldLoom() {
        return ReflectionUtil.classExists("net.fabricmc.loom.decompilers.fernflower.AbstractForkedFFExecutor");
    }

    private static boolean isNewArchLoom() {
        return ReflectionUtil.classExists("net.fabricmc.loom.api.decompilers.architectury.ArchitecturyLoomDecompiler");
    }

    private static boolean isNewLoom() {
        return ReflectionUtil.classExists("net.fabricmc.loom.api.decompilers.DecompilerOptions");
    }
}
