package juuxel.vineflowerforloom.impl;

import juuxel.loomquiltflower.api.QuiltflowerExtension;
import juuxel.loomquiltflower.api.QuiltflowerPreferences;
import juuxel.loomquiltflower.api.QuiltflowerSource;
import juuxel.loomquiltflower.api.SourceFactory;
import juuxel.vineflowerforloom.api.DecompilerBrand;
import juuxel.vineflowerforloom.api.DecompilerSource;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class DeprecatedQuiltflowerExtension implements QuiltflowerExtension {
    private final Project project;
    private final QuiltflowerExtension parent;
    private final String name;

    public DeprecatedQuiltflowerExtension(Project project, QuiltflowerExtension parent, String name) {
        this.project = project;
        this.parent = parent;
        this.name = name;
    }

    private void reportDeprecation() {
        DeprecationReporter.get(project).reportRename(name, "vineflower", "extension");
    }

    @Override
    public Property<String> getToolVersion() {
        reportDeprecation();
        return parent.getToolVersion();
    }

    @Override
    public Property<DecompilerSource> getToolSource() {
        reportDeprecation();
        return parent.getToolSource();
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
    public void fromQuiltMaven() {
        reportDeprecation();
        parent.fromQuiltMaven();
    }

    @Override
    public void fromQuiltSnapshotMaven() {
        reportDeprecation();
        parent.fromQuiltSnapshotMaven();
    }

    @Override
    public void fromLatestSnapshot() {
        reportDeprecation();
        parent.fromLatestSnapshot();
    }

    @Override
    public void fromLatestQuiltSnapshot() {
        reportDeprecation();
        parent.fromLatestQuiltSnapshot();
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

    @Override
    public Property<DecompilerBrand> getBrand() {
        reportDeprecation();
        return parent.getBrand();
    }
}
