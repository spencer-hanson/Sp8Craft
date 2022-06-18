package net.sp8craft.worldgen.funcs;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Sp8WorldGenFunction {
    public BlockPos.MutableBlockPos mutableBlockPos;

    public Sp8WorldGenFunction() {
        this.mutableBlockPos = new BlockPos.MutableBlockPos();
    }

    private int getSign(int num) {
        return num < 0 ? -1 : 1;
    }

    private int oppSign(int num) {
        return num < 0 ? 1 : -1;
    }

    private static int getSpiralIndex(int x, int y) {
        int index;
        if (y * y >= x * x) {
            index = (4 * y * y - y) - x;
            if (y < x) {
                index -= 2 * (y - x);
            }
        } else {
            index = (4 * x * x - y) - x;
            if (y < x) {
                index += 2 * (y - x);
            }
        }
        return index;
    }

    private BlockState getBlockState(int absX, int absY, int absZ, int relativeX, int relativeZ) {
        Expression ex = new ExpressionBuilder("3*x").build();
        ex.setVariable("x", relativeX);
        System.out.println("Evaluatin " + relativeX + " -> " + ex.evaluate());


        int funcRange = 13;

        int interval = 22;
        int baseY = 64;

        if (absY > baseY + funcRange || absY < baseY || absX == 0 || absZ == 0) {
            return Blocks.AIR.defaultBlockState();
        }

        absX = Math.abs(absX);
        absZ = Math.abs(absZ);
        funcRange -= 1; // subtract 1 to account for 0
        boolean rangePosX = (absX % interval) <= funcRange;
        boolean rangePosZ = (absZ % interval) <= funcRange;


        if (rangePosX && rangePosZ) {
            absZ %= interval;
            absX %= interval;
            int funcRadius = funcRange / 2;
            absZ = absZ - funcRadius;
            absX = absX - funcRadius;
            absY = (absY - baseY) - funcRadius;

            if (absY * absY + absX * absX + absZ * absZ <= funcRadius * funcRadius - 1) { // remove ball nipple
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
                    chunkAccess.setBlockState(
                            this.mutableBlockPos.set(absX, absY, absZ),
                            this.getBlockState(absX, absY, absZ, relativeX, relativeZ),
                            false
                    );
                }
            }
        }
    }


}
