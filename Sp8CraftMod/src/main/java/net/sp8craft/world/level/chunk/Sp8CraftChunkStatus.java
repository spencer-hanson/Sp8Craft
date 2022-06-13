package net.sp8craft.world.level.chunk;

import com.mojang.datafixers.util.Either;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.sp8craft.util.Sp8CraftUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class Sp8CraftChunkStatus extends ChunkStatus {
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

    public static @NotNull CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> generateTaskDoWork(
//            (p_187808_, p_187809_, p_187810_, p_187811_, p_187812_, p_187813_, p_187814_, p_187815_, p_187816_, p_187817_)
            ChunkStatus chunkStatus, Executor executor, ServerLevel serverLevel, ChunkGenerator generator,
            StructureManager structureManager,
            ThreadedLevelLightEngine threadedLevelLightEngine,
            Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> chunkAccessCompletableFutureFunction,
            List<ChunkAccess> chunkAccessList,
            ChunkAccess chunkAccess,
            boolean unknownFlag // TODO figure out what this param is lmao
    ) {

        {// FULL
            // TODO Run this func
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> fullFunc = CompletableFuture.completedFuture(Either.left(chunkAccess));
        }

        // HEIGHTMAPS - Nothing

        { // SPAWN
            if (!chunkAccess.isUpgrading()) {
                generator.spawnOriginalMobs(new WorldGenRegion(
                        serverLevel,
                        chunkAccessList,
                        chunkStatus,
                        -1 // writeRadiusCutoff
                ));
            }
        }

        { // LIGHT (same in loading task)
            boolean isChunkLighted = chunkAccess.getStatus().isOrAfter(chunkStatus) && chunkAccess.isLightCorrect();
            if (!chunkAccess.getStatus().isOrAfter(chunkStatus)) {
                ((ProtoChunk) chunkAccess).setStatus(chunkStatus);
            }
            // TODO Run this func
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> lightFunc = getLightChunkFuture(chunkStatus, threadedLevelLightEngine, chunkAccess);
        }


        { // FEATURES
            ProtoChunk protochunk = (ProtoChunk) chunkAccess;
            protochunk.setLightEngine(threadedLevelLightEngine);
            if (unknownFlag || !chunkAccess.getStatus().isOrAfter(chunkStatus)) {
                Heightmap.primeHeightmaps(chunkAccess, EnumSet.of(Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE));
                WorldGenRegion worldgenregion = new WorldGenRegion(serverLevel, chunkAccessList, chunkStatus, 1);
                generator.applyBiomeDecoration(worldgenregion, chunkAccess, serverLevel.structureFeatureManager().forWorldGenRegion(worldgenregion));
                Blender.generateBorderTicks(worldgenregion, chunkAccess);
                protochunk.setStatus(chunkStatus);
            }

            // TODO Run this func
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> featureFunc = CompletableFuture.completedFuture(Either.left(chunkAccess));
        }
        // LIQUID CARVERS - Nothing

        // TODO Carvers -> ... -> Empty
//        {// CARVERS
//            //(p_187819_, p_187820_, p_187821_, p_187822_, p_187823_)
//            WorldGenRegion worldgenregion = new WorldGenRegion(p_187820_, p_187822_, p_187819_, 0);
//            if (p_187823_ instanceof ProtoChunk) {
//                ProtoChunk protochunk = (ProtoChunk) p_187823_;
//                Blender.addAroundOldChunksCarvingMaskFilter(worldgenregion, protochunk);
//            }
//
//            p_187821_.applyCarvers(worldgenregion, p_187820_.getSeed(), p_187820_.getBiomeManager(), p_187820_.structureFeatureManager().forWorldGenRegion(worldgenregion), p_187823_, GenerationStep.Carving.AIR);
//
//        }
//

        // From the FULL ChunkStatus
        return chunkAccessCompletableFutureFunction.apply(chunkAccess);
    }

    public static @NotNull CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> loadingTaskDoWork(
            ChunkStatus pStatus, ServerLevel pLevel, StructureManager pStructureManager,
            ThreadedLevelLightEngine pLightEngine,
            Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> pTask,
            ChunkAccess pLoadingChunk) {
// TODO
        return pTask.apply(pLoadingChunk);

    }

    public static final ChunkStatus SP8CRAFT_FULL = new ChunkStatus(
            "full",
            ChunkStatus.EMPTY, // TODO lighting etc
            0,
            ChunkStatus.POST_FEATURES,
            ChunkType.LEVELCHUNK,
//             List<ChunkAccess> p_187878_, ChunkAccess p_187879_, boolean p_187880_)
            Sp8CraftChunkStatus::generateTaskDoWork,
            Sp8CraftChunkStatus::loadingTaskDoWork
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
