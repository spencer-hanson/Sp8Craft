package net.sp8craft.math.expressions.json;

import java.util.HashMap;

public record FunctionJSON(VarsJSON vars, ConditionalJSON conditionals, FeaturesJSON features) {
    public static FunctionJSON getEmpty() {
        return new FunctionJSON(
                new VarsJSON(new HashMap<>()),
                new ConditionalJSON(new HashMap<>()),
                new FeaturesJSON(new HashMap<>())
        );
    }
}
