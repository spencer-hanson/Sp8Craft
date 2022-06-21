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
package net.sp8craft.dependencies.net.objecthunter.exp4j;
// Sourced from https://gitlab.com/riddler_arg/exp4j/-/tree/master/src

import java.io.Serializable;

import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Function;
import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Functions;
import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.FunctionToken;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.NumberToken;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.OperatorToken;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.Token;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.VariableToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.TokenType.*;
import static net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text.l10n;

/**
 * This class represents a valid mathematical expression.
 *
 * @author Frank Asseg
 * @see ExpressionBuilder#build()
 */
public final class Expression implements Serializable {

    private static final long serialVersionUID = -2510794384846712749L;

    private final Token[] tokens;

    private final String[] userFunctionNames;

    private final Map<String, VariableToken> variables = new TreeMap<>();

    private final boolean cacheResult;

    private Double result;

    /**
     * Creates a new expression that is a copy of the existing one.
     *
     * @return copy of this {@code Expression}
     */
    public Expression copy() {
        Expression exp = new Expression(
            Arrays.copyOf(tokens, tokens.length),
            Arrays.copyOf(userFunctionNames, userFunctionNames.length)
        );

        exp.variables.clear();
        //Since I don't honor the immutable token philosophy I need to copy
        //variable tokens... Still... I regret nothing!
        for (int i = 0; i < exp.tokens.length; i++) {
            if (exp.tokens[i].getType() == VARIABLE) {
                final VariableToken v = ((VariableToken)exp.tokens[i]);
                if (!exp.variables.containsKey(v.getName())) {
                    exp.variables.put(v.getName(), v.copy());
                }
                exp.tokens[i] = exp.variables.get(v.getName());
            }
        }

        return exp;
    }

    Expression(final Token[] tokens, String[] userFunctionNames) {
        this.tokens = tokens;
        this.userFunctionNames = userFunctionNames;
        populateVariablesMap();
        cacheResult = checkNonDeterministic(tokens, userFunctionNames.length);
    }

    /**
     * Tells if the result is being cached.
     *
     * @return {@code true} if the result will be cached and {@code false}
     * otherwise
     */
    protected boolean isCachingResult() {
        return cacheResult;
    }

    private boolean checkNonDeterministic(Token[] tokens, int userFuncs) {
        if (userFuncs == 0) {
            return true;
        }

        boolean status = false;
        for (Token t : tokens) {
            status |= (t.getType() == FUNCTION &&
                    !((FunctionToken)t).getFunction().isDeterministic());
        }
        return !status;
    }

    private void populateVariablesMap() {
        for (final Token t: tokens) {
            if (t.getType() == VARIABLE) {
                variables.put(((VariableToken)t).getName(), (VariableToken)t);
            }
        }
    }

    /**
     * Sets the value of a variable, the variable to set must exist at build time and can't be the
     * name of a function.
     * All variables must be set before calling {@link Expression#evaluate()}
     *
     * @param name variable name as passed to {@link ExpressionBuilder}
     * @param value value of the variable
     * @return {@code this}
     * @throws IllegalArgumentException if the variable name is a function name or if the variable
     * doesn't exist at build time.
     * @see ExpressionBuilder#build()
     * @see Expression#containsVariable(String)
     * @see Expression#getVariableNames()
     */
    public Expression setVariable(final String name, final double value) {
        checkVariableName(name);
        variables.get(name).setValue(value);
        result = null;
        return this;
    }

    private boolean hasUserFunction(String name) {
        boolean contains = false;
        for (String s : userFunctionNames) {
            contains |= Objects.equals(s, name);
        }
        return contains;
    }

