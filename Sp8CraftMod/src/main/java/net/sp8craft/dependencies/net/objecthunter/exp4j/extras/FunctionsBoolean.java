/*
* Copyright 2016-2018 Federico Vera
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
package net.sp8craft.dependencies.net.objecthunter.exp4j.extras;

import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Function;
import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator;
import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operators;

import static net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator.*;

/**
 * This class contains a set of commonly used boolean functions and constants.
 *
 * <p>The boolean value rules are the same the we use in the operators:</p><ol>
 * <li>if the absolute value is less than {@link Operator#BOOLEAN_THRESHOLD} it
 * will be considered {@code false}.</li>
 * <li>if the absolute value is bigger or equal than
 * {@link Operator#BOOLEAN_THRESHOLD} it will be considered {@code true}</li>
 * <li>the recommended approach is to use functions or operators that return
 * boolean values, like the comparison operators or the
 * {@link FunctionsBoolean#TRUE} and {@link FunctionsBoolean#FALSE} constants
 * </li>
 * <li>the {@link FunctionsBoolean#TRUE} constant will always return 1.0 and
 * {@link FunctionsBoolean#FALSE} will always return 0.0
 * </ol>
 *
 * @see Operator#BOOLEAN_THRESHOLD
 * @see OperatorsComparison
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 * @since 0.6-riddler
 */
public final class FunctionsBoolean {

    /**
     * Logical function {@code NOT}
     * <pre>
     *     <b>a</b> | <b>~a</b>
     *     --+--
     *     0 | <i>1</i>
     *     1 | <i>0</i>
     * </pre>
     * @see Operators#NOT
     * @since 0.6-riddler
     */
    public static final Function NOT   = new Not();

    /**
     * Logical function {@code AND}
     * <pre>
     *     <b>a</b> | <b>b</b> | <b>a&amp;b</b>
     *     --+---+----
     *     0 | 0 |  <i>0</i>
     *     0 | 1 |  <i>0</i>
     *     1 | 0 |  <i>0</i>
     *     1 | 1 |  <i>1</i>
     * </pre>
     * @see Operators#AND
     * @since 0.6-riddler
     */
    public static final Function AND   = new And();

    /**
     * Logical function {@code OR}
     * <pre>
     *     <b>a</b> | <b>b</b> | <b>a|b</b>
     *     --+---+----
     *     0 | 0 |  <i>0</i>
     *     0 | 1 |  <i>1</i>
     *     1 | 0 |  <i>1</i>
     *     1 | 1 |  <i>1</i>
     * </pre>
     * @see Operators#OR
     * @since 0.6-riddler
     */
    public static final Function OR    = new Or();

    /**
     * Logical function {@code XOR}
     * <pre>
     *     <b>a</b> | <b>b</b> | <b>a^b</b>
     *     --+---+----
     *     0 | 0 |  <i>0</i>
     *     0 | 1 |  <i>1</i>
     *     1 | 0 |  <i>1</i>
     *     1 | 1 |  <i>0</i>
     * </pre>
     * @since 0.6-riddler
     */
    public static final Function XOR   = new Xor();

    /**
     * Logical function {@code NAND}
     * <pre>
     *     <b>a</b> | <b>b</b> | <b>!(a&amp;b)</b>
     *     --+---+-------
     *     0 | 0 |   <i>1</i>
     *     0 | 1 |   <i>1</i>
     *     1 | 0 |   <i>1</i>
     *     1 | 1 |   <i>0</i>
     * </pre>
     * @since 0.6-riddler
     */
    public static final Function NAND  = new Nand();

    /**
     * Logical function {@code NOR}
     * <pre>
     *     <b>a</b> | <b>b</b> | <b>!(a|b)</b>
     *     --+---+-------
     *     0 | 0 |   <i>1</i>
     *     0 | 1 |   <i>0</i>
     *     1 | 0 |   <i>0</i>
     *     1 | 1 |   <i>1</i>
     * </pre>
     * @since 0.6-riddler
     */
    public static final Function NOR   = new Nor();

    /**
     * Logical function {@code XNOR}
     * <pre>
     *     <b>a</b> | <b>b</b> | <b>!(a^b)</b>
     *     --+---+-------
     *     0 | 0 |   <i>1</i>
     *     0 | 1 |   <i>0</i>
     *     1 | 0 |   <i>0</i>
     *     1 | 1 |   <i>1</i>
     * </pre>
     * @since 0.6-riddler
     */
    public static final Function XNOR  = new Xnor();

    /**
     * Boolean value {@code false} -&gt; {@code 0.0}.
     * @since 0.6-riddler
     */
    public static final Function FALSE = new False();

    /**
     * Boolean value {@code true} -&gt; {@code 1.0}.
     * @since 0.6-riddler
     */
    public static final Function TRUE  = new True();

