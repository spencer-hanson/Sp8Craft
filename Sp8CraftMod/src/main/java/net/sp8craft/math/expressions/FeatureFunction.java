package net.sp8craft.math.expressions;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.state.BlockState;
import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;

import java.util.Optional;

public class FeatureFunction extends Sp8Function {

    public FeatureFunction() {

    }

    @Override
    public Either<Optional<BlockState>, Sp8Function> applyFunc(int x, int y, int z) {
        return Either.left(Optional.empty());
    }

//
//    public static FeatureFunction build(String data) {
//        return new FeatureFunction(Sp8Function.buildExpression(data));
//    }
}
