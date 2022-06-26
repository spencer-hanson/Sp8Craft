package net.sp8craft.math.funcs;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;


public class FeatureFunction extends Sp8Function {
    private FeatureBoundFunc featureFunc;
    private BlockState result;

    public interface FeatureBoundFunc {
        public boolean isWithinFeature(int x, int y, int z);
    }

    public FeatureFunction(FeatureBoundFunc featureBoundFunc, BlockState result) {
        this.featureFunc = featureBoundFunc;
        this.result = result;
    }

    @Override
    public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z) {
        return Either.left(Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState());
    }
}