    private void checkVariableName(String name) {
        if (hasUserFunction(name) || Functions.getBuiltinFunction(name) != null) {
            throw new IllegalArgumentException(l10n(
                    "The variable name '%s' is invalid. Since "
                  + "there exists a function with the same name", name
            ));
        }
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException(l10n("Variable '%s' doesn't exist.", name));
        }
    }

    /**
     * Sets the value of a set of variables, the variables to set must exist at build time and can't
     * be the name of a function.
     * All variables must be set before calling {@link Expression#evaluate()}
     *
     * @param variables a {@code Map<String,Double>} containing all of the (name, value) pairs.
     * @return {@code this}
     * @throws IllegalArgumentException if the variable name is a function name or if the variable
     * doesn't exist at build time.
     * @see ExpressionBuilder#build()
     * @see Expression#containsVariable(String)
     * @see Expression#getVariableNames()
     * @see Expression#setVariable(String, double)
     */
    public Expression setVariables(Map<String, Double> variables) {
        for (Map.Entry<String, Double> v : variables.entrySet()) {
            setVariable(v.getKey(), v.getValue());
        }
        return this;
    }

    /**
     * Retrieves a {@link Set} containing all the variable names
     *
     * @return variable names
     */
    public Set<String> getVariableNames() {
        return variables.keySet();
    }

    /**
     * Tells if a variable exists in the expression
     *
     * @param name variable name
     * @return {@code true} if the variable exists and {@code false} otherwise
     */
    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    /**
     * Validates an expression.<br>
     * Building an expression is not the only metric of <i>correctness</i>, this method will
     * generate a {@link ValidationResult} telling if a variables are set, if the number of
     * operands is correct, and if all functions have the right number of parameters.<br><br>
     * <i><b>Note:</b></i> future version will most likely fail on build, and not at this stage
     * (at least that's my plan).
     * @param checkVariablesSet {@code true} to check if all variables are set and {@code false}
     * otherwise
     * @return {@link ValidationResult}
     */
    public ValidationResult validate(boolean checkVariablesSet) {
        final List<String> errors = new ArrayList<>(10);
        checkVariablesSet(checkVariablesSet, errors);

        /* Check if the number of operands, functions and operators match.
           The idea is to increment a counter for operands and decrease it for operators.
           When a function occurs the number of available arguments has to be greater
           than or equals to the function's expected number of arguments.
           The count has to be larger than 1 at all times and exactly 1 after all tokens
           have been processed */
        int count = 0;
        for (Token tok : tokens) {
            switch (tok.getType()) {
                case NUMBER:
                case VARIABLE:
                    count++;
                    break;
                case FUNCTION:
                    final Function func = ((FunctionToken) tok).getFunction();
                    final int argsNum = func.getNumArguments();
                    count = validateFunction(argsNum, count, errors, func);
                    break;
                case OPERATOR:
                    final Operator op = ((OperatorToken) tok).getOperator();
                    if (op.getNumOperands() == 2) {
                        count--;
                    }
                    break;
                default:
                    //Do nothing
            }
            if (count < 1) {
                errors.add(l10n("Too many operators"));
                return new ValidationResult(errors);
            }
        }

        if (count > 1) {
            errors.add(l10n("Too many operands"));
        }

        return errors.isEmpty() ? ValidationResult.SUCCESS : new ValidationResult(errors);
    }

    private int validateFunction(int argsNum, int count, List<String> errors, Function func) {
        if (argsNum > count) {
            errors.add(l10n("Not enough arguments for '%s'", func.getName()));
        }

        int res = count;
        if (argsNum > 1) {
            res -= argsNum - 1;
        } else if (argsNum == 0) {
            // see https://github.com/fasseg/exp4j/issues/59
            res++;
        }

        return res;
    }

    /**
     * Alias for {@code Expression#validate(true)}
     *
     * @return {@link ValidationResult}
     * @see Expression#validate(boolean)
     */
    public ValidationResult validate() {
        return validate(true);
    }

    /**
     * Simple wrapper for {@link ExecutorService#submit(java.util.concurrent.Callable)}.<br><br>
     * Expressions are <b>NOT</b> thread safe (and most likely will never be).
     * @param executor {@link ExecutorService} to use
     * @return {@link Future} task that will eventually have the result of evaluate()
     * @see Expression#evaluate()
     */
    public Future<Double> evaluateAsync(ExecutorService executor) {
        return executor.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return evaluate();
            }
        });
    }

    /**
     * Evaluates the expression with the given values, this method will fail if
     * {@link Expression#validate()} returns a {@link ValidationResult}
     * different that {@link ValidationResult#SUCCESS}.<br><br>
     * <i><b>Note:</b></i> future version will most likely fail on build, and
     * not at this stage, this method will only fail if variables aren't set.
     *
     * @return result of the evaluation
     * @throws IllegalArgumentException if the expression isn't valid
     * @see Expression#validate()
     */
    public double evaluate() {
        if (cacheResult && result != null) {
            return result;
        }

        final ArrayStack output = new ArrayStack();
        for (Token t : tokens) {
            if (null != t.getType()) switch (t.getType()) {
                case NUMBER:
                    output.push(((NumberToken) t).getValue());
                    break;
                case VARIABLE:
                    final VariableToken vt = (VariableToken)t;

                    if (!vt.isValueSet()) {
                        throw new IllegalArgumentException(l10n(
                            "No value has been set for variable '%s'", vt.getName()
                        ));
                    }

                    output.push(vt.getValue());
                    break;
                case OPERATOR:
                    final Operator op = ((OperatorToken) t).getOperator();

                    if (output.size() < op.getNumOperands()) {
                        throw new IllegalArgumentException(l10n(
                            "Invalid number of operands available for '%s' operator",
                            op.getSymbol()
                        ));
                    }

                    if (op.getNumOperands() == 2) {
                        /* pop the operands and push the result of the operation */
                        final double rightArg = output.pop();
                        final double leftArg = output.pop();
                        output.push(op.apply(leftArg, rightArg));
                    } else if (op.getNumOperands() == 1) {
                        /* pop the operand and push the result of the operation */
                        final double arg = output.pop();
                        output.push(op.apply(arg));
                    }
                    break;
                case FUNCTION:
                    final Function func = ((FunctionToken) t).getFunction();
                    final int numArguments = func.getNumArguments();

                    if (output.size() < numArguments) {
                        throw new IllegalArgumentException(l10n(
                            "Invalid number of arguments available for '%s' function",
                            func.getName()
                        ));
                    }
                    /* collect the arguments from the stack */
                    final double[] args = new double[numArguments];

                    for (int j = numArguments - 1; j >= 0; j--) {
                        args[j] = output.pop();
                    }

                    output.push(func.apply(args));
                    break;
                default:
                    break;
            }
        }

        if (output.size() > 1) {
            throw new IllegalArgumentException(l10n(
                "Invalid number of items on the output queue. "
              + "Might be caused by an invalid number of arguments for a function."
            ));
        }

        return result = output.pop();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(tokens.length * 15);

        for (Token token : tokens) {
            sb.append(token).append(' ');
        }

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Retrieves the internal representation of the expression.<br>
     * This method is mostly useless for most users.
     *
     * @return RPN of the expression
     */
    public String toTokenString() {
        StringBuilder sb = new StringBuilder(tokens.length * 35);

        for (Token token : tokens) {
            sb.append(token.getType()).append('[').append(token).append("] ");
        }

        return sb.substring(0, sb.length() - 1);
    }

    private void checkVariablesSet(boolean checkVariablesSet, List<String> errors) {
        if (!checkVariablesSet) {
            return;
        }

        /* check that all vars have a value set */
        for (VariableToken vt : variables.values()) {
            if (!vt.isValueSet()) {
                errors.add(l10n("The variable '%s' has not been set", vt.getName()));
            }
        }
    }
}
