/*
 * This file is part of loom-vineflower, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2019-2020 FabricMC, 2021-2023 Juuz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package juuxel.vineflowerforloom.impl.bridge;

import juuxel.vineflowerforloom.impl.relocated.vineflower.main.extern.IFernflowerLogger;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Stack;

/**
 * This logger simply prints what each thread is doing
 * to the console in a machine parsable way.
 *
 * <p>Created by covers1624 on 11/02/19.
 */
// TODO: Make this simpler (see FernflowerLogger in Loom 0.10)
public class VfThreadIdLogger extends IFernflowerLogger {
    public final PrintStream stdOut;
    public final PrintStream stdErr;

    private final ThreadLocal<Stack<String>> workingClass = ThreadLocal.withInitial(Stack::new);
    private final ThreadLocal<Stack<String>> line = ThreadLocal.withInitial(Stack::new);

    public VfThreadIdLogger() {
        this(System.err, System.out);
    }

    public VfThreadIdLogger(PrintStream stdOut, PrintStream stdErr) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    @Override
    public void writeMessage(String message, Severity severity) {
        System.err.println(message);
    }

    @Override
    public void writeMessage(String message, Severity severity, Throwable t) {
        System.err.println(message);
        t.printStackTrace(System.err);
    }

    private void print() {
        Thread thread = Thread.currentThread();
        long id = thread.getId();

        if (line.get().isEmpty()) {
            System.out.println(MessageFormat.format("{0} :: waiting", id));
            return;
        }

        String line = this.line.get().peek();
        System.out.println(MessageFormat.format("{0} :: {1}", id, line).trim());
    }

    @Override
    public void startReadingClass(String className) {
        workingClass.get().push(className);
        line.get().push("Decompiling " + className);
        print();
    }

    @Override
    public void startClass(String className) {
        workingClass.get().push(className);
        line.get().push("Decompiling " + className);
        print();
    }

    @Override
    public void startMethod(String methodName) {
        // No need to print out methods
    }

    @Override
    public void endMethod() {
    }

    @Override
    public void endClass() {
        line.get().pop();
        workingClass.get().pop();
        print();
    }

    @Override
    public void startWriteClass(String className) {
        line.get().push("Writing " + className);
        print();
    }

    @Override
    public void endWriteClass() {
        line.get().pop();
        print();
    }

    @Override
    public void endReadingClass() {
        line.get().pop();
        workingClass.get().pop();
        print();
    }
}
