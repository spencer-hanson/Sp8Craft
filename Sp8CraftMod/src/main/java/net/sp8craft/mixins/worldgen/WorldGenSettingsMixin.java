package net.sp8craft.mixins.worldgen;


import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.sp8craft.worldgen.ISp8CraftLevelTypeMixin;
import net.sp8craft.worldgen.Sp8CraftChunkGenerator;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static net.minecraft.world.level.levelgen.WorldGenSettings.parseSeed;
import static net.sp8craft.worldgen.Sp8CraftMod.MOD_ID;

@Mixin(WorldGenSettings.class)
public abstract class WorldGenSettingsMixin implements ISp8CraftLevelTypeMixin {
    // Directly reference a slf4j logger
    @Final
    @Shadow
    private static Logger LOGGER = LogUtils.getLogger();

    private String levelType;

    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    private static void sp8craft_worldgen_create(RegistryAccess pRegistryAccess, DedicatedServerProperties.WorldGenProperties pProperties, CallbackInfoReturnable<WorldGenSettings> callback) {
        String levelType = pProperties.levelType();
        if (levelType.equals(MOD_ID)) {
            long seed = parseSeed(pProperties.levelSeed()).orElse((new Random()).nextLong());
            ChunkGenerator sp8ChunkGen = new Sp8CraftChunkGenerator.Sp8ChunkFactory().createChunkGenerator(pRegistryAccess, seed);

            Registry<LevelStem> levelStems = DimensionType.defaultDimensions(pRegistryAccess, seed);
            LevelStem levelstem = levelStems.get(LevelStem.OVERWORLD);

            Registry<DimensionType> dimensionTypes = pRegistryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
            Holder<DimensionType> dimensionTypeholder = levelstem == null ? dimensionTypes.getOrCreateHolder(DimensionType.OVERWORLD_LOCATION) : levelstem.typeHolder();

            WritableRegistry<LevelStem> writableregistry = new MappedRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental(), null);

            writableregistry.register(LevelStem.OVERWORLD, new LevelStem(dimensionTypeholder, sp8ChunkGen), Lifecycle.stable());

            for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : levelStems.entrySet()) {
                ResourceKey<LevelStem> resourcekey = entry.getKey();
                if (resourcekey != LevelStem.OVERWORLD) {
                    writableregistry.register(resourcekey, entry.getValue(), levelStems.lifecycle(entry.getValue()));
                }
            }

            WorldGenSettings worldGenSettings = new WorldGenSettings(
                    seed,
                    pProperties.generateStructures(),
                    false,
                    writableregistry
            );

            LOGGER.info("Setting leveltype as " + levelType);
            ISp8CraftLevelTypeMixin m = (ISp8CraftLevelTypeMixin) worldGenSettings;
            m.setLevelType(levelType);

            callback.setReturnValue(worldGenSettings);
        }
    }

    @Inject(at=@At("RETURN"), method = "<init>*")
    private void sp8craftworldgen_registerLevelType(CallbackInfo ci) {
        this.levelType = "default";
    }


    @Override
    public String getLevelType() {
        return this.levelType;
    }

    @Override
    public void setLevelType(String levl) {
        this.levelType = levl;
    }
}
