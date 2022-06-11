package net.sp8craft.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.common.world.ForgeWorldPreset;
import net.sp8craft.worldgen.biomes.Sp8CraftBiomeManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class Sp8CraftChunkGenerator extends ChunkGenerator {
    private long seed;
    protected final Registry<StructureSet> structureSets;

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
    }

    public Sp8CraftChunkGenerator(Registry<StructureSet> pStructureSets, Optional<HolderSet<StructureSet>> pStructureOverrides, BiomeSource pBiomeSource) {
        // TODO Generate a seed for the non-seed generated constructor
        this(pStructureSets, pBiomeSource, 8);
    }

    public Sp8CraftChunkGenerator(Registry<StructureSet> pStructureSets, Optional<HolderSet<StructureSet>> pStructureOverrides, BiomeSource pBiomeSource, BiomeSource pRuntimeBiomeSource, long pRingPlacementSeed) {
        this(pStructureSets, pStructureOverrides, pBiomeSource);
    }

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


            return new Sp8CraftChunkGenerator(
                    structureSet,
//                    new FixedBiomeSource(settings.getBiomeFromSettings()),
                    new FixedBiomeSource(
                            biomeRegistry.getOrCreateHolder(Sp8CraftBiomeManager.SP8_BIOME_KEY)
                    ),
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


    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(
            @NotNull Executor executor,
            @NotNull Blender blender,
            @NotNull StructureFeatureManager structureFeatureManager,
            ChunkAccess chunkAccess) {

        List<BlockState> list = new ArrayList<>();
        list.add(Blocks.DIAMOND_BLOCK.defaultBlockState());
        list.add(Blocks.GOLD_BLOCK.defaultBlockState());
        list.add(Blocks.IRON_BLOCK.defaultBlockState());


        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        Heightmap heightmap = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmap1 = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);

//        for (int y = 0;y < Math.min(chunkAccess.getHeight(), l))
        int baseY = chunkAccess.getMinBuildHeight();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();


        BlockState blockState = Blocks.GRASS_BLOCK.defaultBlockState();
        double twoPi = (Math.PI * 2);
        double period = 32;
        double scale = twoPi / period;
        double roundFactor = 10000;
        int amplitude = 32;

        int chunkX = chunkAccess.getPos().x;
        int chunkZ = chunkAccess.getPos().z;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double blockX = x + (chunkX * 16);
                double blockZ = z + (chunkZ * 16);
                double blockY = (amplitude * Math.round((Math.sin(Math.sqrt(blockX*blockX + blockZ*blockZ) * scale) / twoPi * roundFactor)) / roundFactor);
                blockY += amplitude; // Offset from 0


                int intY = (int)Math.floor(blockY);

                chunkAccess.setBlockState(
                        mutableBlockPos.set(
                                x,
                                intY,
                                z
                        ),
                        blockState,
                        false
                );

//                heightmap.update(
//                        x,
//                        intY,
//                        z,
//                        blockState
//                );
//
//                heightmap1.update(
//                        x,
//                        intY,
//                        z,
//                        blockState
//                );
            }
        }

//        for (int i = 0; i < Math.min(chunkAccess.getHeight(), list.size()); ++i) {
//            BlockState blockstate = list.get(i);
//            if (blockstate != null) {
//                int j = chunkAccess.getMinBuildHeight() + i;
//
//                for (int k = 0; k < 16; ++k) {
//                    for (int l = 0; l < 16; ++l) {
//                        chunkAccess.setBlockState(blockpos$mutableblockpos.set(k, j, l), blockstate, false);
//                        heightmap.update(k, j, l, blockstate);
//                        heightmap1.update(k, j, l, blockstate);
//                    }
//                }
//            }
//        }

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
    public ChunkGenerator withSeed(long pSeed) {
        return new Sp8CraftChunkGenerator(this.structureSets, this.biomeSource, pSeed);
    }

    @Override
    public Climate.Sampler climateSampler() {
        return Climate.empty();
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, BiomeManager pBiomeManager, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) {
        // TODO?
    }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk) {
        // TODO?
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) {
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
}
