package net.sp8craft.world.level.chunk;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.sp8craft.config.Sp8CraftConfig;
import net.sp8craft.util.ConditionalList;
import net.sp8craft.util.Sp8CraftUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.allOf;

public class Sp8CraftChunkStatus extends ChunkStatus {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final List<ChunkStatus> DEFAULT_CHUNKSTATUS_LIST = ImmutableList.of(ChunkStatus.FULL,
            ChunkStatus.FEATURES,
            ChunkStatus.LIQUID_CARVERS,
            ChunkStatus.BIOMES,
            ChunkStatus.STRUCTURE_STARTS,
            ChunkStatus.STRUCTURE_STARTS,
            ChunkStatus.STRUCTURE_STARTS,
            ChunkStatus.STRUCTURE_STARTS,
            ChunkStatus.STRUCTURE_STARTS,
            ChunkStatus.STRUCTURE_STARTS,
            ChunkStatus.STRUCTURE_STARTS,
            ChunkStatus.STRUCTURE_STARTS
    );

    public static final List<ChunkStatus> SP8_CHUNKSTATUS_LIST = ImmutableList.of(Sp8CraftChunkStatus.SP8CRAFT_FULL);

//    public static ConditionalList<ChunkStatus> createConditionalChunkStatusList() {
//        return new ConditionalList<ChunkStatus>(SP8_CHUNKSTATUS_LIST, DEFAULT_CHUNKSTATUS_LIST,
//                () -> Sp8CraftConfig.BOOL_CONFIGS.get(Sp8CraftConfig.KEY_USE_WORLDGEN),
//                Sp8CraftChunkStatus.SP8CRAFT_FULL);
//    }

    public Sp8CraftChunkStatus(String pName, @Nullable ChunkStatus pParent, int pRange, EnumSet<Heightmap.Types> pHeightmapsAfter, ChunkType pChunkType, GenerationTask pGenerationTask, LoadingTask pLoadingTask) {
        super(pName, pParent, pRange, pHeightmapsAfter, pChunkType, pGenerationTask, pLoadingTask);
    }
//    public static String SP8CRAFT_FULL_NAME = "sp8craft_full";


    public static @NotNull CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getLightChunkFuture(
            ChunkStatus chunkStatus, ThreadedLevelLightEngine threadedLevelLightEngine, ChunkAccess chunkAccess) {
        boolean isChunkLighted = chunkAccess.getStatus().isOrAfter(chunkStatus) && chunkAccess.isLightCorrect();
        if (!chunkAccess.getStatus().isOrAfter(chunkStatus)) {
            ((ProtoChunk) chunkAccess).setStatus(chunkStatus);
        }

        return threadedLevelLightEngine.lightChunk(chunkAccess, isChunkLighted).thenApply(Either::left);
    }

    public interface SimpleDoWorkFunc {
        void doWork(ChunkStatus pStatus, ServerLevel pLevel, ChunkGenerator pGenerator, List<ChunkAccess> pChunks, ChunkAccess pLoadingChunk);
    }

    public static CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> simpleDoWork(
            ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel,
            ChunkGenerator generator, StructureManager structureManager, ThreadedLevelLightEngine lightEngine,
            Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> accessCompletableFutureFunction,
            List<ChunkAccess> chunkAccessList, ChunkAccess chunkAccess,
            boolean unknownFlag, // TODO figure out what this param is lmao
            SimpleDoWorkFunc work
    ) {
        if (unknownFlag || !chunkAccess.getStatus().isOrAfter(chunkStatus)) {

            work.doWork(chunkStatus, serverLevel, generator, chunkAccessList, chunkAccess);

            if (chunkAccess instanceof ProtoChunk) {
                ProtoChunk protochunk = (ProtoChunk) chunkAccess;
                protochunk.setStatus(chunkStatus);
            }
        }

        return CompletableFuture.completedFuture(Either.left(chunkAccess));
    }

