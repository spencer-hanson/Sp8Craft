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

import java.math.BigInteger;
import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Function;
import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Functions;
import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator;

/**
 * This class contains a small set of useful functions that don't really fit in
 * the other categories.
 *
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 * @since 0.6-riddler
 */
public final class FunctionsMisc {
    /**
     * Equality function.
     *
     * <p>The test is performed using a threshold given by
     * {@link Operator#BOOLEAN_THRESHOLD}.</p>
     * <p>This function has two argument {@code equal(a, b)} where:</p>
     * <ul>
     * <li><code><b>a</b></code>: First value to test</li>
     * <li><code><b>b</b></code>: Second value to test</li>
     * </ul>
     *
     * @see Operator#BOOLEAN_THRESHOLD
     * @since 0.6-riddler
     */
    public static final Function EQUAL = new Equals();

    /**
     * Branching function.
     * <p>This function has three argument {@code if(exp, v_true, v_false)}
     * where:</p>
     * <ul>
     * <li><code><b>exp</b></code>: Boolean expression</li>
     * <li><code><b>v_true</b></code>: Value if true</li>
     * <li><code><b>v_false</b></code>: Value if false</li>
     * </ul>
     *
     * @see FunctionsBoolean
     * @see OperatorsComparison
     * @see Operator#BOOLEAN_THRESHOLD
     * @see OperatorsComparison#OP_EQU
     * @see OperatorsComparison#OP_GOE
     * @see OperatorsComparison#OP_GT
     * @see OperatorsComparison#OP_LOE
     * @see OperatorsComparison#OP_LT
     * @see OperatorsComparison#OP_NEQ
     * @since 0.6-riddler
     */
    public static final Function IF = new If();

    /**
     * Retrieves the value of {@link Double#POSITIVE_INFINITY}.
     *
     * @see Double#POSITIVE_INFINITY
     * @see Double#NEGATIVE_INFINITY
     * @since 0.8-riddler
     */
    public static final Function INFINITY = new Infinity();

    /**
     * Tells if a number is {@link Double#NaN}.
     *
     * @see Double#isNaN(double)
     * @since 0.8-riddler
     */
    public static final Function IS_NAN = new IsNaN();

    /**
     * Returns the smallest (closest to negative infinity) of two numbers.
     *
     * @see Math#min(double, double)
     * @since 0.8-riddler
     */
    public static final Function MIN = new Min();

    /**
     * Returns the largest (closest to positive infinity) of two numbers.
     *
     * @see Math#max(double, double)
     * @since 0.8-riddler
     */
    public static final Function MAX = new Max();

    /**
     * Returns the Greatest Common Denominator of two numbers.
     *
     * <p>The numbers WILL be rounded using {@link Math#round(double)} before
     * the analysis.</p>
     * <p>If the resulting value is out of the range of the {@code long} type,
     * then an {@code ArithmeticException} is thrown.</p>
     *
     * @see Functions#FLOOR
     * @see Functions#CEIL
     * @see FunctionsMisc#ROUND
     * @see FunctionsMisc#LCM
     * @since 0.8-riddler
     */
    public static final Function GCD = new GCD();

    /**
     * Returns the Least Common Multiple of two numbers.
     *
     * <p>The numbers WILL be rounded using {@link Math#round(double)} before
     * the analysis.</p>
     * <p>If the resulting value is out of the range of the {@code long} type,
     * then an {@code ArithmeticException} is thrown.</p>
     *
     * @see Functions#FLOOR
     * @see Functions#CEIL
     * @see FunctionsMisc#ROUND
     * @see FunctionsMisc#GCD
     * @since 0.8-riddler
     */
    public static final Function LCM = new LCM();

    /**
     * Rounds to closest integer.
     *
     * @see Functions#FLOOR
     * @see Functions#CEIL
     * @see Math#round(double)
     * @since 0.8-riddler
     */
    public static final Function ROUND = new Round();

    /**
     * Converts from degrees to radians.
     *
     * @see Functions#RAD2DEG
     * @see Math#toRadians(double)
     * @since 0.9-riddler
     */
    public static final Function DEG2RAD = new Deg2Rad();

    /**
     * Converts from radians to degrees.
     *
     * @see Functions#DEG2RAD
     * @see Math#toDegrees(double)
     * @since 0.9-riddler
     */
    public static final Function RAD2DEG = new Rad2Deg();

    /**
     * This is the threshold used to consider values equal, that is, if two
     * values {@code a} and {@code b} are separated by less than this threshold
     * they will be considered to be equal, it has a default value of {@value}
     */
    public static final double EQUALITY_THRESHOLD = Operator.BOOLEAN_THRESHOLD;

    /**
     * Array with all the available functions.
     *
     * @return {@link Function} array
     *
     * @see FunctionsMisc#getFunction(String)
     * @see FunctionsMisc#EQUAL
     * @see FunctionsMisc#IF
     * @see FunctionsMisc#INFINITY
     * @see FunctionsMisc#IS_NAN
     * @see FunctionsMisc#MIN
     * @see FunctionsMisc#MAX
     * @see FunctionsMisc#GCD
     * @see FunctionsMisc#LCM
     * @see FunctionsMisc#ROUND
     * @see FunctionsMisc#DEG2RAD
     * @see FunctionsMisc#RAD2DEG
     */
    public static Function[] getFunctions() {
        return new Function[]{
            EQUAL, IF, INFINITY, IS_NAN, MIN, MAX, GCD, LCM, ROUND, DEG2RAD, RAD2DEG
        };
    }

