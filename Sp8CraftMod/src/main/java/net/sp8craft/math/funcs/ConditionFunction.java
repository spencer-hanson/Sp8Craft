package net.sp8craft.math.funcs;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.state.BlockState;

public class ConditionFunction extends Sp8Function {
    private final Condition condition;
    private final Sp8Function nextFunc;

    public interface Condition {
        public boolean isConditionMet(int x, int y, int z);
    }

    public ConditionFunction(String name, Condition condition, Sp8Function nextFunc) {
        super(name);
        this.condition = condition;
        this.nextFunc = nextFunc;
    }

    @Override
    public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z) {
        if (this.condition.isConditionMet(x, y, z)) {
            return Either.right(nextFunc);
        } else {
            return Either.right(Sp8Function.EMPTY);
        }
    }
}
