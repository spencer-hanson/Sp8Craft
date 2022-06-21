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

/**
 * Class representing the builtin functions available for use in expressions
 */
public final class Functions {

    /**
     * Wrapper for {@link Math#sin(double)}.
     */
    public static final Function SIN   = new Sin();

    /**
     * Wrapper for {@link Math#cos(double)}.
     */
    public static final Function COS   = new Cos();

    /**
     * Wrapper for {@link Math#tan(double)}.
     */
    public static final Function TAN   = new Tan();

    /**
     * Wrapper for {@link Math#log(double)}.
     */
    public static final Function LOG   = new Log();

    /**
     * Wrapper for {@link Math#log1p(double)}.
     */
    public static final Function LOG1P = new Log1p();

    /**
     * Wrapper for {@link Math#log10(double)}.
     */
    public static final Function LOG10 = new Log10();

    /**
     * Base 2 Logarithm
     */
    public static final Function LOG2  = new Log2();

    /**
     * Wrapper for {@link Math#abs(double)}.
     */
    public static final Function ABS   = new Abs();

    /**
     * Wrapper for {@link Math#acos(double)}.
     */
    public static final Function ACOS  = new ACos();

    /**
     * Wrapper for {@link Math#asin(double)}.
     */
    public static final Function ASIN  = new ASin();

    /**
     * Wrapper for {@link Math#atan(double)}.
     */
    public static final Function ATAN  = new ATan();

    /**
     * Wrapper for {@link Math#cbrt(double)}.
     */
    public static final Function CBRT  = new CBRT();

    /**
     * Wrapper for {@link Math#ceil(double)}.
     */
    public static final Function CEIL  = new Ceil();

    /**
     * Wrapper for {@link Math#floor(double)}.
     */
    public static final Function FLOOR = new Floor();

    /**
     * Wrapper for {@link Math#sinh(double)}.
     */
    public static final Function SINH  = new Sinh();

    /**
     * Wrapper for {@link Math#sqrt(double)}.
     */
    public static final Function SQRT  = new Sqrt();

    /**
     * Wrapper for {@link Math#tanh(double)}.
     */
    public static final Function TANH  = new Tanh();

    /**
     * Wrapper for {@link Math#cosh(double)}.
     */
    public static final Function COSH  = new Cosh();

    /**
     * Wrapper for {@link Math#pow(double, double)}.
     */
    public static final Function POW   = new Pow();

    /**
     * Wrapper for {@link Math#exp(double)}.
     */
    public static final Function EXP   = new Exp();

    /**
     * Wrapper for {@link Math#expm1(double)}.
     */
    public static final Function EXPM1 = new Expm1();

    /**
     * Wrapper for {@link Math#signum(double)}.
     */
    public static final Function SGN   = new Signum();

    /**
     * Wrapper for {@link Math#PI}.
     */
    public static final Function PI    = new Pi();

    /**
     * Wrapper for {@link Math#E}.
     */
    public static final Function E     = new E();

    private Functions() {
        // Don't let anyone initialize this class
    }

    /**
     * Array with all the available functions
     *
     * @return {@link Function} array
     * 
     * @see Functions#getBuiltinFunction(String)
     * @see Functions#SIN
     * @see Functions#COS
     * @see Functions#TAN
     * @see Functions#ASIN
     * @see Functions#ACOS
     * @see Functions#ATAN
     * @see Functions#SINH
     * @see Functions#COSH
     * @see Functions#TANH
     * @see Functions#ABS
     * @see Functions#LOG
     * @see Functions#LOG10
     * @see Functions#LOG2
     * @see Functions#LOG1P
     * @see Functions#CEIL
     * @see Functions#FLOOR
     * @see Functions#SQRT
     * @see Functions#CBRT
     * @see Functions#POW
     * @see Functions#EXP
     * @see Functions#EXPM1
     * @see Functions#SGN
     * @see Functions#PI
     * @see Functions#E
     */
    public static Function[] getFunctions() {
        return new Function[] {
            SIN, COS, TAN,
            ASIN, ACOS, ATAN,
            SINH, COSH, TANH,
            LOG, LOG2, LOG10, LOG1P,
            ABS, CBRT, CEIL, FLOOR,
            SQRT, POW, EXP, EXPM1, SGN,
            PI, E
        };
    }

