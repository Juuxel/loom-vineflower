package juuxel.vineflowerforloom.impl.module;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import juuxel.vineflowerforloom.impl.VineflowerResolving;
import juuxel.vineflowerforloom.impl.task.ResolveVineflower;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.api.decompilers.DecompilerOptions;
import org.gradle.api.Action;
import org.gradle.api.Project;

import java.util.function.Function;

public final class Loom011Setup implements VflModule {
    @Override
    public void setup(Project project, VineflowerExtension extension) {
        LoomGradleExtensionAPI loom = (LoomGradleExtensionAPI) project.getExtensions().getByName("loom");
        Function<String, Action<DecompilerOptions>> decompilerConfig = suffix -> options -> {
            options.getDecompilerClassName().set("juuxel.vineflowerforloom.impl.modern.VineflowerDecompiler" + suffix);
            options.getOptions().putAll(extension.getPreferences().asStringMap());
            options.getClasspath().from(VineflowerResolving.getResolveVineflowerTask(project).flatMap(ResolveVineflower::getRemappedOutput));
            options.getClasspath().builtBy(VineflowerResolving.getResolveVineflowerTask(project));
        };
        loom.getDecompilerOptions().register("vineflower", decompilerConfig.apply(""));
        loom.getDecompilerOptions().register("quiltflower", decompilerConfig.apply("$DeprecatedQuiltflower"));
    }

    @Override
    public boolean shouldGenSourcesDependOnResolving() {
        return false;
    }
}
