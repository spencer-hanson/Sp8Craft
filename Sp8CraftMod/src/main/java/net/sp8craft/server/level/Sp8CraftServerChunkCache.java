package net.sp8craft.server.level;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Either;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class Sp8CraftServerChunkCache extends ServerChunkCache {

    public Sp8CraftServerChunkCache(ServerLevel p_184009_, LevelStorageSource.LevelStorageAccess p_184010_, DataFixer p_184011_, StructureManager p_184012_, Executor p_184013_, ChunkGenerator p_184014_, int p_184015_, int p_184016_, boolean p_184017_, ChunkProgressListener p_184018_, ChunkStatusUpdateListener p_184019_, Supplier<DimensionDataStorage> p_184020_) {
        super(p_184009_, p_184010_, p_184011_, p_184012_, p_184013_, p_184014_, p_184015_, p_184016_, p_184017_, p_184018_, p_184019_, p_184020_);
    }

    @Override
    public @NotNull ChunkGenerator getGenerator() {
        return super.getGenerator();
    }

    @Override
    public @NotNull String getChunkDebugData(@NotNull ChunkPos pChunkPos) {
        return super.getChunkDebugData(pChunkPos);
    }

    @Override
    public @NotNull ChunkScanAccess chunkScanner() {
        return super.chunkScanner();
    }

    /**
     * Gets the chunk at the provided position, if it exists.
     * Note: This method <strong>can deadlock</strong> when called from within an existing chunk load, as it will be
     * stuck waiting for the current chunk to load!
     *
     * @param pLoad           If this should force a chunk load. When {@code false}, this will return null if the chunk is not
     */
    @Nullable
    @Override
    public ChunkAccess getChunk(int pChunkX, int pChunkZ, @NotNull ChunkStatus pRequiredStatus, boolean pLoad) {
        return super.getChunk(pChunkX, pChunkZ, pRequiredStatus, pLoad);
    }

    @Nullable
    @Override
    public LevelChunk getChunk(int pChunkX, int pChunkZ, boolean pLoad) {
        return super.getChunk(pChunkX, pChunkZ, pLoad);
    }


    @Nullable
    @Override
    public LevelChunk getChunkNow(int pChunkX, int pChunkZ) {
        return super.getChunkNow(pChunkX, pChunkZ);
    }

    @Override
    public @NotNull CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getChunkFuture(int p_8432_, int p_8433_, @NotNull ChunkStatus p_8434_, boolean p_8435_) {
        return super.getChunkFuture(p_8432_, p_8433_, p_8434_, p_8435_);
    }
}
