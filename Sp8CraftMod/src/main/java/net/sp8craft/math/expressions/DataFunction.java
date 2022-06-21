package net.sp8craft.math.expressions;

import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;

public class DataFunction extends Sp8Function {

    private DataFunction(Expression expression) {
        super(expression, Sp8FunctionType.DATA);
    }

    public static DataFunction build(String data) {
        return new DataFunction(Sp8Function.buildExpression(data));
    }
}
