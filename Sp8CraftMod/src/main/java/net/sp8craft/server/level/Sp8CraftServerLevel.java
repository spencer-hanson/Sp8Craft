package net.sp8craft.server.level;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.slf4j.Logger;
import java.util.List;
import java.util.concurrent.Executor;

public class Sp8CraftServerLevel extends ServerLevel {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Sp8CraftServerLevel(MinecraftServer p_203762_, Executor p_203763_, LevelStorageSource.LevelStorageAccess p_203764_, ServerLevelData p_203765_,
                               ResourceKey<Level> p_203766_, Holder<DimensionType> p_203767_, ChunkProgressListener p_203768_, ChunkGenerator p_203769_,
                               boolean p_203770_, long p_203771_, List<CustomSpawner> p_203772_, boolean p_203773_) {
        super(p_203762_, p_203763_, p_203764_, p_203765_, p_203766_, p_203767_, p_203768_, p_203769_, p_203770_, p_203771_, p_203772_, p_203773_);

        LOGGER.info("==============================================================");
        LOGGER.info("Creating custom Sp8Craft server level!");
        LOGGER.info("==============================================================");
    }
}
