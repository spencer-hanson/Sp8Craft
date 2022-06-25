package net.sp8craft.math.expressions;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.state.BlockState;
import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;
import net.sp8craft.dependencies.net.objecthunter.exp4j.ExpressionBuilder;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsBoolean;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsMisc;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsSignal;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.OperatorsComparison;

import java.util.Optional;

public class Sp8Function {
    public Sp8Function() {

    }

    public Either<Optional<BlockState>, Sp8Function> applyFunc(int x, int y, int z) {
        return Either.left(Optional.empty());
    }

    public enum Sp8FunctionType {
        CONDITIONAL, // Used for detecting whether to apply the data function
        FEATURE // Returns feature
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
