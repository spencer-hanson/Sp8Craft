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
package net.sp8craft.dependencies.net.objecthunter.exp4j.shuntingyard;

import java.util.*;

import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Function;
import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.OperatorToken;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.Token;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.Tokenizer;

import static net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.TokenType.*;
import static net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text.l10n;

/**
 * Shunting yard implementation to convert infix to reverse polish notation
 */
public final class ShuntingYard {

    private ShuntingYard() {
        // Don't let anyone initialize this class
    }

    /**
     * Convert a Set of tokens from infix to reverse polish notation
     * @param simplify tells the method to apply the simplifier to returned expression
     * @param expression the expression to convert
     * @param userFunctions the custom functions used
     * @param userOperators the custom operators used
     * @param variableNames the variable names used in the expression
     * @param useBuiltInFunctions tells if builtin functions should be enabled
     * @return a {@link net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.Token} array containing the result
     */
    public static Token[] convertToRPN(final boolean simplify,
                                       final String expression,
                                       final Map<String, Function> userFunctions,
                                       final Map<String, Operator> userOperators,
                                       final Set<String> variableNames,
                                       final boolean useBuiltInFunctions){
        final TokenStack stack  = new TokenStack();
        final TokenStack output = new TokenStack();

        final Tokenizer tokenizer = new Tokenizer(
                expression,
                userFunctions,
                userOperators,
                variableNames,
                useBuiltInFunctions
        );
        while (tokenizer.hasNext()) {
            Token token = tokenizer.nextToken();
            switch (token.getType()) {
            case NUMBER:
            case VARIABLE:
                output.push(token);
                break;
            case FUNCTION:
                function(stack, token);
                break;
            case SEPARATOR:
                separator(stack, output);
                break;
            case OPERATOR:
                operator(stack, output, token);
                stack.push(token);
                break;
            case PARENTHESES_OPEN:
                stack.push(token);
                break;
            case PARENTHESES_CLOSE:
                while (stack.peek().getType() != PARENTHESES_OPEN) {
                    output.push(stack.pop());
                }
                stack.pop();
                if (!stack.isEmpty() && stack.peek().getType() == FUNCTION) {
                    output.push(stack.pop());
                }
                break;
            default:
                //Do nothing
            }
        }

        while (!stack.isEmpty()) {
            Token t = stack.pop();
            if (t.getType() == PARENTHESES_CLOSE ||
                t.getType() == PARENTHESES_OPEN) {
                throw new IllegalArgumentException(l10n(
                    "Mismatched parentheses detected. Please check the expression"
                ));
            } else {
                output.push(t);
            }
        }

        if (simplify) {
            return Simplifier.simplify(output.toArray());
        }
        return output.toArray();
    }

    private static void operator(TokenStack stack, TokenStack output, Token token) {
        while (!stack.isEmpty() && stack.peek().getType() == OPERATOR) {
            final Operator o1 = ((OperatorToken) token).getOperator();
            final Operator o2 = ((OperatorToken) stack.peek()).getOperator();

            if (o1.getNumOperands() == 1 && o2.getNumOperands() == 2) {
                break;
            } else if ((o1.isLeftAssociative()
                        && (o1.getPrecedence() <= o2.getPrecedence()))
                    || (o1.getPrecedence() < o2.getPrecedence())) {
                output.push(stack.pop());
            } else {
                break;
            }
        }
    }

    private static void separator(TokenStack stack, TokenStack output) {
        while (!stack.isEmpty() && stack.peek().getType() != PARENTHESES_OPEN) {
            output.push(stack.pop());
        }

        if (stack.isEmpty() || stack.peek().getType() != PARENTHESES_OPEN) {
            throw new IllegalArgumentException(l10n(
                "Misplaced function separator ',' or mismatched parentheses"
            ));
        }
    }

    private static void function(TokenStack stack, Token token) {
        if(!stack.isEmpty() && stack.peek().getType() == FUNCTION) {
            throw new IllegalArgumentException(l10n(
                "Mismatched parentheses detected. Please check the expression"
            ));
        }
        stack.push(token);
    }
}