    public static @NotNull ChunkStatus.GenerationTask getGenTask(String name) {

//        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>

        //ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
        //                StructureManager structureManager,
        //                ThreadedLevelLightEngine threadedLevelLightEngine,
        //                Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
        //                List<ChunkAccess> chunkAccessList,
        //                ChunkAccess chunkAccess,
        //                boolean  unknownFlag // TODO figure out what this param is lmao


        // FULL
        ChunkStatus.GenerationTask fullGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                return CompletableFuture.completedFuture(Either.left(chunkAccess));
            }
        };

        // HEIGHTMAPS - Nothing

        // SPAWN
        ChunkStatus.GenerationTask spawnGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                return simpleDoWork(
                        chunkStatus, executor, serverLevel,
                        generator, structureManager, threadedLevelLightEngine,
                        chunkAccessCompletableFutureFunction,
                        chunkAccessList, chunkAccess,
                        unknownFlag,
                        (ChunkStatus pStatus, ServerLevel pLevel, ChunkGenerator pGenerator, List<ChunkAccess> pChunks, ChunkAccess pLoadingChunk) -> { // SPAWN
                            if (!chunkAccess.isUpgrading()) {
                                generator.spawnOriginalMobs(new WorldGenRegion(
                                        serverLevel,
                                        chunkAccessList,
                                        chunkStatus,
                                        -1 // writeRadiusCutoff
                                ));
                            }
                        }
                );
            }
        };

        // LIGHT
        ChunkStatus.GenerationTask lightGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                return getLightChunkFuture(chunkStatus, threadedLevelLightEngine, chunkAccess);
            }
        };

        // FEATURES
        ChunkStatus.GenerationTask featuresGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                ProtoChunk protochunk = (ProtoChunk) chunkAccess;
                protochunk.setLightEngine(threadedLevelLightEngine);
                if (unknownFlag || !chunkAccess.getStatus().isOrAfter(chunkStatus)) {
                    Heightmap.primeHeightmaps(chunkAccess, EnumSet.of(Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE));
                    WorldGenRegion worldgenregion = new WorldGenRegion(serverLevel, chunkAccessList, chunkStatus, 1);
                    generator.applyBiomeDecoration(worldgenregion, chunkAccess, serverLevel.structureFeatureManager().forWorldGenRegion(worldgenregion));
                    Blender.generateBorderTicks(worldgenregion, chunkAccess);
                    protochunk.setStatus(chunkStatus);
                }
                return CompletableFuture.completedFuture(Either.left(chunkAccess));
            }
        };

        // LIQUID CARVERS - Nothing

        // CARVERS
        ChunkStatus.GenerationTask carversGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                return simpleDoWork(
                        chunkStatus, executor, serverLevel,
                        generator, structureManager, threadedLevelLightEngine,
                        chunkAccessCompletableFutureFunction,
                        chunkAccessList, chunkAccess,
                        unknownFlag,
                        (ChunkStatus pStatus, ServerLevel pLevel, ChunkGenerator pGenerator, List<ChunkAccess> pChunks, ChunkAccess pLoadingChunk) -> {

                            WorldGenRegion worldgenregion = new WorldGenRegion(serverLevel, chunkAccessList, chunkStatus, 0);
                            if (chunkAccess instanceof ProtoChunk) {
                                ProtoChunk protochunk = (ProtoChunk) chunkAccess;
                                Blender.addAroundOldChunksCarvingMaskFilter(worldgenregion, protochunk);
                            }

                            generator.applyCarvers(worldgenregion, serverLevel.getSeed(), serverLevel.getBiomeManager(), serverLevel.structureFeatureManager().forWorldGenRegion(worldgenregion), chunkAccess, GenerationStep.Carving.AIR);
                        });
            }
        };

        // SURFACE
        ChunkStatus.GenerationTask surfaceGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                return simpleDoWork(
                        chunkStatus, executor, serverLevel,
                        generator, structureManager, threadedLevelLightEngine,
                        chunkAccessCompletableFutureFunction,
                        chunkAccessList, chunkAccess,
                        unknownFlag,
                        (ChunkStatus pStatus, ServerLevel pLevel, ChunkGenerator pGenerator, List<ChunkAccess> pChunks, ChunkAccess pLoadingChunk) -> {
                            WorldGenRegion worldgenregion = new WorldGenRegion(serverLevel, chunkAccessList, chunkStatus, 0);
                            generator.buildSurface(worldgenregion, serverLevel.structureFeatureManager().forWorldGenRegion(worldgenregion), chunkAccess);
                        });
            }
        };

        // NOISE
        ChunkStatus.GenerationTask noiseGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> noiseFunc;

                if (!unknownFlag && chunkAccess.getStatus().isOrAfter(chunkStatus)) {
                    noiseFunc = CompletableFuture.completedFuture(Either.left(chunkAccess));
                } else {
                    WorldGenRegion worldgenregion = new WorldGenRegion(serverLevel, chunkAccessList, chunkStatus, 0);
                    noiseFunc = generator.fillFromNoise(
                            executor,
                            Blender.of(worldgenregion),
                            serverLevel.structureFeatureManager().forWorldGenRegion(worldgenregion),
                            chunkAccess
                    ).thenApply((p_196792_) -> {
                        if (p_196792_ instanceof ProtoChunk) {
                            ProtoChunk protochunk = (ProtoChunk) p_196792_;
                            BelowZeroRetrogen belowzeroretrogen = protochunk.getBelowZeroRetrogen();
                            if (belowzeroretrogen != null) {
                                BelowZeroRetrogen.replaceOldBedrock(protochunk);
                                if (belowzeroretrogen.hasBedrockHoles()) {
                                    belowzeroretrogen.applyBedrockMask(protochunk);
                                }
                            }

                            protochunk.setStatus(chunkStatus);
                        }

                        return Either.left(p_196792_);
                    });
                }
                return noiseFunc;
            }
        };

        // BIOME
        ChunkStatus.GenerationTask biomeGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> biomeFunc;
                if (!unknownFlag && chunkAccess.getStatus().isOrAfter(chunkStatus)) {
                    biomeFunc = CompletableFuture.completedFuture(Either.left(chunkAccess));
                } else {
                    WorldGenRegion worldgenregion = new WorldGenRegion(serverLevel, chunkAccessList, chunkStatus, -1);
                    biomeFunc = generator.createBiomes(
                            serverLevel.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY),
                            executor,
                            Blender.of(worldgenregion),
                            serverLevel.structureFeatureManager().forWorldGenRegion(worldgenregion),
                            chunkAccess
                    ).thenApply((pChunkAccess) -> {
                        if (pChunkAccess instanceof ProtoChunk) {
                            ((ProtoChunk) pChunkAccess).setStatus(chunkStatus);
                        }
                        return Either.left(pChunkAccess);
                    });
                }
                return biomeFunc;
            }
        };

        // STRUCTURE_REFRENCE
        ChunkStatus.GenerationTask structureReferenceGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                return simpleDoWork(
                        chunkStatus, executor, serverLevel,
                        generator, structureManager, threadedLevelLightEngine,
                        chunkAccessCompletableFutureFunction,
                        chunkAccessList, chunkAccess,
                        unknownFlag,
                        (ChunkStatus pStatus, ServerLevel pLevel, ChunkGenerator pGenerator, List<ChunkAccess> pChunks, ChunkAccess pLoadingChunk) -> {
                            WorldGenRegion worldgenregion = new WorldGenRegion(serverLevel, chunkAccessList, chunkStatus, -1);
                            generator.createReferences(worldgenregion, serverLevel.structureFeatureManager().forWorldGenRegion(worldgenregion), chunkAccess);
                        });
            }
        };

        // STRUCTURE_STARTS
        ChunkStatus.GenerationTask structureStartsGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {
                if (!chunkAccess.getStatus().isOrAfter(chunkStatus)) {
                    if (serverLevel.getServer().getWorldData().worldGenSettings().generateFeatures()) {
                        generator.createStructures(
                                serverLevel.registryAccess(),
                                serverLevel.structureFeatureManager(),
                                chunkAccess,
                                structureManager,
                                serverLevel.getSeed()
                        );
                    }

                    if (chunkAccess instanceof ProtoChunk) {
                        ProtoChunk protochunk = (ProtoChunk) chunkAccess;
                        protochunk.setStatus(chunkStatus);
                    }

                    serverLevel.onStructureStartsAvailable(chunkAccess);
                }

                return CompletableFuture.completedFuture(Either.left(chunkAccess));
            }
        };


        List<ChunkStatus.GenerationTask> allTasks = Arrays.asList(
                fullGenTask,
                // heightmaps - none
                spawnGenTask,
                lightGenTask,
                featuresGenTask,
                // liquid carvers - none
                carversGenTask,
                surfaceGenTask,
                noiseGenTask,
                biomeGenTask,
                structureReferenceGenTask,
                structureStartsGenTask
        );

        ChunkStatus.GenerationTask allGenTask = new ChunkStatus.GenerationTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
                    StructureManager structureManager,
                    ThreadedLevelLightEngine threadedLevelLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
                    List<ChunkAccess> chunkAccessList,
                    ChunkAccess chunkAccess,
                    boolean unknownFlag // TODO figure out what this param is lmao
            ) {

                CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> lastFuture = allTasks.get(0).doWork(chunkStatus, executor, serverLevel, generator, structureManager, threadedLevelLightEngine, chunkAccessCompletableFutureFunction, chunkAccessList, chunkAccess, unknownFlag);
                for (int i = 1; i < allTasks.size(); i++) {
                    try {
                        LOGGER.info("[GENERATION] Running");
                        ChunkAccess lastChunkAccess = lastFuture.get().left().get();
                        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> future = allTasks.get(i).doWork(chunkStatus, executor, serverLevel, generator, structureManager, threadedLevelLightEngine, chunkAccessCompletableFutureFunction, chunkAccessList,
                                lastChunkAccess,
                                unknownFlag
                        );
                        lastFuture = future;
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error running all generation tasks " + e);
                    }
                }

                return lastFuture;
            }
        };

        // EMPTY - (empty)

        HashMap<String, GenerationTask> taskHashMap = new HashMap<>();
        taskHashMap.put("full", fullGenTask);
        taskHashMap.put("all", allGenTask);

        // From the FULL ChunkStatus
        return taskHashMap.getOrDefault(name,
                (a, b, c, d, e, f, g, h, i, j) -> {
                    throw new RuntimeException("Error unknown generation task named '" + name + "'");
                }
        );

    }

    private static final ChunkStatus.LoadingTask PASSTHROUGH_LOADTASK = (chunkStatus, serverLevel, structureManager, lightEngine, chunkAccessCompletableFutureFunction, chunkAccess) -> {
        if (chunkAccess instanceof ProtoChunk) {
            ProtoChunk protochunk = (ProtoChunk) chunkAccess;
            if (!chunkAccess.getStatus().isOrAfter(chunkStatus)) {
                protochunk.setStatus(chunkStatus);
            }
        }
        return CompletableFuture.completedFuture(Either.left(chunkAccess));
    };


    public static @NotNull ChunkStatus.LoadingTask getLoadingTask(String name) {
//            ChunkStatus pStatus, ServerLevel pLevel, StructureManager pStructureManager,
//            ThreadedLevelLightEngine pLightEngine,
//            Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> pTask,
//            ChunkAccess pLoadingChunk

        // FULL
        ChunkStatus.LoadingTask fullLoadTask = new ChunkStatus.LoadingTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus pStatus,
                    ServerLevel pLevel,
                    StructureManager pStructureManager,
                    ThreadedLevelLightEngine pLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> pTask,
                    ChunkAccess pLoadingChunk
            ) {
                return pTask.apply(pLoadingChunk);
            }
        };
        // HEIGHTMAPS - passthrough
        ChunkStatus.LoadingTask heightmapsLoadingTask = PASSTHROUGH_LOADTASK;
        // SPAWN - passthrough
        ChunkStatus.LoadingTask spawnLoadingTask = PASSTHROUGH_LOADTASK;
        // LIGHT (same in loading task)
        ChunkStatus.LoadingTask lightLoadTask = new ChunkStatus.LoadingTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus pStatus,
                    ServerLevel pLevel,
                    StructureManager pStructureManager,
                    ThreadedLevelLightEngine pLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> pTask,
                    ChunkAccess pLoadingChunk
            ) {
                return getLightChunkFuture(pStatus, pLightEngine, pLoadingChunk);
            }
        };
        // FEATURES - passthrough
        ChunkStatus.LoadingTask featuresLoadingTask = PASSTHROUGH_LOADTASK;
        // LIQUID_FEATURES - passthrough
        ChunkStatus.LoadingTask liquidFeaturesLoadingTask = PASSTHROUGH_LOADTASK;
        // CARVERS - passthrough
        ChunkStatus.LoadingTask carversLoadingTask = PASSTHROUGH_LOADTASK;
        // SURFACE - passthrough
        ChunkStatus.LoadingTask surfaceLoadingTask = PASSTHROUGH_LOADTASK;
        // NOISE - passthrough
        ChunkStatus.LoadingTask noiseLoadingTask = PASSTHROUGH_LOADTASK;
        // BIOMES - passthrough
        ChunkStatus.LoadingTask biomesLoadingTask = PASSTHROUGH_LOADTASK;
        // STRUCTURE_REFERENCES - passthrough
        ChunkStatus.LoadingTask structureRefLoadingTask = PASSTHROUGH_LOADTASK;
        // STRUCTURE_STARTS
        ChunkStatus.LoadingTask structureStartsLoadTask = new ChunkStatus.LoadingTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus pStatus,
                    ServerLevel pLevel,
                    StructureManager pStructureManager,
                    ThreadedLevelLightEngine pLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> pTask,
                    ChunkAccess pLoadingChunk
            ) {
                if (!pLoadingChunk.getStatus().isOrAfter(pStatus)) {
                    if (pLoadingChunk instanceof ProtoChunk) {
                        ProtoChunk protochunk = (ProtoChunk) pLoadingChunk;
                        protochunk.setStatus(pStatus);
                    }
                    pLevel.onStructureStartsAvailable(pLoadingChunk);
                }
                return CompletableFuture.completedFuture(Either.left(pLoadingChunk));
            }
        };


        // EMPTY - empty
        List<ChunkStatus.LoadingTask> allTasks = Arrays.asList(
                fullLoadTask,
                heightmapsLoadingTask,
                spawnLoadingTask,
                lightLoadTask,
                featuresLoadingTask,
                liquidFeaturesLoadingTask,
                carversLoadingTask,
                surfaceLoadingTask,
                noiseLoadingTask,
                biomesLoadingTask,
                structureRefLoadingTask,
                structureStartsLoadTask
        );

        ChunkStatus.LoadingTask allLoadTask = new ChunkStatus.LoadingTask() {
            @Override
            public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> doWork(
                    ChunkStatus pStatus,
                    ServerLevel pLevel,
                    StructureManager pStructureManager,
                    ThreadedLevelLightEngine pLightEngine,
                    Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> pTask,
                    ChunkAccess pLoadingChunk
            ) {

                CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> lastFuture = allTasks.get(0).doWork(pStatus, pLevel, pStructureManager, pLightEngine, pTask, pLoadingChunk);
                for (int i = 1; i < allTasks.size(); i++) {
                    try {
                        LOGGER.info("[LOADING] Running");
                        ChunkAccess lastChunkAccess = lastFuture.get().left().get();
                        CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> future = allTasks.get(i).doWork(pStatus, pLevel, pStructureManager, pLightEngine, pTask,
                                lastChunkAccess);
                        lastFuture = future;
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error running all loading tasks " + e);
                    }
                }

                return lastFuture;
            }
        };

        // EMPTY - (empty)

        HashMap<String, LoadingTask> taskHashMap = new HashMap<>();
        taskHashMap.put("full", fullLoadTask);
        taskHashMap.put("all", allLoadTask);

        // From the FULL ChunkStatus
        return taskHashMap.getOrDefault(name,
                (a, b, c, d, e, f) -> {
                    throw new RuntimeException("Error unknown generation task named '" + name + "'");
                }
        );

    }

    public static final ChunkStatus SP8CRAFT_FULL = new ChunkStatus(
            "full",
            ChunkStatus.EMPTY, // TODO lighting etc
            0,
            ChunkStatus.POST_FEATURES,
            ChunkType.LEVELCHUNK,
            Sp8CraftChunkStatus.getGenTask("all"),
            Sp8CraftChunkStatus.getLoadingTask("all")
    );

    public static final ChunkStatus DEFAULT_FULL = new ChunkStatus(
            "full",
            ChunkStatus.HEIGHTMAPS,
            0,
            ChunkStatus.POST_FEATURES,
            ChunkStatus.ChunkType.LEVELCHUNK,
            (p_196771_, p_196772_, p_196773_, p_196774_, p_196775_, p_196776_, p_196777_, p_196778_, p_196779_, p_196780_) -> {
                return p_196777_.apply(p_196779_);
            },
            (p_196764_, p_196765_, p_196766_, p_196767_, p_196768_, p_196769_) -> {
                return p_196768_.apply(p_196769_);
            }
    );

    public static void registerChunks() {

    }
}
