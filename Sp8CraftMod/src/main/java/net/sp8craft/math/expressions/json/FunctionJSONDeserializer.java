package net.sp8craft.math.expressions.json;

import com.google.gson.*;
import net.sp8craft.math.expressions.FeatureExpression;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FunctionJSONDeserializer implements JsonDeserializer<FunctionJSON> {

    public HashMap<String, String> jsonObjToMap(JsonObject jsonObj) {
        HashMap<String, String> mapping = new HashMap<>();

        for(Map.Entry<String, JsonElement> entry : jsonObj.entrySet()) {
            mapping.put(entry.getKey(), entry.getValue().getAsString());
        }
        return mapping;
    }

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public FunctionJSON deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();

        HashMap<String, String> vars = jsonObjToMap(jsonObj.get("vars").getAsJsonObject());
        HashMap<String, String> conditionals = jsonObjToMap(jsonObj.get("conditionals").getAsJsonObject());

        JsonObject featuresObj = jsonObj.get("features").getAsJsonObject();

        HashMap<String, FeatureExpression> features = new HashMap<>();

        for(Map.Entry<String, JsonElement> entry : featuresObj.entrySet()) {
            JsonObject featureEntry = entry.getValue().getAsJsonObject();
            FeatureExpression expr = new FeatureExpression(
                    featureEntry.get("expression").getAsString(),
                    featureEntry.get("result").getAsString()
            );

            features.put(entry.getKey(), expr);
        }

        return new FunctionJSON(new VarsJSON(vars), new ConditionalJSON(conditionals), new FeaturesJSON(features));
    }
}
