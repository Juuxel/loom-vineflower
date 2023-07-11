package juuxel.vineflowerforloom.impl;

import juuxel.vineflowerforloom.api.DecompilerBrand;
import juuxel.vineflowerforloom.impl.util.FlexVerComparator;

public final class TimeMachine {
    private static final String LAST_QUILTFLOWER_VERSION = "1.9.0";

    public static DecompilerBrand determineBrand(String version) {
        if (FlexVerComparator.compare(version, LAST_QUILTFLOWER_VERSION) <= 0) {
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

    public static String getOfficialRepository(DecompilerBrand brand, boolean snapshot) {
        return switch (brand) {
            case VINEFLOWER -> snapshot ? Repositories.OSSRH_SNAPSHOTS : Repositories.MAVEN_CENTRAL;
            case QUILTFLOWER -> snapshot ? Repositories.QUILT_SNAPSHOT : Repositories.QUILT_RELEASE;
        };
    }

    public static boolean isSnapshot(String version) {
        return version.endsWith("-SNAPSHOT");
    }
}
