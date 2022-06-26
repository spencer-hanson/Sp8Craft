package net.sp8craft.math.funcs;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.state.BlockState;

public class ConditionFunction extends Sp8Function {
    private Condition condition;
    private Sp8Function nextFunc;

    public interface Condition {
        public boolean isConditionMet(int x, int y, int z);
    }

    public ConditionFunction(Condition condition, Sp8Function nextFunc) {
        this.condition = condition;
        this.nextFunc = nextFunc;
    }

    @Override
    public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z) {
        return null;
    }
}
