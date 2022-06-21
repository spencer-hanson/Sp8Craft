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

package net.sp8craft.dependencies.net.objecthunter.exp4j;

import java.io.Serializable;
import java.util.EmptyStackException;
import net.sp8craft.dependencies.net.objecthunter.exp4j.utils.Text;

/**
 * Simple double stack using a double array as data storage
 *
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 */
final class ArrayStack implements Serializable {

    private static final long serialVersionUID = 1922104850295923845L;

    private double[] data;

    private int idx;

    protected ArrayStack() {
        this(5);
    }

    protected ArrayStack(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                Text.l10n("Stack's capacity must be positive")
            );
        }

        data = new double[initialCapacity];
        idx = -1;
    }

    protected void push(double value) {
        if (idx + 1 == data.length) {
            double[] temp = new double[(int) (data.length * 1.2) + 1];
            System.arraycopy(data, 0, temp, 0, data.length);
            data = temp;
        }

        data[++idx] = value;
    }

    protected double peek() {
        if (idx == -1) {
            throw new EmptyStackException();
        }
        return data[idx];
    }

    protected double pop() {
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
}
