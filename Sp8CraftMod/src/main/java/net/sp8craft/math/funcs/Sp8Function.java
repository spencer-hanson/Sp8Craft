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
import net.sp8craft.math.expressions.json.VarsJSON;
import net.sp8craft.util.Sp8CraftUtil;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Sp8Function {
    public static Sp8Function EMPTY = new Sp8Function();
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Func func;
    private boolean empty;
    public String name;
    public static boolean shownError = false;

    public interface Func {
        public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z);
    }

    public Sp8Function(Func func, String name) {
        this.func = func;
        this.name = name;
        this.empty = false;
    }

    public Sp8Function(String name) {
        this((x, y, z) -> Either.left(Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState()), name);
    }

    private Sp8Function() {
        this("empty");
        this.empty = true;
    }

    public Either<BlockState, Sp8Function> applyFunc(int x, int y, int z) {
        return this.func.applyFunc(x, y, z);
    }


    public static double evaluateExpression(int absX, int absY, int absZ, Expression expr) {
        int relX = absX % 16;
        int relZ = absZ % 16;

        int chunkX = (int) Math.floor(absX / 16f);
        int chunkZ = (int) Math.floor(absZ / 16f);
        try {
            return expr.setVariable("rX", relX, false)
                    .setVariable("rZ", relZ, false)
                    .setVariable("chunkX", chunkX, false)
                    .setVariable("chunkZ", chunkZ, false)
                    .setVariable("aX", absX, false)
                    .setVariable("aY", absY, false)
                    .setVariable("aZ", absZ, false)
                    .setVariable("spiralIndex", Sp8CraftUtil.Math.getSpiralIndex(chunkX, chunkZ), false)
                    .evaluate();

        } catch (Exception e) {
            if(!shownError) {
                e.printStackTrace();
                LOGGER.error("Error evaluating expression " + expr.toString());
                shownError = true;
            }
            return 0.0;
        }
    }

    public static Sp8Function fromFunctionJSON(FunctionJSON json) {
        ArrayList<Sp8Function> funcList = new ArrayList<>();

        for (Map.Entry<String, FeatureExpression> featureEntry : json.features().features().entrySet()) {
            String featureConditions = featureEntry.getKey(); // TODO Parse multiple here?
            String conditionalExprStr = json.conditionals().expressions().get(featureConditions);

            if (conditionalExprStr != null) {
                conditionalExprStr = json.vars().replaceWithVars(conditionalExprStr);
                String featureExprStr = json.vars().replaceWithVars(featureEntry.getValue().expression());

                Expression compiledConditionalExpr = Sp8Function.buildExpression(conditionalExprStr, json.vars());
                Expression compiledFeatureExpr = Sp8Function.buildExpression(featureExprStr, json.vars());

                funcList.add(new ConditionFunction("condition-" + featureConditions,
                        (x, y, z) -> Sp8Function.evaluateExpression(x, y, z, compiledConditionalExpr) == 1.0,
                        new FeatureFunction("feature-" + featureEntry,
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
        return new SplitFunction("json-start-split", funcList);
    }

    public static BlockState evaluateFunction(Sp8Function func, int x, int y, int z) {
        Either<BlockState, Sp8Function> result = func.applyFunc(x, y, z);

        if (result.left().isPresent()) {
            return result.left().get();
        }
        Sp8Function nextFunc = result.right().get();
        if (nextFunc.isEmpty()) {
//            LOGGER.error("Function '" + nextFunc + "' returned empty! Returning airblock");
            return Blocks.AIR.defaultBlockState();
        }
        return Sp8Function.evaluateFunction(nextFunc, x, y, z);
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public static Expression buildExpression(String data, VarsJSON vars) {
        try {
            ExpressionBuilder exBuilder = new ExpressionBuilder(data)
                    .functions(FunctionsBoolean.getFunctions())
                    .functions(FunctionsMisc.getFunctions())
                    .functions(FunctionsSignal.getFunctions())
                    .operators(OperatorsComparison.getOperators())
                    .variables("aY", "aX", "aZ", "rX", "rZ", "chunkX", "chunkZ", "spiralIndex");
            HashSet<String> varNames = new HashSet<>();
            for (Map.Entry<String, String> entry : vars.vars().entrySet()) {
                varNames.add(entry.getKey());
            }
            exBuilder.variables(varNames);

            return exBuilder.build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LOGGER.error("Error parsing expression data: \n\n" + data + "\n\n");
            return new ExpressionBuilder("1.0").build();
        }
    }
}
