package juuxel.loomquiltflower.impl.bridge;

import juuxel.loomquiltflower.relocated.quiltflower.main.extern.IFernflowerLogger;
import net.fabricmc.loom.decompilers.fernflower.ThreadIDFFLogger;

public class QfThreadIdLogger extends IFernflowerLogger {
    private final ThreadIDFFLogger logger;

    public QfThreadIdLogger(ThreadIDFFLogger logger) {
        this.logger = logger;
    }

    @Override
    public void writeMessage(String s, Severity severity) {
        logger.writeMessage(s, EnumConversion.convert(severity, org.jetbrains.java.decompiler.main.extern.IFernflowerLogger.Severity.class));
    }

    @Override
    public void writeMessage(String s, Severity severity, Throwable throwable) {
        logger.writeMessage(s, EnumConversion.convert(severity, org.jetbrains.java.decompiler.main.extern.IFernflowerLogger.Severity.class), throwable);
    }

    @Override
    public void startReadingClass(String className) {
        logger.startReadingClass(className);
    }

    @Override
    public void endReadingClass() {
        logger.endReadingClass();
    }

    @Override
    public void startClass(String className) {
        logger.startClass(className);
    }

    @Override
    public void endClass() {
        logger.endClass();
    }

    @Override
    public void startMethod(String methodName) {
        logger.startMethod(methodName);
    }

    @Override
    public void endMethod() {
        logger.endMethod();
    }

    @Override
    public void startWriteClass(String className) {
        logger.startWriteClass(className);
    }

    @Override
    public void endWriteClass() {
        logger.endWriteClass();
    }
}
