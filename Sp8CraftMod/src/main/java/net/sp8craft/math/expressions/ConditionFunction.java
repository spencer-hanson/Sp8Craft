package net.sp8craft.math.expressions;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.state.BlockState;
import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;

import java.util.Optional;

public class ConditionFunction extends Sp8Function {

    public ConditionFunction(String conditionExpression, Sp8Function func) {

    }

    @Override
    public Either<Optional<BlockState>, Sp8Function> applyFunc(int x, int y, int z) {
        return Either.left(Optional.empty());
    }


//    public static ConditionFunction build(String data) {
//        return new ConditionFunction(Sp8Function.buildExpression(data));
//    }
//
//    public boolean run() {
//
//    }
}
