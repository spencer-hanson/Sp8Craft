/*
 * Copyright 2014 Frank Asseg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sp8craft.dependencies.net.objecthunter.exp4j.function;

import java.io.Serializable;

import static net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text.l10n;

/**
 * A class representing a Function which can be used in an expression
 */
public abstract class Function implements Serializable {
    private static final long serialVersionUID = -4027601306719396290L;

    private final String name;

    private final int numArguments;

    private final boolean deterministic;

    /**
     * Create a new Function with a given name and number of arguments
     *
     * @param name the name of the Function
     * @param numArguments the number of arguments the function takes
     * @param deterministic {@code true} if the function is deterministic (i.e. can be simplified)
     * {@code false} otherwise
     */
    public Function(String name, int numArguments, boolean deterministic) {
        if (numArguments < 0) {
            throw new IllegalArgumentException(l10n(
                "The number of function arguments can not be less than 0 for '%s'", name
            ));
        }
        if (!isValidFunctionName(name)) {
            throw new IllegalArgumentException(l10n(
                "Function name '%s' is invalid", name
            ));
        }
        this.name = name;
        this.numArguments = numArguments;
        this.deterministic = deterministic;
    }

    /**
     * Create a new Function with a given name and number of arguments
     *
     * @param name the name of the Function
     * @param numArguments the number of arguments the function takes
     */
    public Function(String name, int numArguments) {
        this(name, numArguments, true);
    }

    /**
     * Create a new Function with a given name that takes a single argument
     *
     * @param name the name of the Function
     */
    public Function(String name) {
        this(name, 1);
    }

    /**
     * Tells if a function is deterministic, in this scenario basically applies to functions that
     * are not supposed to be simplified.
     *
     * @return {@code true} if the function is to be simplified (deterministic) and {@code false}
     * otherwise
     */
    public boolean isDeterministic() {
        return deterministic;
    }

    /**
     * Get the name of the Function
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the number of arguments for this function
     *
     * @return the number of arguments
     */
    public int getNumArguments() {
        return numArguments;
    }

    /**
     * Method that does the actual calculation of the function value given the arguments
     *
     * @param args the set of arguments used for calculating the function
     * @return the result of the function evaluation
     */
    public abstract double apply(double... args);

    /**
     * Tells if a function name is valid in the context of the expression.
     * This means that it's not {@code null} or empty and it only contains
     * {@code ASCII} chars, {@code _} or digits.
     *
     * @param name name to test
     * @return {@code true} if the function name can be used and {@code false}
     * otherwise
     */
    public static boolean isValidFunctionName(final String name) {
        if (name == null) {
            return false;
        }

        final int size = name.length();

        if (size == 0) {
            return false;
        }

        boolean status = true;
        for (int i = 0; i < size; i++) {
            final char c = name.charAt(i);
            if (Character.isLetter(c) || c == '_') {
                continue;
            } else if (Character.isDigit(c) && i > 0) {
                continue;
            }
            status &= false;
        }
        return status;
    }
}