    /**
     * Get the builtin function for a given name.
     *
     * @param name the name of the function
     * @return a Function instance
     *
     * @see Functions#getFunctions()
     * @see Functions#SIN
     * @see Functions#COS
     * @see Functions#TAN
     * @see Functions#ASIN
     * @see Functions#ACOS
     * @see Functions#ATAN
     * @see Functions#SINH
     * @see Functions#COSH
     * @see Functions#TANH
     * @see Functions#ABS
     * @see Functions#LOG
     * @see Functions#LOG10
     * @see Functions#LOG2
     * @see Functions#LOG1P
     * @see Functions#CEIL
     * @see Functions#FLOOR
     * @see Functions#SQRT
     * @see Functions#CBRT
     * @see Functions#POW
     * @see Functions#EXP
     * @see Functions#EXPM1
     * @see Functions#SGN
     * @see Functions#PI
     * @see Functions#E
     */
    public static Function getBuiltinFunction(final String name) {
        switch (name) {
            case "sin":   return SIN;
            case "cos":   return COS;
            case "tan":   return TAN;
            case "asin":  return ASIN;
            case "acos":  return ACOS;
            case "atan":  return ATAN;
            case "sinh":  return SINH;
            case "cosh":  return COSH;
            case "tanh":  return TANH;
            case "abs":   return ABS;
            case "log":   return LOG;
            case "log10": return LOG10;
            case "log2":  return LOG2;
            case "log1p": return LOG1P;
            case "ceil":  return CEIL;
            case "floor": return FLOOR;
            case "sqrt":  return SQRT;
            case "cbrt":  return CBRT;
            case "pow":   return POW;
            case "exp":   return EXP;
            case "expm1": return EXPM1;
            case "signum":return SGN;
            case "pi":    return PI;
            case "e":     return E;
            default:
                return null;
        }
    }

    private static final class Sin extends Function {
        private static final long serialVersionUID = -2914815912624930172L;
        Sin() { super("sin"); }
        @Override
        public double apply(double... args) {
            return Math.sin(args[0]);
        }
    }

    private static final class Cos extends Function {
        private static final long serialVersionUID = -8597756339516016820L;
        Cos() { super("cos"); }
        @Override
        public double apply(double... args) {
            return Math.cos(args[0]);
        }
    }

    private static final class Tan extends Function {
        private static final long serialVersionUID = -8768547215015498527L;
        Tan() { super("tan"); }
        @Override
        public double apply(double... args) {
            return Math.tan(args[0]);
        }
    }

    private static final class Log extends Function {
        private static final long serialVersionUID = 2780592577805165247L;
        Log() { super("log"); }
        @Override
        public double apply(double... args) {
            return Math.log(args[0]);
        }
    }

    private static final class Log2 extends Function {
        private static final long serialVersionUID = -440311464566044720L;
        Log2() { super("log2"); }
        @Override
        public double apply(double... args) {
            return Math.log(args[0]) / Math.log(2d);
        }
    }

    private static final class Log10 extends Function {
        private static final long serialVersionUID = 4939208624837474082L;
        Log10() { super("log10"); }
        @Override
        public double apply(double... args) {
            return Math.log10(args[0]);
        }
    }

    private static final class Log1p extends Function {
        private static final long serialVersionUID = 8955502126364713001L;
        Log1p() { super("log1p"); }
        @Override
        public double apply(double... args) {
            return Math.log1p(args[0]);
        }
    }

