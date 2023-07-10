package juuxel.vineflowerforloom.impl;

import juuxel.vineflowerforloom.api.DecompilerBrand;
import juuxel.vineflowerforloom.impl.util.FlexVerComparator;

public final class TimeMachine {
    private static final String LAST_QUILTFLOWER_VERSION = "1.9.0";

    public static DecompilerBrand determineBrand(String version) {
        if (FlexVerComparator.compare(LAST_QUILTFLOWER_VERSION, version) <= 0) {
            return DecompilerBrand.QUILTFLOWER;
        }

        return DecompilerBrand.VINEFLOWER;
    }

    public static DependencyCoordinates getDependencyCoordinates(DecompilerBrand brand) {
        return switch (brand) {
            case VINEFLOWER -> DependencyCoordinates.VINEFLOWER;
            case QUILTFLOWER -> DependencyCoordinates.QUILTFLOWER;
        };
    }

    public static String getOfficialRepository(DecompilerBrand brand) {
        return switch (brand) {
            case VINEFLOWER -> Repositories.MAVEN_CENTRAL;
            case QUILTFLOWER -> Repositories.QUILT_RELEASE;
        };
    }

}
