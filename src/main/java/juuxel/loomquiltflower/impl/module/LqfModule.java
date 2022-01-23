package juuxel.loomquiltflower.impl.module;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import net.fabricmc.loom.task.GenerateSourcesTask;
import org.gradle.api.Project;

public interface LqfModule {
    void setup(Project project, QuiltflowerExtension extension);

    default Class<?> getDecompileTaskClass() {
        return GenerateSourcesTask.class;
    }

    static LqfModule get(String className) throws ReflectiveOperationException {
        return (LqfModule) Class.forName(className).getConstructor().newInstance();
    }
}
