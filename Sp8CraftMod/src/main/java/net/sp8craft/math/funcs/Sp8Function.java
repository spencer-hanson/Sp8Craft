package net.sp8craft.math.funcs;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sp8craft.dependencies.net.objecthunter.exp4j.Expression;
import net.sp8craft.dependencies.net.objecthunter.exp4j.ExpressionBuilder;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsBoolean;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsMisc;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.FunctionsSignal;
import net.sp8craft.dependencies.net.objecthunter.exp4j.extras.OperatorsComparison;
import net.sp8craft.math.expressions.FeatureExpression;
import net.sp8craft.math.expressions.json.FunctionJSON;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Map;

public class Sp8Function {
    private static final Logger LOGGER = LogUtils.getLogger();
    private Func func;
    private boolean empty;

    public interface Func {
        public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z);
    }

    public Sp8Function(Func func) {
        this.func = func;
        this.empty = false;
    }

    public Sp8Function() {
        this.func = this::applyFunc;
        this.empty = true;
    }

    public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z) {
        return Either.left(Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState());
    }


    public static double evaluateExpression(int absX, int absY, int absZ, Expression expr) {
        int relX = absX % 16;
        int relZ = absZ % 16;

        int chunkX = (int)Math.floor(absX / 16f);
        int chunkZ = (int)Math.floor(absZ / 16f);

        return expr.setVariable("relX", relX)
                .setVariable("relZ", relZ)
                .setVariable("chunkX", chunkX)
                .setVariable("chunkZ", chunkZ)
                .setVariable("absX", absX)
                .setVariable("absY", absY)
                .setVariable("absZ", absZ)
                .evaluate();
    }

    public static Sp8Function fromFunctionJSON(FunctionJSON json) {
        ArrayList<Sp8Function> funcList = new ArrayList<>();

        for (Map.Entry<String, FeatureExpression> featureEntry : json.features().features().entrySet()) {
            String featureConditions = featureEntry.getKey(); // TODO Parse multiple here?
            String expr = json.conditionals().expressions().get(featureConditions);

            if (expr != null) {
                expr = json.vars().replaceWithVars(expr);
                Expression compiledConditionalExpr = Sp8Function.buildExpression(expr);
                Expression compiledFeatureExpr = Sp8Function.buildExpression(featureEntry.getValue().expression());

                funcList.add(new ConditionFunction(
                        (x, y, z) -> Sp8Function.evaluateExpression(x, y, z, compiledConditionalExpr) == 1.0,
                        new FeatureFunction(
                                (x, y, z) -> Sp8Function.evaluateExpression(x, y, z, compiledFeatureExpr) == 1.0,
                                featureEntry.getValue().getResult()
                        )
                ));
            } else {
                // TODO Errors
                LOGGER.warn("Conditional Expression from feature '" + featureConditions + "' returned Null!");
                funcList.add(new Sp8Function()); // default
            }
        }
        return new SplitFunction(funcList);
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
