package juuxel.loomquiltflower.impl.module;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import org.gradle.api.Project;

public interface LqfModule {
    void setup(Project project, QuiltflowerExtension extension);

    default boolean shouldGenSourcesDependOnResolving() {
        return true;
    }

    static LqfModule get(String className) throws ReflectiveOperationException {
        return (LqfModule) Class.forName(className).getConstructor().newInstance();
    }
}
