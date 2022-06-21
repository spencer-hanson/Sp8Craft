package net.sp8craft.math.expressions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FunctionLoader {
    public static class FunctionParsingException extends Exception {}

    public FunctionLoader() {
        ArrayList<Sp8Function> functions = new ArrayList<>();

        try {

            File fileObj = new File("expressions.txt");
            Scanner reader = new Scanner(fileObj);
            while(reader.hasNextLine()) {
                String funcType = reader.nextLine();
                switch (Sp8Function.stringToType(funcType)) {
                    case DATA -> functions.add(DataFunction.build(reader.nextLine()));
                    case CONDITIONAL -> functions.add(ConditionFunction.build(reader.nextLine()));
                }
            }
//            File fileObj = new File("expressions.txt");
//            fileObj.createNewFile();
//            FileWriter writer = new FileWriter(fileObj);
//            writer.write("test");
//            writer.close();
        } catch (Exception e) {
//        } catch (FileNotFoundException | Sp8Function.InvalidFunctionTypeException e) {
            e.printStackTrace();
        }

    }
}
