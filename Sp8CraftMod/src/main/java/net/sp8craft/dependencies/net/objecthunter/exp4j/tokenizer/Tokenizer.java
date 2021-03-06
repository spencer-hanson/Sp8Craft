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
package net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer;

import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Function;
import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Functions;
import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operator;
import net.sp8craft.dependencies.net.objecthunter.exp4j.operator.Operators;

import static net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.TokenType.*;
import static net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text.l10n;

public final class Tokenizer {

    private final char[] expression;

    private final int expressionLength;

    private final Map<String, Function> userFunctions;

    private final Map<String, Operator> userOperators;

    private final Map<String, VariableToken> variableTokens;

    private int pos = 0;

    private Token lastToken;

    private final boolean useBuiltInFunctions;

    public Tokenizer(final String expression,
                    final Map<String, Function> userFunctions,
                    final Map<String, Operator> userOperators,
                    final Set<String> variableNames,
                    final boolean useBuiltInFunctions) {
        this.expression = expression.trim().toCharArray();
        this.expressionLength = this.expression.length;
        this.userFunctions = userFunctions;
        this.userOperators = userOperators;
        this.useBuiltInFunctions = useBuiltInFunctions;

        variableTokens = new TreeMap<>();
        if (variableNames != null) {
            for (String vn : variableNames) {
                variableTokens.put(vn, new VariableToken(vn));
            }
        }
    }

    public boolean hasNext() {
        return expression.length > pos;
    }

    public Token nextToken(){
        char ch = expression[pos];
        while (Character.isWhitespace(ch)) {
            ch = expression[++pos];
        }
        if (Character.isDigit(ch) || ch == '.') {
            if (lastToken != null) {
                if (lastToken.getType() == NUMBER) {
                    throw new IllegalArgumentException(l10n(
                        "Unable to parse char '%s' (Code: %d) at [%d]", ch, (int) ch, pos
                    ));
                } else if ((lastToken.getType() != OPERATOR
                         && lastToken.getType() != PARENTHESES_OPEN
                         && lastToken.getType() != FUNCTION
                         && lastToken.getType() != SEPARATOR)) {
                    // insert an implicit multiplication token
                    lastToken = new OperatorToken(Operators.getBuiltinOperator('*', 2));
                    return lastToken;
                }
            }
            return parseNumberToken(ch);
        } else if (isArgumentSeparator(ch)) {
            return parseArgumentSeparatorToken();
        } else if (isOpenParentheses(ch)) {
            if (lastToken != null &&
                    (lastToken.getType() != OPERATOR
                  && lastToken.getType() != PARENTHESES_OPEN
                  && lastToken.getType() != FUNCTION
                  && lastToken.getType() != SEPARATOR)) {
                // insert an implicit multiplication token
                lastToken = new OperatorToken(Operators.getBuiltinOperator('*', 2));
                return lastToken;
            }
            return parseParentheses(true);
        } else if (isCloseParentheses(ch)) {
            return parseParentheses(false);
        } else if (Operator.isAllowedOperatorChar(ch)) {
            return parseOperatorToken(ch);
        } else if (isAlphabetic(ch) || ch == '_') {
            // parse the name which can be a setVariable or a function
            if (lastToken != null &&
                    (lastToken.getType() != OPERATOR
                  && lastToken.getType() != PARENTHESES_OPEN
                  && lastToken.getType() != FUNCTION
                  && lastToken.getType() != SEPARATOR)) {
                // insert an implicit multiplication token
                lastToken = new OperatorToken(Operators.getBuiltinOperator('*', 2));
                return lastToken;
            }
            return parseFunctionOrVariable();

        }
        throw new IllegalArgumentException(l10n(
            "Unable to parse char '%s' (Code: %d) at [%d]", ch, (int) ch, pos
        ));
    }

    private Token parseArgumentSeparatorToken() {
        this.pos++;
        this.lastToken = new ArgumentSeparatorToken();
        return lastToken;
    }

    private boolean isArgumentSeparator(char ch) {
        return ch == ',';
    }

    private Token parseParentheses(final boolean open) {
        if (open) {
            lastToken = new OpenParenthesesToken();
        } else {
            lastToken = new CloseParenthesesToken();
        }
        pos++;
        return lastToken;
    }

    private boolean isOpenParentheses(char ch) {
        return ch == '(' || ch == '{' || ch == '[';
    }

