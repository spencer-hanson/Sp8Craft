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
package net.sp8craft.dependencies.net.objecthunter.exp4j.operator;


import static net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator.*;
import static net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text.l10n;

/**
 * This class implements all of the built-in operators both arithmetic and
 * boolean.
 * <p>Boolean values will be treated as follows:</p><ol>
 * <li>if the absolute value is less than 1e-12 it will be considered
 * {@code false}. About this... I know is not the ideal approach, but we have a
 * LOT of arithmetic "hacks" all around some projects and this behavior is
 * actually expected.</li>
 * <li>if the absolute value is bigger or equal than 1e-12 it will be considered
 * {@code true}</li>
 * <li>the boolean results will <b>always</b> be {@code 1.0} for {@code true}
 * and {@code 0.0} for {@code false}</li>
 * <li>boolean operations will always have lower precedence than arithmetic
 * operations (i.e. they will be evaluated last)</li>
 * <li>The precedence (order) of the boolean operators will always be ¬ &amp;
 * | (in accordance to the analogy with arithmetic operators), so that:<pre>
 *             a &amp; b| b &amp; ¬c -&gt; ((a &amp; b) | (b &amp; (¬c)))</pre>
 * </li>
 * </ol>
 */
public final class Operators {
    private static final Operator ADDITION        = new OpAdd();
    private static final Operator ADDITION_UN     = new OpAddUnary();
    private static final Operator SUBTRACTION     = new OpMinus();
    private static final Operator SUBTRACTION_UN  = new OpMinusUnary();
    private static final Operator MUTLIPLICATION  = new OpMultiply();
    private static final Operator DIVISION        = new OpDivide();
    private static final Operator MODULO          = new OpModulo();
    private static final Operator POWER           = new OpPower();

    private static final Operator AND             = new OpAnd();
    private static final Operator OR              = new OpOr();
    private static final Operator NOT             = new OpNot();

    private static final Operator FACTORIAL       = new OpFactorial();

    /**
     * Retrieves a Built-in operator.
     *
     * @param symbol Symbol representing the operator
     * @param numArguments Number of arguments of the operator
     * @return An operator matching the criteria if one exists or {@code null}
     * otherwise
     * @see Operator
     */
    public static Operator getBuiltinOperator(final char symbol, final int numArguments) {
        switch(symbol) {
            case '+':
                if (numArguments != 1) {
                    return ADDITION;
                } else{
                    return ADDITION_UN;
                }
            case '-':
                if (numArguments != 1) {
                    return SUBTRACTION;
                } else{
                    return SUBTRACTION_UN;
                }
            case '*': return MUTLIPLICATION;
            case '/': return DIVISION;
            case '^': return POWER;
            case '%': return MODULO;
            case '&': return AND;
            case '|': return OR;
            case '¬': return NOT;
            case '!':
                if (numArguments != 1) {
                    return FACTORIAL;
                }
            default:
                return null;
        }
    }

    private Operators() {
        // Don't let anyone initialize this class
    }

    /**
     * Array with all the available operators
     *
     * @return {@link Operator} array
     * @see Operators#getBuiltinOperator(char, int)
     */
    public static Operator[] getOperators() {
        return new Operator[]{
            ADDITION, ADDITION_UN, SUBTRACTION, SUBTRACTION_UN,
            MUTLIPLICATION, DIVISION, MODULO, POWER,
            AND, OR, NOT,
            FACTORIAL
        };
    }

    private static final class OpAdd extends Operator {
        private static final long serialVersionUID = -6902781239333016448L;
        OpAdd() { super("+", 2, true, PRECEDENCE_ADDITION); }
        @Override
        public double apply(double... args) {
            return args[0] + args[1];
        }
    }

    private static final class OpAddUnary extends Operator {
        private static final long serialVersionUID = 793924203719717929L;
        OpAddUnary() { super("+", 1, false, PRECEDENCE_UNARY_PLUS); }
        @Override
        public double apply(double... args) {
            return args[0];
        }
    }

