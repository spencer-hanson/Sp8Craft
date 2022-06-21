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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Contains the validation result for a given {@link Expression}
 */
public final class ValidationResult implements Serializable {
    private static final long serialVersionUID = -4623935821384176190L;

    /**
     * A static class representing a successful validation result
     */
    @SuppressWarnings("unchecked")
    public static final ValidationResult SUCCESS = new ValidationResult(Collections.EMPTY_LIST);

    private final List<String> errors;

    /**
     * Create a new instance
     * @param errors The list of errors returned if the validation was unsuccessful
     */
    ValidationResult(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Check if an expression has been validated successfully
     * @return true if the validation was successful, false otherwise
     */
    public boolean isValid() {
        return errors.isEmpty();
    }

    /**
     * Get the list of errors describing the issues while validating the expression
     * @return The List of errors
     */
    public List<String> getErrors() {
        return errors;
    }
}
