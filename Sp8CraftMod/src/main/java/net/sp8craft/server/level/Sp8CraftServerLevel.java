package net.sp8craft.server.level;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class Sp8CraftServerLevel extends ServerLevel {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Sp8CraftServerLevel(MinecraftServer minecraftServer, Executor executor, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData,
                               ResourceKey<Level> levelResourceKey, Holder<DimensionType> dimensionTypeHolder, ChunkProgressListener chunkProgressListener, ChunkGenerator chunkGenerator,
                               boolean isDebug, long obfuscatedWorldSeed, List<CustomSpawner> customSpawnerList, boolean doTickTime) {
        super(minecraftServer, executor, levelStorageAccess, serverLevelData, levelResourceKey, dimensionTypeHolder, chunkProgressListener, chunkGenerator, isDebug, obfuscatedWorldSeed, customSpawnerList, doTickTime);

        LOGGER.info("==============================================================");
        LOGGER.info("Creating custom Sp8Craft server level '" + levelResourceKey + "'");
        LOGGER.info("==============================================================");

    }

    /*
    Will need to edit
    */
    @Override
    public void tickChunk(@NotNull LevelChunk pChunk, int pRandomTickSpeed) {
        super.tickChunk(pChunk, pRandomTickSpeed);
    }


    @Override
    public void startTickingChunk(@NotNull LevelChunk p_184103_) {
        super.startTickingChunk(p_184103_);
    }

    /**
     * Returns the name of the current chunk provider, by calling chunkprovider.makeString()
     */
    @Override
    public @NotNull String gatherChunkSourceStats() {
        return super.gatherChunkSourceStats();
    }

    @Nullable
    @Override
    public BlockGetter getChunkForCollisions(int pChunkX, int pChunkZ) {
        return super.getChunkForCollisions(pChunkX, pChunkZ);
    }

    @Override
    public void onStructureStartsAvailable(@NotNull ChunkAccess p_196558_) {
        super.onStructureStartsAvailable(p_196558_);
    }

    @Override
    public boolean shouldTickBlocksAt(long p_184059_) {
        return this.getChunkSource().chunkMap.getDistanceManager().inBlockTickingRange(p_184059_);
    }

    @Override
    public void save(@Nullable ProgressListener pProgress, boolean pFlush, boolean pSkipSave) {
        super.save(pProgress, pFlush, pSkipSave);
    }

    /**
     * Gets the biome at the given quart positions.
     * Note that the coordinates passed into this method are 1/4 the scale of block coordinates. The noise biome is then
     * SP8NOTE: BIOME ZOOMMER DOESNT EXIST
     * used by the @link net.minecraft.world.level.biome.BiomeZoomer to produce a biome for each unique position,
     * whilst only saving the biomes once per each 4x4x4 cube.
     */
    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int pX, int pY, int pZ) {
        return super.getNoiseBiome(pX, pY, pZ);
    }

    /**
     * Gets the world's chunk provider
     */
    @Override
    public @NotNull ServerChunkCache getChunkSource() {
        return super.getChunkSource();
    }


    @Override
    public @NotNull LevelChunk getChunkAt(@NotNull BlockPos pPos) {
        return super.getChunkAt(pPos);
    }

    @Override
    public @NotNull ChunkAccess getChunk(@NotNull BlockPos pPos) {
        return super.getChunk(pPos);
    }

    @Override
    public @NotNull LevelChunk getChunk(int pChunkX, int pChunkZ) {
        return super.getChunk(pChunkX, pChunkZ);
    }

    @Override
    public @NotNull ChunkAccess getChunk(int pChunkX, int pChunkZ, @NotNull ChunkStatus pRequiredStatus) {
        return super.getChunk(pChunkX, pChunkZ, pRequiredStatus);
    }

    @Nullable
    @Override
    public ChunkAccess getChunk(int pX, int pZ, @NotNull ChunkStatus pRequiredStatus, boolean pNonnull) {
        return super.getChunk(pX, pZ, pRequiredStatus, pNonnull);
    }

    @Override
    public @NotNull StructureManager getStructureManager() {
        return super.getStructureManager();
    }

    @Nullable
    @Override
    public BlockPos findNearestMapFeature(@NotNull TagKey<ConfiguredStructureFeature<?, ?>> pStructureTag, @NotNull BlockPos pPos, int pRadius, boolean pSkipExistingChunks) {
        return super.findNearestMapFeature(pStructureTag, pPos, pRadius, pSkipExistingChunks);
    }

    @Nullable
    @Override
    public Pair<BlockPos, Holder<Biome>> findNearestBiome(@NotNull Predicate<Holder<Biome>> pBiomePredicate, @NotNull BlockPos pPos, int pRadius, int pIncrement) {
        return super.findNearestBiome(pBiomePredicate, pPos, pRadius, pIncrement);
    }

    @Override
    public @NotNull ServerLevel getLevel() {
        return super.getLevel();
    }

    @Override
    public @NotNull DimensionType dimensionType() {
        return super.dimensionType();
    }

    /*
    Methods might need stuff
    */
    @Override
    public int getMinBuildHeight() {
        return super.getMinBuildHeight();
    }

    @Override
    public int getMaxBuildHeight() {
        return super.getMaxBuildHeight();
    }

    @Override
    public int getSectionsCount() {
        return super.getSectionsCount();
    }

    @Override
    public int getMinSection() {
        return super.getMinSection();
    }

    @Override
    public int getMaxSection() {
        return super.getMaxSection();
    }

    @NotNull
    @Override
    public MinecraftServer getServer() {
        return super.getServer();
    }

    @Override
    public boolean isOutsideBuildHeight(@NotNull BlockPos pPos) {
        return super.isOutsideBuildHeight(pPos);
    }

    @Override
    public boolean ensureCanWrite(@NotNull BlockPos pPos) {
        return super.ensureCanWrite(pPos);
    }

    @Override
    public void setCurrentlyGenerating(@Nullable Supplier<String> p_186618_) {
        super.setCurrentlyGenerating(p_186618_);
    }

    @Override
    public @NotNull String toString() {
        return super.toString();
    }

    @Override
    public boolean isVillage(@NotNull BlockPos pPos) {
        return super.isVillage(pPos);
    }

    @Override
    public boolean isVillage(@NotNull SectionPos pPos) {
        return super.isVillage(pPos);
    }

    @Override
    public boolean isCloseToVillage(@NotNull BlockPos pPos, int pSections) {
        return super.isCloseToVillage(pPos, pSections);
    }

    @Override
    public int sectionsToVillage(@NotNull SectionPos pPos) {
        return super.sectionsToVillage(pPos);
    }

    @Override
    public boolean setChunkForced(int pChunkX, int pChunkZ, boolean pAdd) {
        return super.setChunkForced(pChunkX, pChunkZ, pAdd);
    }

    @Override
    public @NotNull LongSet getForcedChunks() {
        return super.getForcedChunks();
    }

    @Override
    public @NotNull StructureFeatureManager structureFeatureManager() {
        return super.structureFeatureManager();
    }

    @Override
    public boolean mayInteract(@NotNull Player pPlayer, @NotNull BlockPos pPos) {
        return super.mayInteract(pPlayer, pPos);
    }

    @Override
    public void unload(@NotNull LevelChunk pChunk) {
        super.unload(pChunk);
    }

    @Override
    public boolean hasChunk(int pChunkX, int pChunkZ) {
        return super.hasChunk(pChunkX, pChunkZ);
    }

    @Override
    public @NotNull BlockPos getHeightmapPos(Heightmap.@NotNull Types pHeightmapType, @NotNull BlockPos pPos) {
        return super.getHeightmapPos(pHeightmapType, pPos);
    }

    @Override
    public boolean isHumidAt(@NotNull BlockPos pPos) {
        return super.isHumidAt(pPos);
    }

    @Override
    public @NotNull BiomeManager getBiomeManager() {
        return super.getBiomeManager();
    }

    @Override
    public @NotNull Holder<Biome> getBiome(@NotNull BlockPos pPos) {
        return super.getBiome(pPos);
    }
    /**
     * Check if precipitation is currently happening at a position
     */
    @Override
    public boolean isRainingAt(@NotNull BlockPos pPosition) {
        return super.isRainingAt(pPosition);
    }
}