    private boolean isCloseParentheses(char ch) {
        return ch == ')' || ch == '}' || ch == ']';
    }

    private Token parseFunctionOrVariable() {
        final int offset = this.pos;
        int testPos;
        int lastValidLen = 1;
        Token lastValidToken = null;
        int len = 1;

        if (isEndOfExpression(offset)) {
            this.pos++;
        }

        testPos = offset + len - 1;
        while (!isEndOfExpression(testPos) &&
                isVariableOrFunctionCharacter(expression[testPos])) {
            String name = new String(expression, offset, len);
            if (variableTokens.containsKey(name)) {
                lastValidLen = len;
                lastValidToken = variableTokens.get(name);
            } else {
                final Function f = getFunction(name);
                if (f != null) {
                    lastValidLen = len;
                    lastValidToken = new FunctionToken(f);
                }
            }
            len++;
            testPos = offset + len - 1;
        }

        if (lastValidToken == null) {
            throw new UnknownFunctionOrVariableException(new String(expression), pos, len);
        }

        pos += lastValidLen;
        lastToken = lastValidToken;

        return lastToken;
    }

    private Function getFunction(String name) {
        Function f = null;

        if (this.userFunctions != null) {
            f = this.userFunctions.get(name);
        }

        if (f == null && useBuiltInFunctions) {
            f = Functions.getBuiltinFunction(name);
        }

        return f;
    }

    private Token parseOperatorToken(char firstChar) {
        final int offset = this.pos;
        int len = 1;
        final StringBuilder symbol = new StringBuilder();
        Operator lastValid = null;
        symbol.append(firstChar);

        while (!isEndOfExpression(offset + len)  && Operator.isAllowedOperatorChar(expression[offset + len])) {
            symbol.append(expression[offset + len++]);
        }

        while (symbol.length() > 0) {
            Operator op = this.getOperator(symbol.toString());
            if (op == null) {
                symbol.setLength(symbol.length() - 1);
            }else{
                lastValid = op;
                break;
            }
        }

        pos += symbol.length();
        lastToken = new OperatorToken(lastValid);
        return lastToken;
    }

    private Operator getOperator(String symbol) {
        Operator op = null;

        if (this.userOperators != null) {
            op = this.userOperators.get(symbol);
        }

        if (op == null && symbol.length() == 1) {
            int argc = 2;
            if (lastToken == null) {
                argc = 1;
            } else {
                TokenType lastTokenType = lastToken.getType();
                if (lastTokenType == PARENTHESES_OPEN || lastTokenType == SEPARATOR) {
                    argc = 1;
                } else if (lastTokenType == OPERATOR) {
                    final Operator lastOp = ((OperatorToken) lastToken).getOperator();
                    if (lastOp.getNumOperands() == 2 || (lastOp.getNumOperands() == 1 && !lastOp.isLeftAssociative())) {
                        argc = 1;
                    }
                }

            }
            op = Operators.getBuiltinOperator(symbol.charAt(0), argc);
        }
        return op;
    }

    private Token parseNumberToken(final char firstChar) {
        final int offset = this.pos;
        int len = 1;
        this.pos++;

        if (isEndOfExpression(offset + len)) {
            lastToken = new NumberToken(Double.parseDouble(String.valueOf(firstChar)));
            return lastToken;
        }

        while (!isEndOfExpression(offset + len) &&
                isNumeric(expression[offset + len], expression[offset + len - 1] == 'e' ||
                        expression[offset + len - 1] == 'E')) {
            len++;
            this.pos++;
        }

        // check if the e is at the end
        if (expression[offset + len - 1] == 'e' || expression[offset + len - 1] == 'E') {
            // since the e is at the end it's not part of the number and a rollback is necessary
            len--;
            pos--;
        }

        lastToken = new NumberToken(expression, offset, len);
        return lastToken;
    }

    private static boolean isNumeric(char ch, boolean lastCharE) {
        return Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E' ||
                (lastCharE && (ch == '-' || ch == '+'));
    }

    private static boolean isAlphabetic(int codePoint) {
        return Character.isLetter(codePoint);
    }

    private static boolean isVariableOrFunctionCharacter(int codePoint) {
        return isAlphabetic(codePoint) ||
                Character.isDigit(codePoint) ||
                codePoint == '_' ||
                codePoint == '.';
    }

    private boolean isEndOfExpression(int offset) {
        return this.expressionLength <= offset;
    }
}
