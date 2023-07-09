/*
 * This file is part of loom-quiltflower, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2019-2020 FabricMC, 2021 Juuz
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

package juuxel.vineflowerforloom.impl.legacy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Entry point for Forked FernFlower task.
 * Takes one parameter, a single file, each line is treated as command line input.
 * Forces one input file.
 * Forces one output file using '-o=/path/to/output'
 * Created by covers1624 on 11/02/19.
 * <p>Extending classes MUST have a standard "public static void main(args)".
 * They may then call AbstractForkedFFExecutor#decompile for it to use the overridden AbstractForkedFFExecutor#runFF
 * </p>
 */
public abstract class AbstractForkedFFExecutor {
    public static void decompile(String[] args, AbstractForkedFFExecutor ffExecutor) {
        Map<String, Object> options = new HashMap<>();
        File input = null;
        File output = null;
        File lineMap = null;
        File mappings = null;
        List<File> libraries = new ArrayList<>();

        boolean isOption = true;

        for (String arg : args) {
            if (isOption && arg.length() > 5 && arg.charAt(0) == '-' && arg.charAt(4) == '=') {
                String value = arg.substring(5);

                if ("true".equalsIgnoreCase(value)) {
                    value = "1";
                } else if ("false".equalsIgnoreCase(value)) {
                    value = "0";
                }

                options.put(arg.substring(1, 4), value);
            } else {
                isOption = false;

                if (arg.startsWith("-e=")) {
                    libraries.add(new File(arg.substring(3)));
                } else if (arg.startsWith("-o=")) {
                    if (output != null) {
                        throw new RuntimeException("Unable to set more than one output.");
                    }

                    output = new File(arg.substring(3));
                } else if (arg.startsWith("-l=")) {
                    if (lineMap != null) {
                        throw new RuntimeException("Unable to set more than one lineMap file.");
                    }

                    lineMap = new File(arg.substring(3));
                } else if (arg.startsWith("-m=")) {
                    if (mappings != null) {
                        throw new RuntimeException("Unable to use more than one mappings file.");
                    }

                    mappings = new File(arg.substring(3));
                } else {
                    if (input != null) {
                        throw new RuntimeException("Unable to set more than one input.");
                    }

                    input = new File(arg);
                }
            }
        }

        Objects.requireNonNull(input, "Input not set.");
        Objects.requireNonNull(output, "Output not set.");
        Objects.requireNonNull(mappings, "Mappings not set.");

        ffExecutor.runFF(options, libraries, input, output, lineMap, mappings);
    }

    public abstract void runFF(Map<String, Object> options, List<File> libraries, File input, File output, File lineMap, File mappings);
}