    /**
     * Array with all the available functions
     *
     * @return {@link Function} array
     * @see FunctionsBoolean#getFunction(String)
     * @see FunctionsBoolean#NOT
     * @see FunctionsBoolean#AND
     * @see FunctionsBoolean#OR
     * @see FunctionsBoolean#XOR
     * @see FunctionsBoolean#NAND
     * @see FunctionsBoolean#NOR
     * @see FunctionsBoolean#XNOR
     * @see FunctionsBoolean#FALSE
     * @see FunctionsBoolean#TRUE
     */
    public static Function[] getFunctions() {
        return new Function[] {NOT, AND, OR, XOR, NAND, NOR, XNOR, FALSE, TRUE};
    }

    /**
     * Get the function for a given name
     * @param name the name of the function
     * @return a Function instance
     * @see FunctionsBoolean#getFunctions()
     * @see FunctionsBoolean#NOT
     * @see FunctionsBoolean#AND
     * @see FunctionsBoolean#OR
     * @see FunctionsBoolean#XOR
     * @see FunctionsBoolean#NAND
     * @see FunctionsBoolean#NOR
     * @see FunctionsBoolean#XNOR
     * @see FunctionsBoolean#FALSE
     * @see FunctionsBoolean#TRUE
     */
    public static Function getFunction(final String name) {
        switch (name) {
            case "not"  : return NOT;
            case "and"  : return AND;
            case "or"   : return OR;
            case "xor"  : return XOR;
            case "nand" : return NAND;
            case "nor"  : return NOR;
            case "xnor" : return XNOR;
            case "false": return FALSE;
            case "true" : return TRUE;
            default:      return null;
        }
    }

    private FunctionsBoolean() {
        // Don't let anyone initialize this class
    }

    private static final class Not extends Function {
        private static final long serialVersionUID = 5548754689040328198L;
        Not() { super("not", 1); }
        @Override
        public double apply(double... args) {
            return (Math.abs(args[0]) >= BOOLEAN_THRESHOLD) ? 0 : 1;
        }
    }

    private static final class And extends Function {
        private static final long serialVersionUID = 610825426695578129L;
        And() { super("and", 2); }
        @Override
        public double apply(double... args) {
            final boolean a = Math.abs(args[0]) >= BOOLEAN_THRESHOLD;
            final boolean b = Math.abs(args[1]) >= BOOLEAN_THRESHOLD;
            return (a & b) ? 1 : 0;
        }
    }

    private static final class Or extends Function {
        private static final long serialVersionUID = -6688682627798315082L;
        Or() { super("or", 2); }
        @Override
        public double apply(double... args) {
            final boolean a = Math.abs(args[0]) >= BOOLEAN_THRESHOLD;
            final boolean b = Math.abs(args[1]) >= BOOLEAN_THRESHOLD;
            return (a | b) ? 1 : 0;
        }
    }

    private static final class Xor extends Function {
        private static final long serialVersionUID = 9162299011098408477L;
        Xor() { super("xor", 2); }
        @Override
        public double apply(double... args) {
            final boolean a = Math.abs(args[0]) >= BOOLEAN_THRESHOLD;
            final boolean b = Math.abs(args[1]) >= BOOLEAN_THRESHOLD;
            return (a ^ b) ? 1 : 0;
        }
    }

    private static final class Nand extends Function {
        private static final long serialVersionUID = -5696690796736889084L;
        Nand() { super("nand", 2); }
        @Override
        public double apply(double... args) {
            final boolean a = Math.abs(args[0]) >= BOOLEAN_THRESHOLD;
            final boolean b = Math.abs(args[1]) >= BOOLEAN_THRESHOLD;
            return (a & b) ? 0 : 1;
        }
    }

    private static final class Nor extends Function {
        private static final long serialVersionUID = 7443039549525626711L;
        Nor() { super("nor", 2); }
        @Override
        public double apply(double... args) {
            final boolean a = Math.abs(args[0]) >= BOOLEAN_THRESHOLD;
            final boolean b = Math.abs(args[1]) >= BOOLEAN_THRESHOLD;
            return (a | b) ? 0 : 1;
        }
    }

    private static final class Xnor extends Function {
        private static final long serialVersionUID = -7602049045810375102L;
        Xnor() { super("xnor", 2); }
        @Override
        public double apply(double... args) {
            final boolean a = Math.abs(args[0]) >= BOOLEAN_THRESHOLD;
            final boolean b = Math.abs(args[1]) >= BOOLEAN_THRESHOLD;
            return (a ^ b) ? 0 : 1;
        }
    }

    private static final class False extends Function {
        private static final long serialVersionUID = 8888653636237556280L;
        False() { super("false", 0); }
        @Override
        public double apply(double... args) {
            return 0;
        }
    }

    private static final class True extends Function {
        private static final long serialVersionUID = -5934349523714582684L;
        True() { super("true", 0); }
        @Override
        public double apply(double... args) {
            return 1;
        }
    }
}