    private static final class Abs extends Function {
        private static final long serialVersionUID = 3561065212490216039L;
        Abs() { super("abs"); }
        @Override
        public double apply(double... args) {
            return Math.abs(args[0]);
        }
    }

    private static final class ACos extends Function {
        private static final long serialVersionUID = 2254051679608503250L;
        ACos() { super("acos"); }
        @Override
        public double apply(double... args) {
            return Math.acos(args[0]);
        }
    }

    private static final class ASin extends Function {
        private static final long serialVersionUID = -326395111700221980L;
        ASin() { super("asin"); }
        @Override
        public double apply(double... args) {
            return Math.asin(args[0]);
        }
    }

    private static final class ATan extends Function {
        private static final long serialVersionUID = -7050692574354752375L;
        ATan() { super("atan"); }
        @Override
        public double apply(double... args) {
            return Math.atan(args[0]);
        }
    }

    private static final class CBRT extends Function {
        private static final long serialVersionUID = 2299685413091396681L;
        CBRT() { super("cbrt"); }
        @Override
        public double apply(double... args) {
            return Math.cbrt(args[0]);
        }
    }

    private static final class Floor extends Function {
        private static final long serialVersionUID = 2356850441515448860L;
        Floor() { super("floor"); }
        @Override
        public double apply(double... args) {
            return Math.floor(args[0]);
        }
    }

    private static final class Sinh extends Function {
        private static final long serialVersionUID = -8338824383285606826L;
        Sinh() { super("sinh"); }
        @Override
        public double apply(double... args) {
            return Math.sinh(args[0]);
        }
    }

    private static final class Tanh extends Function {
        private static final long serialVersionUID = -1652041817305063164L;
        Tanh() { super("tanh"); }
        @Override
        public double apply(double... args) {
            return Math.tanh(args[0]);
        }
    }

    private static final class Cosh extends Function {
        private static final long serialVersionUID = 1997312144944632975L;
        Cosh() { super("cosh"); }
        @Override
        public double apply(double... args) {
            return Math.cosh(args[0]);
        }
    }

    private static final class Ceil extends Function {
        private static final long serialVersionUID = -7245919541022618316L;
        Ceil() { super("ceil"); }
        @Override
        public double apply(double... args) {
            return Math.ceil(args[0]);
        }
    }

    private static final class Sqrt extends Function {
        private static final long serialVersionUID = 398483995880506177L;
        Sqrt() { super("sqrt"); }
        @Override
        public double apply(double... args) {
            return Math.sqrt(args[0]);
        }
    }

    private static final class Pow extends Function {
        private static final long serialVersionUID = -3555626505791838799L;
        Pow() { super("pow", 2); }
        @Override
        public double apply(double... args) {
            return Math.pow(args[0], args[1]);
        }
    }

    private static final class Exp extends Function {
        private static final long serialVersionUID = 2154874773418461840L;
        Exp() { super("exp"); }
        @Override
        public double apply(double... args) {
            return Math.exp(args[0]);
        }
    }

    private static final class Expm1 extends Function {
        private static final long serialVersionUID = -7746496058083268582L;
        Expm1() { super("expm1"); }
        @Override
        public double apply(double... args) {
            return Math.expm1(args[0]);
        }
    }

    private static final class Signum extends Function {
        private static final long serialVersionUID = -5814745953179606422L;
        Signum() { super("signum"); }
        @Override
        public double apply(double... args) {
            if (args[0] > 0) {
                return 1;
            } else if (args[0] < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private static final class Pi extends Function {
        private static final long serialVersionUID = -3698084286493723333L;
        Pi () { super("pi", 0); }
        @Override
        public double apply(double... args) {
            return Math.PI;
        }
    }

    private static final class E extends Function {
        private static final long serialVersionUID = 8712470987230587412L;
        E() { super("e", 0); }
        @Override
        public double apply(double... args) {
            return Math.E;
        }
    }
}