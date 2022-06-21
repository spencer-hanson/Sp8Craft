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
package net.sp8craft.dependencies.net.objecthunter.exp4j.utils;

import java.util.ResourceBundle;

/**
 * Small wrapper for {@code ResourceBundle}
 * @author Federico Vera {@literal <fede@riddler.com.ar>}
 */
public final class Text {
    private Text() {
        // Don't let anyone initialize this class
    }

    /**
     * Retrieves the localized version of the string.
     *
     * @param key key to search
     * @return if the key is found it retrieves the value, in case of error it
     * returns the key itself
     */
    public static String l10n(String key) {
        try {
            return key; // modified
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * Retrieves the localized and formatted version of the string.
     *
     * @param key key to search
     * @param args arguments used for formatting
     * @return if the key is found it retrieves the value, in case of error it
     * returns the key itself
     * @see String#format(java.lang.String, java.lang.Object...)
     */
    public static String l10n(String key, Object...args) {
        try {
            return String.format(key, args);
        } catch (Exception e) {
            return key;
        }
    }
}
