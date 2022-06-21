package net.sp8craft.math.expressions;

import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;
import net.sp8craft.dependencies.net.objecthunter.exp4j.ExpressionBuilder;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsBoolean;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsMisc;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsSignal;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.OperatorsComparison;
import net.sp8craft.dependencies.net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

public class Sp8Function {
    public Expression expression;
    public Sp8FunctionType type;

    protected Sp8Function(Expression expression, Sp8FunctionType typ) {
        this.type = typ;
        this.expression = expression;
    }

    public static class InvalidFunctionTypeException extends Exception {}
    public enum Sp8FunctionType {
        CONDITIONAL, // Used for detecting whether to apply the data function
        DATA // Applies func and gets block type?
    }
    public static String typeToString(Sp8FunctionType typ) {
        return switch (typ) {
            case DATA -> "data";
            case CONDITIONAL -> "conditional";
        };
    }

    public static Sp8FunctionType stringToType(String val) throws InvalidFunctionTypeException {
        return switch(val) {
            case "data" -> Sp8FunctionType.DATA;
            case "conditional" -> Sp8FunctionType.CONDITIONAL;
            default -> throw new InvalidFunctionTypeException();
        };
    }

    public static Expression buildExpression(String data) throws IllegalArgumentException {


        return new ExpressionBuilder(data)
                .functions(FunctionsBoolean.getFunctions())
                .functions(FunctionsMisc.getFunctions())
                .functions(FunctionsSignal.getFunctions())
                .operators(OperatorsComparison.getOperators())
                .build();
    }
}
