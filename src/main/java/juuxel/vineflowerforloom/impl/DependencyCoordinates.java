package juuxel.vineflowerforloom.impl;

public record DependencyCoordinates(String group, String artifact) {
    public static final DependencyCoordinates VINEFLOWER = new DependencyCoordinates("org.vineflower", "vineflower");
    public static final DependencyCoordinates QUILTFLOWER = new DependencyCoordinates("org.quiltmc", "quiltflower");

    public String asUrlPart() {
        return group.replace('.', '/') + '/' + artifact;
    }

    public String asDependencyNotation() {
        return group + ':' + artifact;
    }
}
