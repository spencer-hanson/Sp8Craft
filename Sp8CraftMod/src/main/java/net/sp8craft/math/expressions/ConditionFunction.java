package net.sp8craft.math.expressions;

import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;

public class ConditionFunction extends Sp8Function {

    public ConditionFunction(Expression expression) {
        super(expression, Sp8FunctionType.CONDITIONAL);
    }

    public static ConditionFunction build(String data) {
        return new ConditionFunction(Sp8Function.buildExpression(data));
    }
}
