package net.sp8craft.math.funcs;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.state.BlockState;


public class FeatureFunction extends Sp8Function {
    private final FeatureBoundFunc featureFunc;
    private final BlockState result;

    public interface FeatureBoundFunc {
        public boolean isWithinFeature(int x, int y, int z);
    }

    public FeatureFunction(FeatureBoundFunc featureBoundFunc, BlockState result) {
        this.featureFunc = featureBoundFunc;
        this.result = result;
    }

    @Override
    public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z) {
        if (this.featureFunc.isWithinFeature(x, y, z)) {
            return Either.left(this.result);
        } else {
            return Either.right(Sp8Function.EMPTY);
        }
    }
}
