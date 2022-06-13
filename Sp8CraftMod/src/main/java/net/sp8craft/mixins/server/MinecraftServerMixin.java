package net.sp8craft.mixins.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.*;
import net.sp8craft.config.Sp8CraftConfig;
import net.sp8craft.server.level.Sp8CraftServerLevel;
import net.sp8craft.world.level.chunk.Sp8CraftChunkStatus;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> implements CommandSource, AutoCloseable {
    // Directly reference a slf4j logger
    @Final
    @Shadow
    private static Logger LOGGER = LogUtils.getLogger();

    @Shadow
    @Final
    protected WorldData worldData;

    @Shadow
    @Final
    private Executor executor;

    @Shadow
    @Final
    protected LevelStorageSource.LevelStorageAccess storageSource;

    @Shadow
    @Final
    private final Map<ResourceKey<Level>, ServerLevel> levels = Maps.newLinkedHashMap();

    @Shadow
    public abstract RegistryAccess.Frozen registryAccess();

    @Shadow
    protected abstract void readScoreboard(DimensionDataStorage p_129842_);

    @Nullable
    @Shadow
    private CommandStorage commandStorage;

    @Shadow
    protected abstract void setupDebugLevel(WorldData p_129848_);

    @Shadow
    public abstract PlayerList getPlayerList();

    @Shadow
    public abstract CustomBossEvents getCustomBossEvents();

    public MinecraftServerMixin(String p_18765_) {
        super(p_18765_);
    }

    @Invoker("setInitialSpawn")
    public static void setInitialSpawn(ServerLevel pLevel, ServerLevelData pLevelData, boolean pGenerateBonusChest, boolean pDebug) {
        throw new AssertionError();
    }


    public List<ChunkStatus> getDefaultStatusByRange() {
        return ImmutableList.of(ChunkStatus.FULL,
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
    }

    public List<ChunkStatus> getSp8CraftStatusByRange() {
        return ImmutableList.of(Sp8CraftChunkStatus.SP8CRAFT_FULL);
    }

    @Inject(at = @At("HEAD"), method = "createLevels", cancellable = true)
    private void sp8craft_server_createLevels(ChunkProgressListener p_129816_, CallbackInfo ci) {
        ChunkStatus.STATUS_BY_RANGE = this.getDefaultStatusByRange();


        if (Sp8CraftConfig.BOOL_CONFIGS.get(Sp8CraftConfig.KEY_USE_WORLDGEN)) {
            LOGGER.info("createLevels"); // TODO Logging

//            ChunkStatus.STATUS_BY_RANGE = this.getSp8CraftStatusByRange();
            ChunkStatus.STATUS_BY_RANGE = this.getDefaultStatusByRange();

            ServerLevelData serverleveldata = this.worldData.overworldData();
            WorldGenSettings worldgensettings = this.worldData.worldGenSettings();
            boolean flag = worldgensettings.isDebug();
            long i = worldgensettings.seed();
            long j = BiomeManager.obfuscateSeed(i);
            List<CustomSpawner> list = ImmutableList.of(
                    new PhantomSpawner(),
                    new PatrolSpawner(),
                    new CatSpawner(),
                    new VillageSiege(),
                    new WanderingTraderSpawner(serverleveldata)
            );

            Registry<LevelStem> registry = worldgensettings.dimensions();
            LevelStem levelstem = registry.get(LevelStem.OVERWORLD);
            ChunkGenerator chunkgenerator;
            Holder<DimensionType> holder;

            if (levelstem == null) {
                holder = this.registryAccess().<DimensionType>registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrCreateHolder(DimensionType.OVERWORLD_LOCATION);
                chunkgenerator = WorldGenSettings.makeDefaultOverworld(this.registryAccess(), (new Random()).nextLong());
            } else {
                holder = levelstem.typeHolder();
                chunkgenerator = levelstem.generator();
            }

            //////////////
            ServerLevel serverlevel = new Sp8CraftServerLevel(
                    (MinecraftServer) (Object) this,
                    this.executor, this.storageSource, serverleveldata, Level.OVERWORLD, holder, p_129816_, chunkgenerator, flag, j, list, true);
            //////////////
            this.levels.put(Level.OVERWORLD, serverlevel);
            DimensionDataStorage dimensiondatastorage = serverlevel.getDataStorage();
            this.readScoreboard(dimensiondatastorage);
            this.commandStorage = new CommandStorage(dimensiondatastorage);
            WorldBorder worldborder = serverlevel.getWorldBorder();
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.WorldEvent.Load(this.levels.get(Level.OVERWORLD)));
            if (!serverleveldata.isInitialized()) {
                try {
                    MinecraftServerMixin.setInitialSpawn(serverlevel, serverleveldata, worldgensettings.generateBonusChest(), flag);
                    serverleveldata.setInitialized(true);
                    if (flag) {
                        this.setupDebugLevel(this.worldData);
                    }
                } catch (Throwable throwable1) {
                    CrashReport crashreport = CrashReport.forThrowable(throwable1, "Exception initializing level");

                    try {
                        serverlevel.fillReportDetails(crashreport);
                    } catch (Throwable throwable) {
                    }

                    throw new ReportedException(crashreport);
                }

                serverleveldata.setInitialized(true);
            }

            this.getPlayerList().addWorldborderListener(serverlevel);
            if (this.worldData.getCustomBossEvents() != null) {
                this.getCustomBossEvents().load(this.worldData.getCustomBossEvents());
            }

            for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : registry.entrySet()) {
                ResourceKey<LevelStem> resourcekey = entry.getKey();
                if (resourcekey != LevelStem.OVERWORLD) {
                    ResourceKey<Level> resourcekey1 = ResourceKey.create(Registry.DIMENSION_REGISTRY, resourcekey.location());
                    Holder<DimensionType> holder1 = entry.getValue().typeHolder();
                    ChunkGenerator chunkgenerator1 = entry.getValue().generator();
                    DerivedLevelData derivedleveldata = new DerivedLevelData(this.worldData, serverleveldata);
                    //////////////
                    ServerLevel serverlevel1 = new Sp8CraftServerLevel(
                            (MinecraftServer) (Object) this,
                            this.executor, this.storageSource, derivedleveldata, resourcekey1, holder1, p_129816_, chunkgenerator1, flag, j, ImmutableList.of(), false);
                    //////////////
                    worldborder.addListener(new BorderChangeListener.DelegateBorderChangeListener(serverlevel1.getWorldBorder()));
                    this.levels.put(resourcekey1, serverlevel1);
                    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.WorldEvent.Load(levels.get(resourcekey)));
                }
            }

            worldborder.applySettings(serverleveldata.getWorldBorder());
            ci.cancel();
        }
    }
}
