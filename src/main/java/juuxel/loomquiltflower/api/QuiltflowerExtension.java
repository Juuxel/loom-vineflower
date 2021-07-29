package juuxel.loomquiltflower.api;

import org.gradle.api.provider.Property;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface QuiltflowerExtension {
    Property<String> getQuiltflowerVersion();
    Property<QuiltflowerSource> getQuiltflowerSource();

    QuiltflowerSource fromFile(Object path);
    QuiltflowerSource fromUrl(Object url);
    QuiltflowerSource fromProjectRepositories();
    QuiltflowerSource fromDependency(Object dependencyNotation);
    QuiltflowerSource fromQuiltMaven();
}
