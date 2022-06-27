package net.sp8craft.util;

import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;
import net.sp8craft.dependencies.net.objecthunter.exp4j.ExpressionBuilder;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.*;
import net.sp8craft.math.expressions.FunctionEvaluator;
import net.sp8craft.math.expressions.FunctionJSONLoader;

import java.io.File;
import java.util.*;

public class Sp8CraftTesting {
    public static void testExpressions() {
        int relativeX = 5;

        Expression ex = new ExpressionBuilder("if(1,2,x)")
                .functions(FunctionsBoolean.getFunctions())
                .functions(FunctionsMisc.getFunctions())
                .functions(FunctionsSignal.getFunctions())
                .operators(OperatorsComparison.getOperators())
                .build();

        System.out.println("Evaluatin " + relativeX + " -> " + ex.evaluate());
    }

    public static void testFunctionEval() {
        int baseY = 64;
        int absY = 5;
        int absX = 7;
        int absZ = 8;
        int relX = 9;
        int relZ = 10;
        int relY = 11;

        ArrayList<String> initialValues = new ArrayList<>(Arrays.asList(
                "boolean done = false;",
                "String result = \"air\";",
                "int baseY = " + baseY + ";",
                "int absY = " + absY + ";",
                "int absX = " + absX + ";",
                "int absZ = " + absZ + ";",
                "int relX = " + relX + ";",
                "int relZ = " + relZ + ";",
                "int relY = " + relY + ";"
        ));

//        FunctionEvaluator functionEvaluator = new FunctionEvaluator(
//                new File("expressions.java"),
//                () -> System.out.println("Detected update from elsewhere")
//        );
//
//        functionEvaluator.startFileWatch();
//
//        while (true) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("Running file result -> \"" + functionEvaluator.runFile(initialValues) + "\"");
//        }
    }

    public static void main(String[] args) {
        FunctionJSONLoader.funcFromJSON();
    }
}
