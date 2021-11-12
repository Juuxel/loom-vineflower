package juuxel.loomquiltflower.impl;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.api.QuiltflowerPreferences;
import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.api.SourceFactory;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class DeprecatedQuiltflowerExtension implements QuiltflowerExtension {
    private final Project project;
    private final QuiltflowerExtension parent;
    private boolean reported = false;

    public DeprecatedQuiltflowerExtension(Project project, QuiltflowerExtension parent) {
        this.project = project;
        this.parent = parent;
    }

    private void reportDeprecation() {
        if (!reported) {
            String message = "'loomQuiltflower' has been replaced by 'quiltflower' and will be removed in a future release";
            switch (project.getGradle().getStartParameter().getWarningMode()) {
                case Fail:
                    throw new UnsupportedOperationException(message);
                case None:
                    break;
                default:
                    project.getLogger().warn(message);
            }
            reported = true;
        }
    }

    @Override
    public Property<String> getQuiltflowerVersion() {
        reportDeprecation();
        return parent.getQuiltflowerVersion();
    }

    @Override
    public Property<QuiltflowerSource> getSource() {
        reportDeprecation();
        return parent.getSource();
    }

    @Override
    public SourceFactory getSourceFactory() {
        reportDeprecation();
        return parent.getSourceFactory();
    }

    @Override
    public QuiltflowerPreferences getPreferences() {
        reportDeprecation();
        return parent.getPreferences();
    }

    @Override
    public Property<Boolean> getAddToRuntimeClasspath() {
        reportDeprecation();
        return parent.getAddToRuntimeClasspath();
    }
}
