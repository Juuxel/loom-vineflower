package juuxel.loomquiltflower.impl.bridge;

import juuxel.loomquiltflower.impl.relocated.quiltflower.main.extern.IFernflowerLogger;

import java.io.PrintWriter;

public class SimpleLogger extends IFernflowerLogger {
    private final PrintWriter out;
    private final PrintWriter err;

    public SimpleLogger(PrintWriter out, PrintWriter err) {
        this.out = out;
        this.err = err;
    }

    public SimpleLogger(PrintWriter writer) {
        this(writer, writer);
    }

    private boolean acceptsSeverity(Severity severity) {
        return severity == Severity.ERROR || severity == Severity.WARN;
    }

    @Override
    public void writeMessage(String s, Severity severity) {
        if (acceptsSeverity(severity)) {
            err.print(severity.prefix);
            err.println(s);
        }
    }

    @Override
    public void writeMessage(String s, Severity severity, Throwable throwable) {
        // Not sure if this happens, but let's do it anyway
        if (throwable == null) {
            writeMessage(s, severity);
            return;
        }

        if (acceptsSeverity(severity)) {
            err.print(severity.prefix);
            err.print(s + ": ");
            throwable.printStackTrace();
        }
    }

    @Override
    public void startReadingClass(String className) {
        out.println("Decompiling " + className);
    }

    @Override
    public void startClass(String className) {
        out.println("Decompiling " + className);
    }
}