    /**
     * Get the function for a given name.
     *
     * @param name the name of the function
     * @return a Function instance
     *
     * @see FunctionsMisc#getFunctions()
     * @see FunctionsMisc#EQUAL
     * @see FunctionsMisc#IF
     * @see FunctionsMisc#INFINITY
     * @see FunctionsMisc#IS_NAN
     * @see FunctionsMisc#MIN
     * @see FunctionsMisc#MAX
     * @see FunctionsMisc#GCD
     * @see FunctionsMisc#LCM
     * @see FunctionsMisc#ROUND
     * @see FunctionsMisc#DEG2RAD
     * @see FunctionsMisc#RAD2DEG
     */
    public static Function getFunction(final String name) {
        switch (name) {
            case "equal"   : return EQUAL;
            case "if"      : return IF;
            case "inf"     : return INFINITY;
            case "isnan"   : return IS_NAN;
            case "min"     : return MIN;
            case "max"     : return MAX;
            case "gcd"     : return GCD;
            case "lcm"     : return LCM;
            case "round"   : return ROUND;
            case "degtorad": return DEG2RAD;
            case "radtodeg": return RAD2DEG;
            default:         return null;
        }
    }

    private FunctionsMisc() {
        // Don't let anyone initialize this class
    }

    private static final class Equals extends Function {
        private static final long serialVersionUID = 2388827649030518290L;
        Equals() { super("equal", 2); }
        @Override
        public double apply(double... args) {
            final double  a = args[0];
            final double  b = args[1];
            return Math.abs(a - b) < EQUALITY_THRESHOLD ? 1 : 0;
        }
    }

    private static final class If extends Function {
        private static final long serialVersionUID = 3865326455639650003L;
        If() { super("if", 3); }
        @Override
        public double apply(double... args) {
            final boolean a = args[0] >= EQUALITY_THRESHOLD;
            final double  t = args[1];
            final double  f = args[2];
            return a ? t : f;
        }
    }

    private static final class Infinity extends Function {
        private static final long serialVersionUID = 6249177625376818393L;
        Infinity() { super("inf", 0); }
        @Override
        public double apply(double... args) {
            return Double.POSITIVE_INFINITY;
        }
    }

    private static final class IsNaN extends Function {
        private static final long serialVersionUID = 2987603422726499329L;
        IsNaN() { super("isnan", 1); }
        @Override
        public double apply(double... args) {
            final double val = args[0];
            return Double.isNaN(val) ? 1.0 : 0.0;
        }
    }

    private static final class Min extends Function {
        private static final long serialVersionUID = -8343244242397439087L;
        Min() { super("min", 2); }
        @Override
        public double apply(double... args) {
            final double v1 = args[0];
            final double v2 = args[1];
            return Math.min(v1, v2);
        }
    }

    private static final class Max extends Function {
        private static final long serialVersionUID = 426041154853511222L;
        Max() { super("max", 2); }
        @Override
        public double apply(double... args) {
            final double v1 = args[0];
            final double v2 = args[1];
            return Math.max(v1, v2);
        }
    }

    private static final class GCD extends Function {
        private static final long serialVersionUID = -6539620489548306830L;
        GCD() { super("gcd", 2); }
        @Override
        public double apply(double... args) {
            final BigInteger v1 = BigInteger.valueOf(Math.round(args[0]));
            final BigInteger v2 = BigInteger.valueOf(Math.round(args[1]));
            return v1.gcd(v2).longValueExact();
        }
    }

    private static final class LCM extends Function {
        private static final long serialVersionUID = -6539620489548306830L;
        LCM() { super("lcm", 2); }
        @Override
        public double apply(double... args) {
            final long a = Math.round(args[0]);
            final long b = Math.round(args[1]);
            final BigInteger v1 = BigInteger.valueOf(a);
            final BigInteger v2 = BigInteger.valueOf(b);
            final double gcd = v1.gcd(v2).longValueExact();
            return Math.abs(a * (b / gcd ));
        }
    }

    private static final class Round extends Function {
        private static final long serialVersionUID = -6539620489548306830L;
        Round() { super("round", 1); }
        @Override
        public double apply(double... args) {
            final double a = args[0];
            return Math.round(a);
        }
    }

    private static final class Deg2Rad extends Function {
        private static final long serialVersionUID = -6539620489548306830L;
        Deg2Rad() { super("degtorad", 1); }
        @Override
        public double apply(double... args) {
            final double x = args[0];
            return Math.toRadians(x);
        }
    }

    private static final class Rad2Deg extends Function {
        private static final long serialVersionUID = -6539620489548306830L;
        Rad2Deg() { super("radtodeg", 1); }
        @Override
        public double apply(double... args) {
            final double x = args[0];
            return Math.toDegrees(x);
        }
    }
}
