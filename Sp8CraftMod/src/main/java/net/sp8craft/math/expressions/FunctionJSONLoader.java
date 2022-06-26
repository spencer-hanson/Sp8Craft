package net.sp8craft.math.expressions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sp8craft.math.expressions.json.FunctionJSON;
import net.sp8craft.math.expressions.json.FunctionJSONDeserializer;
import net.sp8craft.math.funcs.Sp8Function;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class FunctionJSONLoader {
    public static Optional<String> readFile(File fileObj) {
        try {
            return Optional.of(String.join("\n", Files.readAllLines(fileObj.toPath())));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static Sp8Function funcFromJSON() {
        Gson jsonReader = new GsonBuilder()
                .registerTypeAdapter(FunctionJSON.class, new FunctionJSONDeserializer())
                .create();
        Optional<String> fileData = readFile(new File("expressions.json"));
        FunctionJSON func;
        if (fileData.isPresent()) {
             func = jsonReader.fromJson(fileData.get(), FunctionJSON.class);
        } else {
            func = FunctionJSON.getEmpty();
        }
        return Sp8Function.fromFunctionJSON(func);
    }
}
