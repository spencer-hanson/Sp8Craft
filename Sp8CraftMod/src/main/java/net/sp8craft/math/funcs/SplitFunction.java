package net.sp8craft.math.funcs;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public class SplitFunction extends Sp8Function {
    private ArrayList<Sp8Function> funcs;

    public SplitFunction(ArrayList<Sp8Function> conditionFunctions) {
        this.funcs = conditionFunctions;
    }

    @Override
    public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z) {
        return null;
    }
}
