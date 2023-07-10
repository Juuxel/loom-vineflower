package juuxel.vineflowerforloom.impl.source;

import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.vineflowerforloom.api.DecompilerBrand;
import juuxel.vineflowerforloom.impl.ReflectionUtil;
import juuxel.vineflowerforloom.impl.TimeMachine;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.artifacts.SelfResolvingDependency;
import org.gradle.api.provider.Provider;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class RepositoryDecompilerSource implements QuiltflowerSource {
    private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    private final Project project;
    private final Object dependencyNotation;
    private @Nullable Dependency dependency = null;
    private @Nullable File decompilerFile = null;
    private @Nullable String resolvedVersion = null;

    public RepositoryDecompilerSource(Project project, Provider<@Nullable DecompilerBrand> brand, Provider<String> version) {
        this.project = project;
        this.dependencyNotation = version.map(it -> {
            DecompilerBrand b = brand.getOrNull();
            if (b == null) TimeMachine.determineBrand(it);
            return TimeMachine.getDependencyCoordinates(b).asDependencyNotation() + ':' + it;
        });
    }

    public RepositoryDecompilerSource(Project project, Object dependencyNotation) {
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

    private void resolve() {
        if (decompilerFile == null) {
            Dependency dependency = getDependency();
            Set<File> files;
            String version = dependency.getVersion();

            if (dependency instanceof SelfResolvingDependency) {
                files = ((SelfResolvingDependency) dependency).resolve();
            } else {
                Configuration configuration = project.getConfigurations().detachedConfiguration(dependency);
                ResolvedConfiguration resolved = configuration.getResolvedConfiguration();
                @Nullable String baseVersion = dependency.getVersion();

                if (baseVersion != null && baseVersion.endsWith(SNAPSHOT_SUFFIX)) {
                    for (ResolvedArtifact artifact : resolved.getResolvedArtifacts()) {
                        var id = artifact.getModuleVersion().getId();
                        if (Objects.equals(id.getGroup(), dependency.getGroup()) &&
                            Objects.equals(id.getName(), dependency.getName())) {
                            var componentId = artifact.getId().getComponentIdentifier();
                            var timestamp = ReflectionUtil.maybeInvokeNoArgsMethod(componentId, "getTimestamp");
                            if (timestamp.isPresent()) {
                                version = baseVersion.substring(0, baseVersion.length() - SNAPSHOT_SUFFIX.length() + 1) + timestamp.get();
                            }
                            break;
                        }
                    }
                }

                files = resolved.getFiles();
            }

            if (files.size() == 0) {
                throw new GradleException("Could not resolve Vineflower " + dependency + " from repositories!");
            } else if (files.size() > 1) {
                throw new GradleException("Found more than 1 Vineflower jar: " + files.stream().map(File::getAbsolutePath).collect(Collectors.joining(", ")));
            }

            decompilerFile = files.iterator().next();
            resolvedVersion = version;
        }
    }

    @Override
    public InputStream open() throws IOException {
        resolve();
        return new FileInputStream(decompilerFile);
    }

    @Override
    public @Nullable String getProvidedVersion() {
        return getDependency().getVersion();
    }

    @Override
    public @Nullable String getResolvedVersion() {
        resolve();
        return resolvedVersion;
    }

    @Override
    public String toString() {
        return "fromDependency(" + dependencyNotation + ")";
    }
}
