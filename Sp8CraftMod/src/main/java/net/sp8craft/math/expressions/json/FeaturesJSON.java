package net.sp8craft.math.expressions.json;

import net.sp8craft.math.expressions.FeatureExpression;
import java.util.HashMap;


public record FeaturesJSON(HashMap<String, FeatureExpression> features) {}
