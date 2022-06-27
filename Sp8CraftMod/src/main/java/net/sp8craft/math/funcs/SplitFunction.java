package net.sp8craft.math.funcs;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;

public class SplitFunction extends Sp8Function {
    private final List<Sp8Function> funcs;

    public SplitFunction(String name, List<Sp8Function> conditionFunctions) {
        super(name);
        this.funcs = conditionFunctions;
    }

    @Override
    public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z) {
        for (Sp8Function func : this.funcs) {
            Either<BlockState, Sp8Function> result = func.applyFunc(x, y, z);
            if (result.right().isPresent() && !result.right().get().isEmpty() || result.left().isPresent()) {
                return result;
            }
        }
        return Either.right(Sp8Function.EMPTY);
    }
}
