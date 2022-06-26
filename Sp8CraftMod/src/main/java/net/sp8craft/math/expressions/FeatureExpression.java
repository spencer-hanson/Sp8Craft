package net.sp8craft.math.expressions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public record FeatureExpression(String expression, String result) {
    public BlockState getResult() {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(result));
        return Objects.requireNonNullElse(block, Blocks.DEEPSLATE_GOLD_ORE).defaultBlockState();
    }
}
