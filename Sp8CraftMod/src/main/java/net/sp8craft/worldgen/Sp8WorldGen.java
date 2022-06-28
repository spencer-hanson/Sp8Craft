package net.sp8craft.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.sp8craft.math.expressions.FunctionJSONLoader;
import net.sp8craft.math.funcs.ConditionFunction;
import net.sp8craft.math.funcs.FeatureFunction;
import net.sp8craft.math.funcs.Sp8Function;
import net.sp8craft.math.funcs.SplitFunction;

import java.util.Arrays;
import java.util.Optional;


public class Sp8WorldGen {
    public BlockPos.MutableBlockPos mutableBlockPos;
    private Sp8Function jsonFunction;

    private XoroshiroRandomSource randomSource;
    private SimplexNoise simplexNoise;

    public Sp8WorldGen(long seed) {
        this.mutableBlockPos = new BlockPos.MutableBlockPos();
        this.randomSource = new XoroshiroRandomSource(seed);
        this.simplexNoise = new SimplexNoise(this.randomSource);

//        this.funcEval = new FunctionEvaluator(
//                new File("expressions.java"),
//                () -> System.out.println("Detected update from elsewhere")
//        );
        //https://redmine.riddler.com.ar/projects/exp4j/wiki/Extra_Functions_and_Operators
        this.reloadJSON();
// add alternate 'empty' need word for it
        // multi-block returns?
        // names for functions
        // possibly skipping to decrease usage?

    }

    public void reloadJSON() {
//        this.jsonFunction = FunctionJSONLoader.funcFromJSON();

        this.jsonFunction = new SplitFunction("worldgen-start", Arrays.asList(
                new FeatureFunction(
                        "simplex",
                        (x, y, z) -> {

                            double val1 = 10 * this.simplexNoise.getValue(x/10f, z/10f);
                            if(y<val1) { return true; }

                            return false;
                        },
                        Blocks.STONE.defaultBlockState())
//                new ConditionFunction(
//                        "within_asteroid",
//                        ((x, y, z) -> {
//                            x = Math.abs(x);
//                            z = Math.abs(z);
//                            int rX = x % 16;
//                            int rZ = z % 16;
//                            return rX <= 6 && rZ <= 6;
//                        }),
//                        new FeatureFunction(
//                                "asteroid_sphere",
//                                ((x, y, z) -> {
//                                    x = Math.abs(x);
//                                    z = Math.abs(z);
//                                    int rX = x % 16;
//                                    int rZ = z % 16;
//                                    rZ = rZ - 6;
//                                    rX = rX - 6;
//                                    return (y * y + rX * rX + rZ * rZ <= 35);
//                                }), Blocks.MYCELIUM.defaultBlockState()
//                        )
//                )
        ));
    }

    private BlockState getBlockState(int absX, int absY, int absZ, int relativeX, int relativeZ) {
        int funcRange = 13;

        int interval = 22;
        int baseY = 64;

        if (absY > baseY + funcRange || absY < baseY || absX == 0 || absZ == 0) {
            return Blocks.AIR.defaultBlockState();
        }

        absX = Math.abs(absX);
        absZ = Math.abs(absZ);

        boolean rangePosX = (absX % interval) <= funcRange;
        boolean rangePosZ = (absZ % interval) <= funcRange;


        if (rangePosX && rangePosZ) {
            absZ %= interval;
            absX %= interval;
            int funcRadius = funcRange / 2;
            absZ = absZ - funcRadius;
            absX = absX - funcRadius;
            absY = (absY - baseY) - funcRadius;

            if (absY * absY + absX * absX + absZ * absZ <= funcRadius * funcRadius - 1) { // remove ball nipple by subtracting 1
                return Blocks.EMERALD_BLOCK.defaultBlockState();
            }
        }
        return Blocks.AIR.defaultBlockState();
    }

    public void setBlockState(ChunkAccess chunkAccess, Optional<ServerLevel> level) {
        int baseY = chunkAccess.getMinBuildHeight();
        int maxY = chunkAccess.getMaxBuildHeight();

        int chunkX = chunkAccess.getPos().x;
        int chunkZ = chunkAccess.getPos().z;


        for (int relativeX = 0; relativeX < 16; relativeX++) {
            for (int relativeZ = 0; relativeZ < 16; relativeZ++) {
                int absX = relativeX + (chunkX * 16);
                int absZ = relativeZ + (chunkZ * 16);

                for (int absY = baseY; absY < maxY; absY++) {
                    BlockState blockState = Sp8Function.evaluateFunction(this.jsonFunction, absX, absY, absZ);
                    this.mutableBlockPos.set(absX, absY, absZ);

                    level.ifPresent(serverLevel -> {
                        serverLevel.setBlock(this.mutableBlockPos.immutable(), blockState, 2 | 4 | 16 | 32);
                    });


                    chunkAccess.setBlockState(
                            this.mutableBlockPos,
                            blockState,
                            false
                    );

//                    ArrayList<String> initialValues = new ArrayList<>(Arrays.asList(
//                            "boolean done = false;",
//                            "String result = \"air\";",
//                            "int baseY = " + baseY + ";",
//                            "int absY = " + absY + ";",
//                            "int absX = " + absX + ";",
//                            "int absZ = " + absZ + ";",
//                            "int relX = " + relX + ";",
//                            "int relZ = " + relZ + ";",
//                            "int relY = " + relY + ";",
//                            "int chunkX = " + chunkX + ";",
//                            "int chunkZ = " + chunkZ + ";"
//                    ));

//                    Optional<String> val = this.funcEval.runFile(initialValues);
//                    if (val.isPresent()) {
//                        this.getBlockState(absX, absY, absZ, relativeX, relativeZ)
//                        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(val.get()));

//                        chunkAccess.setBlockState(
//                                this.mutableBlockPos.set(absX, absY, absZ),
//                                Objects.requireNonNullElse(block, Blocks.DEEPSLATE_GOLD_ORE).defaultBlockState(),
//                                false
//                        );
//                    }

                }
            }
        }
    }
}
