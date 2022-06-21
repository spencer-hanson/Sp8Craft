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

/**
 * represents a setVariable used in an expression
 */
public final class VariableToken extends Token {
    private static final long serialVersionUID = -2944291251393065262L;

    private final String name;
    private boolean valueSet;
    private double value;

    /**
     * Get the name of the setVariable
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Create a new instance
     * @param name the name of the setVariable
     */
    public VariableToken(String name) {
        super(TokenType.VARIABLE);
        this.name = name;
    }

    /**
     * Retrieves the currently assigned value for this Variable
     * @return value
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets a new value for this Variable
     * @param value new value for this variable
     */
    public void setValue(double value) {
        this.value = value;
        valueSet = true;
    }

    /**
     * Tells if a value has been set for this variable
     * @return {@code true} if a value has been set, and {@code false} otherwise
     */
    public boolean isValueSet() {
        return valueSet;
    }

    /**
     * Retrieves a copy of this token
     *
     * @return {@code VariableToken}
     */
    public VariableToken copy() {
        final VariableToken vt = new VariableToken(name);
        if (isValueSet()) {
            vt.setValue(getValue());
        }
        return vt;
    }

    @Override
    public String toString() {
        if (!valueSet) {
            return name;
        }
        return name + "(" + value + ")";
    }
}
