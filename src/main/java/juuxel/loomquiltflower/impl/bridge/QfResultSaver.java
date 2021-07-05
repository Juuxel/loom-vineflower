package juuxel.loomquiltflower.impl.bridge;

import juuxel.loomquiltflower.relocated.quiltflowerapi.IFabricResultSaver;

import java.util.jar.Manifest;

public class QfResultSaver implements IFabricResultSaver {
    private final net.fabricmc.fernflower.api.IFabricResultSaver parent;

    public QfResultSaver(net.fabricmc.fernflower.api.IFabricResultSaver parent) {
        this.parent = parent;
    }

    @Override
    public void saveClassEntry(String s, String s1, String s2, String s3, String s4, int[] ints) {
        parent.saveClassEntry(s, s1, s2, s3, s4, ints);
    }

    @Override
    public void saveFolder(String s) {
        parent.saveFolder(s);
    }

    @Override
    public void copyFile(String s, String s1, String s2) {
        parent.copyFile(s, s1, s2);
    }

    @Override
    public void saveClassFile(String s, String s1, String s2, String s3, int[] ints) {
        parent.saveClassFile(s, s1, s2, s3, ints);
    }

    @Override
    public void createArchive(String s, String s1, Manifest manifest) {
        parent.createArchive(s, s1, manifest);
    }

    @Override
    public void saveDirEntry(String s, String s1, String s2) {
        parent.saveDirEntry(s, s1, s2);
    }

    @Override
    public void copyEntry(String s, String s1, String s2, String s3) {
        parent.copyEntry(s, s1, s2, s3);
    }

    @Override
    public void saveClassEntry(String s, String s1, String s2, String s3, String s4) {
        parent.saveClassEntry(s, s1, s2, s3, s4);
    }

    @Override
    public void closeArchive(String s, String s1) {
        parent.closeArchive(s, s1);
    }
}
