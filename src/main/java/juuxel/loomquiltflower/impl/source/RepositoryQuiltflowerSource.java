package juuxel.loomquiltflower.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.SelfResolvingDependency;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

public final class RepositoryQuiltflowerSource implements QuiltflowerSource {
    private static final String DEPENDENCY_BASE = "org.quiltmc:quiltflower:";
    private final Project project;
    private final Object dependencyNotation;
    private @Nullable Dependency dependency = null;
    private @Nullable File quiltflowerFile = null;

    public RepositoryQuiltflowerSource(Project project, Provider<String> version) {
        this.project = project;
        this.dependencyNotation = version.map(it -> DEPENDENCY_BASE + it);
    }

    public RepositoryQuiltflowerSource(Project project, Object dependencyNotation) {
        this.project = project;
        this.dependencyNotation = dependencyNotation;
    }

    private Dependency getDependency() {
        if (dependency == null) {
            dependency = project.getDependencies().create(unwrapPossibleProviders(dependencyNotation));
        }

        return dependency;
    }

    private static Object unwrapPossibleProviders(Object o) {
        while (o instanceof Provider<?>) {
            o = ((Provider<?>) o).get();
        }

        return o;
    }

    @Override
    public InputStream open() throws IOException {
        if (quiltflowerFile == null) {
            Dependency dependency = getDependency();
            Set<File> files;

            if (dependency instanceof SelfResolvingDependency) {
                files = ((SelfResolvingDependency) dependency).resolve();
            } else {
                Configuration configuration = project.getConfigurations().detachedConfiguration(dependency);
                files = configuration.resolve();
            }

            if (files.size() == 0) {
                throw new GradleException("Could not resolve Quiltflower " + dependency + " from repositories!");
            } else if (files.size() > 1) {
                throw new GradleException("Found more than 1 Quiltflower jar: " + files.stream().map(File::getAbsolutePath).collect(Collectors.joining(", ")));
            }

            quiltflowerFile = files.iterator().next();
        }

        return new FileInputStream(quiltflowerFile);
    }

    @Override
    public @Nullable String getProvidedVersion() {
        return getDependency().getVersion();
    }

    @Override
    public String toString() {
        return "fromDependency(" + dependencyNotation + ")";
    }
}
