package net.sp8craft.math.expressions.json;

import java.util.HashMap;
import java.util.Map;

public record VarsJSON(HashMap<String, String> vars) {
    public String replaceWithVars(String text) {
        String str = text;
        for(Map.Entry<String,String> entry: this.vars().entrySet()) {
            str = str.replaceAll(entry.getKey(), "(" + entry.getValue() + ")");
        }
        return str;
    }
}
