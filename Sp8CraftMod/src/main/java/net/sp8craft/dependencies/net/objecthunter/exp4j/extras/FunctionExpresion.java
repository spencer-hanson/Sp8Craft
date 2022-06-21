/*
* Copyright 2018 Federico Vera
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

import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;
import net.sp8craft.dependencies.net.objecthunter.exp4j.ExpressionBuilder;
import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Function;
import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator;
import net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text;

/**
 * This class contains a wrapper that allows creating expression based functions.
 *
 * <p><i><b>Note</b></i>: This class <s>might</s> <i>will</i> fail in
 * threaded evaluations, if you need to use this to evaluate operations
 * concurrently, please create a new instance per thread.This is particularly
 * important when using {@link Expression#copy()} because {@code Function}s are
 * not copied.</p>
 * <p>Built expressions are simplified by default</p>
 *
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 * @since 0.8-riddler
 */
public class FunctionExpresion extends Function {
    private static final long serialVersionUID = -7343720821425746432L;
    private final Expression exp;
    private final String[] vars;

    /**
     * Constructor of a deterministic Expression based Function.
     *
     * <p>The arguments will be called {@code 'a'}, {@code 'b'}, {@code 'c'},
     * {@code 'd'}, ..., {@code 'z'}</p>
     *
     * @param name Name of the function
     * @param nargs Number of arguments
     * @param expr Expression to evaluate
     * @throws IllegalArgumentException If {@code nargs} is bigger than
     * {@code 26}
     */
    public FunctionExpresion(
            String name,
            int nargs,
            String expr) {
        this(name, nargs, true, expr, null, null);
    }

    /**
     * Constructor of a deterministic Expression based Function.
     *
     * <p>The arguments will be called {@code 'a'}, {@code 'b'}, {@code 'c'},
     * {@code 'd'}, ..., {@code 'z'}</p>
     *
     * @param name Name of the function
     * @param nargs Number of arguments
     * @param expr Expression to evaluate
     * @param functions Additional functions needed to compile this expression
     * @throws IllegalArgumentException If {@code nargs} is bigger than
     * {@code 26}
     */
    public FunctionExpresion(
            String name,
            int nargs,
            String expr,
            Function[] functions) {
        this(name, nargs, true, expr, functions, null);
    }

    /**
     * Constructor.
     *
     * <p>The arguments will be called {@code 'a'}, {@code 'b'}, {@code 'c'},
     * {@code 'd'}, ..., {@code 'z'}</p>
     *
     * @param name Name of the function
     * @param nargs Number of arguments
     * @param det Tells if the function is deterministic or not
     * @param expr Expression to evaluate
     * @param operators Additional operators needed to compile this expression
     * @param functions Additional functions needed to compile this expression
     * @throws IllegalArgumentException If {@code nargs} is bigger than
     * {@code 26}
     */
    public FunctionExpresion(
            String name,
            int nargs,
            boolean det,
            String expr,
            Function[] functions,
            Operator[] operators) {
        super(name, nargs, det);

        if (nargs > 'z' - 'a' + 1) {
            String msg = "Function must have less than 26 arguments";
            throw new IllegalArgumentException(Text.l10n(msg));
        }

        ExpressionBuilder builder = new ExpressionBuilder(expr);
        vars = new String[nargs];

        for (char i = 'a'; i < ('a' + nargs); i++) {
            vars[i - 'a'] = Character.toString(i);
        }

        if (functions != null) {
            builder.functions(functions);
        }

        if (operators != null) {
            builder.operators(operators);
        }

        if (nargs > 0) {
            builder.variables(vars);
        }

        exp = builder.build(true);
    }

    @Override
    public double apply(double... args) {
        for (int i = 0; i < args.length; i++) {
            exp.setVariable(vars[i], args[i]);
        }
        return exp.evaluate();
    }

}
