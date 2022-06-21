/*
 * Copyright 2015-2018 Federico Vera
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

import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.FunctionToken;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.NumberToken;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.OperatorToken;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.Token;
import net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text;

import static net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.TokenType.NUMBER;

/**
 *
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 */
final class Simplifier {
    private Simplifier() {
        // Don't let anyone initialize this class
    }

    public static Token[] simplify (Token[] tokens) {
        final TokenStack output = new TokenStack(tokens.length);
        for (Token t : tokens) {
            switch(t.getType()) {
                case NUMBER:
                case VARIABLE:
                    output.push(t);
                    break;
                case OPERATOR:
                    final OperatorToken op  = (OperatorToken) t;
                    final Operator operator = op.getOperator();

                    if (output.size()  < operator.getNumOperands()) {
                        throw new IllegalArgumentException(Text.l10n(
                                "Invalid number of operands available"
                        ));
                    }

                    if (operator.getNumOperands() == 2) {
                        final Token rightArg = output.pop();
                        final Token leftArg  = output.pop();

                        if (rightArg.getType() == NUMBER &&
                            leftArg .getType() == NUMBER) {

                            output.push(new NumberToken(operator.apply(
                                    ((NumberToken)leftArg ).getValue(),
                                    ((NumberToken)rightArg).getValue()
                                ))
                            );
                        } else {
                            output.push(leftArg);
                            output.push(rightArg);
                            output.push(t);
                        }
                    } else if (operator.getNumOperands() == 1) {
                        final Token arg = output.pop();

                        if (arg.getType() == NUMBER) {
                            output.push(new NumberToken(operator.apply(
                                    ((NumberToken)arg).getValue()
                                ))
                            );
                        } else {
                            output.push(arg);
                            output.push(t);
                        }
                    }
                    break;
                case FUNCTION:
                    final FunctionToken func = (FunctionToken) t;
                    final int numArgs = func.getFunction().getNumArguments();

                    //collect the arguments from the stack
                    final Token[] args = new Token[numArgs];
                    boolean areNumbers = true;

                    for (int j = 0; j < numArgs; j++) {
                        args[j] = output.pop();
                        areNumbers &= args[j].getType() == NUMBER;
                    }

                    if (areNumbers && func.getFunction().isDeterministic()) {
                        output.push(new NumberToken(
                                func.getFunction().apply(reverseInPlace(args)))
                        );
                    } else {
                        for (int i = args.length - 1; i >= 0; i--) {
                            output.push(args[i]);
                        }
                        output.push(t);
                    }
                default:
                    //Do nothing
            }
        }

        return output.toArray();
    }

    private static double[] reverseInPlace(Token[] args) {
        final double[] nargs = new double[args.length];

        final int sz = args.length - 1;
        for (int j = 0; j <= sz; j++) {
            nargs[j] = ((NumberToken)args[sz - j]).getValue();
        }

        return nargs;
    }
}
