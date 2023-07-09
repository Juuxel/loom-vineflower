package juuxel.vineflowerforloom.impl;

import org.gradle.api.Project;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

public final class DeprecationReporter {
    private static final WeakHashMap<Project, DeprecationReporter> reporters = new WeakHashMap<>();
    private final Set<String> reported = new HashSet<>();
    private final Project project;

    private DeprecationReporter(Project project) {
        this.project = project;
    }

    public static DeprecationReporter get(Project project) {
        return reporters.computeIfAbsent(project, DeprecationReporter::new);
    }

    public void reportRename(String oldName, String newName) {
        reportRename(oldName, newName, null);
    }

    public void reportRename(String oldName, String newName, @Nullable String kind) {
        String kindPrefix = kind != null ? kind + ' ' : "";
        if (reported.add(kindPrefix + oldName)) {
            String message = "%s'%s' has been replaced with '%s' and will be removed in a future release"
                .formatted(kindPrefix, oldName, newName);
            report(message);
        }
    }

    public void reportOther(String name, String kind) {
        if (reported.add(kind + " " + name)) {
            report("%s '%s' has been deprecated and will be removed in a future release"
                .formatted(kind, name));
        }
    }

    private void report(String message) {
        switch (project.getGradle().getStartParameter().getWarningMode()) {
            case Fail:
                throw new UnsupportedOperationException(message);
            case None:
                break;
            default:
                project.getLogger().warn(message);
        }
    }
}
