package juuxel.loomquiltflower;

import net.fabricmc.loom.decompilers.fernflower.AbstractFernFlowerDecompiler;
import net.fabricmc.loom.decompilers.fernflower.AbstractForkedFFExecutor;
import org.gradle.api.Project;

final class QuiltflowerDecompiler extends AbstractFernFlowerDecompiler {
    public QuiltflowerDecompiler(Project project) {
        super(project);
    }

    @Override
    public String name() {
        return "Quiltflower";
    }

    @Override
    public Class<? extends AbstractForkedFFExecutor> fernFlowerExecutor() {
        return QuiltflowerExecutor.class;
    }
}