    private static final class OpMinus extends Operator {
        private static final long serialVersionUID = -3511523899514942407L;
        OpMinus() { super("-", 2, true, PRECEDENCE_ADDITION); }
        @Override
        public double apply(double... args) {
            return args[0] - args[1];
        }
    }

    private static final class OpMinusUnary extends Operator {
        private static final long serialVersionUID = -887228242398619895L;
        OpMinusUnary() { super("-", 1, false, PRECEDENCE_UNARY_MINUS); }
        @Override
        public double apply(double... args) {
            return -args[0];
        }
    }

    private static final class OpMultiply extends Operator {
        private static final long serialVersionUID = 604402774847173166L;
        OpMultiply() { super("*", 2, true, PRECEDENCE_MULTIPLICATION); }
        @Override
        public double apply(double... args) {
            return args[0] * args[1];
        }
    }

    private static final class OpDivide extends Operator {
        private static final long serialVersionUID = -1687653461890168296L;
        OpDivide() { super("/", 2, true, PRECEDENCE_DIVISION); }
        @Override
        public double apply(double... args) {
            if (args[1] == 0d) {
                throw new ArithmeticException(l10n("Division by zero!"));
            }
            return args[0] / args[1];
        }
    }

    private static final class OpPower extends Operator {
        private static final long serialVersionUID = 8176172587258190827L;
        OpPower() { super("^", 2, false, PRECEDENCE_POWER); }
        @Override
        public double apply(double... args) {
            return Math.pow(args[0], args[1]);
        }
    }

    private static final class OpModulo extends Operator {
        private static final long serialVersionUID = -8657864901943257599L;
        OpModulo() { super("%", 2, true, PRECEDENCE_MODULO); }
        @Override
        public double apply(double... args) {
            if (args[1] == 0d) {
                throw new ArithmeticException(l10n("Division by zero!"));
            }
            return args[0] % args[1];
        }
    }

    private static final class OpAnd extends Operator {
        private static final long serialVersionUID = 7730531744867276402L;
        OpAnd() { super("&", 2, true, PRECEDENCE_AND); }
        @Override
        public double apply(double... args) {
            final boolean a = Math.abs(args[0]) >= BOOLEAN_THRESHOLD;
            final boolean b = Math.abs(args[1]) >= BOOLEAN_THRESHOLD;
            return (a & b) ? 1 : 0;
        }
    }

    private static final class OpOr extends Operator {
        private static final long serialVersionUID = 4652717701575702240L;
        OpOr() { super("|", 2, true, PRECEDENCE_OR); }
        @Override
        public double apply(double... args) {
            final boolean a = Math.abs(args[0]) >= BOOLEAN_THRESHOLD;
            final boolean b = Math.abs(args[1]) >= BOOLEAN_THRESHOLD;
            return (a | b) ? 1 : 0;
        }
    }

    private static final class OpNot extends Operator {
        private static final long serialVersionUID = -8848717292894659390L;
        OpNot() { super("¬", 1, false, PRECEDENCE_NOT); }
        @Override
        public double apply(double... args) {
            return (Math.abs(args[0]) < BOOLEAN_THRESHOLD) ? 1 : 0;
        }
    }

    private static final class OpFactorial extends Operator {
        private static final long serialVersionUID = 9103176758714614115L;
        OpFactorial() { super("!", 1, true, Operator.PRECEDENCE_POWER + 1); }
        @Override
        public double apply(double... args) {
            final int arg = (int) args[0];
            if ((double) arg != args[0]) {
                String msg = "Operand for factorial has to be an integer";
                throw new IllegalArgumentException(l10n(msg));
            }
            if (arg < 0) {
                String msg = "The operand of the factorial can not be less than zero";
                throw new IllegalArgumentException(l10n(msg));
            }
            if (arg > 170) {
                String msg = "The operand of the factorial can not be more than 170";
                throw new IllegalArgumentException(l10n(msg));
            }
            double result = 1;
            for (int i = 1; i <= arg; i++) {
                result *= i;
            }
            return result;
        }
    }
}
