package net.sp8craft.math.funcs;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.registries.ForgeRegistries;
import net.sp8craft.math.expressions.FunctionEvaluator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


public class Sp8WorldGenFunction {
    public BlockPos.MutableBlockPos mutableBlockPos;
    public FunctionEvaluator funcEval;

    public Sp8WorldGenFunction() {
        this.mutableBlockPos = new BlockPos.MutableBlockPos();
//        this.funcEval = new FunctionEvaluator(
//                new File("expressions.java"),
//                () -> System.out.println("Detected update from elsewhere")
//        );
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

    public void setBlockState(ChunkAccess chunkAccess) {
        int baseY = chunkAccess.getMinBuildHeight();
        int maxY = chunkAccess.getMaxBuildHeight();

        int chunkX = chunkAccess.getPos().x;
        int chunkZ = chunkAccess.getPos().z;


        for (int relativeX = 0; relativeX < 16; relativeX++) {
            for (int relativeZ = 0; relativeZ < 16; relativeZ++) {
                int absX = relativeX + (chunkX * 16);
                int absZ = relativeZ + (chunkZ * 16);

                for (int absY = baseY; absY < maxY; absY++) {
                    int relX = absX % 16;
                    int relZ = absZ % 16;
                    int relY = absY - baseY;

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
