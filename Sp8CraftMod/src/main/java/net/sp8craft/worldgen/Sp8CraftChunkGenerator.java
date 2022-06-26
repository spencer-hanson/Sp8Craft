package net.sp8craft.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.common.world.ForgeWorldPreset;
import net.sp8craft.worldgen.biomes.Sp8CraftBiomeManager;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class Sp8CraftChunkGenerator extends ChunkGenerator {
    private long seed;
    protected final Registry<StructureSet> structureSets;
    private Sp8WorldGen genFunc;


    public static final Codec<Sp8CraftChunkGenerator> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                            RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter((inst) -> inst.structureSets),
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
                            Codec.LONG.fieldOf("seed").stable().forGetter((generator) -> generator.seed))
                    .apply(instance, instance.stable(Sp8CraftChunkGenerator::new)));
    // Codec corresponds to the Constructor

    public Sp8CraftChunkGenerator(Registry<StructureSet> structureSets, BiomeSource biomeSource, long seed) {
        super(structureSets, Optional.empty(), biomeSource);
        this.seed = seed;
        this.structureSets = structureSets;
        this.genFunc = new Sp8WorldGen();
    }

    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(
            @NotNull Executor executor,
            @NotNull Blender blender,
            @NotNull StructureFeatureManager structureFeatureManager,
            @NotNull ChunkAccess chunkAccess) {

        this.genFunc.setBlockState(chunkAccess);
        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public @NotNull NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pLevel) {
        // Looks like something to do with generating structures?
        // Seems to only be called for Nether Fossils and Ruined portals
        //
        List<BlockState> list = new ArrayList<>();
        list.add(Blocks.EMERALD_BLOCK.defaultBlockState());

        return new NoiseColumn(pLevel.getMinBuildHeight(), list
                .stream()
                .limit((long) pLevel.getHeight())
                .map((block) -> block == null ? Blocks.AIR.defaultBlockState() : block)
                .toArray(BlockState[]::new)
        );
    }

    @Override
    protected @NotNull Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public @NotNull ChunkGenerator withSeed(long pSeed) {
        return new Sp8CraftChunkGenerator(this.structureSets, this.biomeSource, pSeed);
    }

    @Override
    public Climate.@NotNull Sampler climateSampler() {
        return Climate.empty();
    }

    @Override
    public void applyCarvers(@NotNull WorldGenRegion pLevel, long pSeed, @NotNull BiomeManager pBiomeManager, @NotNull StructureFeatureManager pStructureFeatureManager, @NotNull ChunkAccess pChunk, GenerationStep.@NotNull Carving pStep) {
        // TODO?
    }

    @Override
    public void buildSurface(@NotNull WorldGenRegion pLevel, @NotNull StructureFeatureManager pStructureFeatureManager, @NotNull ChunkAccess pChunk) {
        // TODO?
    }

    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion pLevel) {
        // TODO?
    }

    @Override
    public int getGenDepth() {
        return 384; // from FlatLevelSource
        //        return 0;
    }

    @Override
    public int getSeaLevel() {

        //        return -63; // From FlatLevelSource
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int pX, int pZ, Heightmap.@NotNull Types pType, LevelHeightAccessor pLevel) {
        return pLevel.getMinBuildHeight();
//        return 0;
    }

    @Override
    public void addDebugScreenInfo(@NotNull List<String> p_208054_, @NotNull BlockPos p_208055_) {
        // TODO?
    }

    // Chunk Generator Class
    public static class Sp8ChunkFactory implements ForgeWorldPreset.IBasicChunkGeneratorFactory {
        @Override
        public ChunkGenerator createChunkGenerator(RegistryAccess registryAccess, long seed) {
            Registry<StructureSet> structureSet = registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY);
            Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);

//            FlatLevelGeneratorSettings settings = FlatLevelGeneratorSettings.getDefault(biomeRegistry, structureSet);

//            HolderSet<StructureSet> holderset = HolderSet.direct(
//                    structureSet.getHolderOrThrow(BuiltinStructureSets.STRONGHOLDS),
//                    structureSet.getHolderOrThrow(BuiltinStructureSets.VILLAGES)
//            );
//            FlatLevelGeneratorSettings flatlevelgeneratorsettings = new FlatLevelGeneratorSettings(Optional.of(holderset), pBiomes);
//            flatlevelgeneratorsettings.biome = pBiomes.getOrCreateHolder(Biomes.PLAINS);
//            flatlevelgeneratorsettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
//            flatlevelgeneratorsettings.getLayersInfo().add(new FlatLayerInfo(2, Blocks.DIRT));
//            flatlevelgeneratorsettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
//            flatlevelgeneratorsettings.updateLayers();

            Registry<NormalNoise.NoiseParameters> noiseRegistry = registryAccess.registryOrThrow(Registry.NOISE_REGISTRY);
            Holder<NoiseGeneratorSettings> noiseSettings = registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY)
                    .getOrCreateHolder(NoiseGeneratorSettings.END);//NoiseGeneratorSettings.FLOATING_ISLANDS);


//            return new Sp8CraftChunkGenerator(
//                    structureSet,
////                new FixedBiomeSource(settings.getBiomeFromSettings()),
//                    new FixedBiomeSource(biomeRegistry.getOrCreateHolder(Sp8CraftBiomeManager.SP8_BIOME_KEY)),
//                    seed,
//                    noiseRegistry,
//                    noiseSettings
//            );
//            (Registry<StructureSet> p_209106_, Registry<NormalNoise.NoiseParameters> p_209107_, BiomeSource p_209108_, long p_209109_, Holder<NoiseGeneratorSettings> p_209110_)
            return new Sp8CraftChunkGenerator(
                    structureSet,
//                    new FixedBiomeSource(settings.getBiomeFromSettings()),
                    new FixedBiomeSource(biomeRegistry.getOrCreateHolder(Sp8CraftBiomeManager.SP8_BIOME_KEY)),
                    seed
            );
        }

        @Override
        public ChunkGenerator createChunkGenerator(RegistryAccess registryAccess, long seed, String generatorSettings) {
            return ForgeWorldPreset.IBasicChunkGeneratorFactory.super.createChunkGenerator(registryAccess, seed, generatorSettings);
        }

        @Override
        public WorldGenSettings createSettings(RegistryAccess dynamicRegistries, long seed, boolean generateStructures, boolean bonusChest, String generatorSettings) {
            return ForgeWorldPreset.IBasicChunkGeneratorFactory.super.createSettings(dynamicRegistries, seed, generateStructures, bonusChest, generatorSettings);
        }
    }
}
