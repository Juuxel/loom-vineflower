package juuxel.vineflowerforloom.impl.module;

import juuxel.vineflowerforloom.api.VineflowerExtension;
import net.fabricmc.loom.task.GenerateSourcesTask;
import org.gradle.api.Project;

public interface LqfModule {
    void setup(Project project, VineflowerExtension extension);

    default boolean shouldGenSourcesDependOnResolving() {
        return true;
    }

    default Class<?> getDecompileTaskClass() {
        return GenerateSourcesTask.class;
    }

    static LqfModule get(String className) throws ReflectiveOperationException {
        return (LqfModule) Class.forName(className).getConstructor().newInstance();
    }
}
