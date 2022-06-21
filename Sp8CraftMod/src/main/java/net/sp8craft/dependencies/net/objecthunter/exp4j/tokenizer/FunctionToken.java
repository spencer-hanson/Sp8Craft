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

import net.sp8craft.dependencies.net.objecthunter.exp4j.function.Function;

/**
 * Represents a {@link Function}
 */
public final class FunctionToken extends Token {
    private static final long serialVersionUID = 6070719888134223365L;

    private final Function function;

    /**
     * Create a new instance
     * @param function Function
     */
    public FunctionToken(final Function function) {
        super(TokenType.FUNCTION);
        this.function = function;
    }

    /**
     * Retrieves the {@link Function} associated to this {@link Token}
     *
     * @return {@link Function}
     */
    public Function getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return function.getName();
    }
}
