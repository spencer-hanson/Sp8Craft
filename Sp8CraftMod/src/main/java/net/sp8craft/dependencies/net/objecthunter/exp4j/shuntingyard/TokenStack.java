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
package net.sp8craft.dependencies.net.objecthunter.exp4j.shuntingyard;

import java.io.Serializable;
import java.util.EmptyStackException;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.Token;
import net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text;

/**
 * Simple Token stack using a Token array as data storage
 *
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 */
final class TokenStack implements Serializable {
    private static final long serialVersionUID = -511763000103618441L;

    private Token[] data;

    private int idx;

    protected TokenStack() {
        this(32);
    }

    protected TokenStack(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(Text.l10n(
                    "Stack's capacity must be positive"
            ));
        }

        data = new Token[initialCapacity];
        idx = -1;
    }

    protected void push(Token value) {
        if (idx + 1 == data.length) {
            Token[] temp = new Token[(int) (data.length * 2) + 1];
            System.arraycopy(data, 0, temp, 0, data.length);
            data = temp;
        }

        data[++idx] = value;
    }

    protected Token peek() {
        if (idx == -1) {
            throw new EmptyStackException();
        }
        return data[idx];
    }

    protected Token pop() {
        if (idx == -1) {
            throw new EmptyStackException();
        }
        return data[idx--];
    }

    protected boolean isEmpty() {
        return idx == -1;
    }

    protected int size() {
        return idx + 1;
    }

    protected Token[] toArray() {
        Token[] ret = new Token[idx + 1];
        System.arraycopy(data, 0, ret, 0, ret.length);
        return ret;
    }
}
