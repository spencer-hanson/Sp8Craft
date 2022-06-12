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
import java.util.Random;

import static net.minecraft.world.level.levelgen.WorldGenSettings.parseSeed;
import static net.sp8craft.worldgen.Sp8CraftMod.SP8_MOD_ID;

@Mixin(WorldGenSettings.class)
public abstract class WorldGenSettingsMixin {
    // Directly reference a slf4j logger
    @Final
    @Shadow
    private final static Logger LOGGER = LogUtils.getLogger();

    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    private static void sp8craft_worldgen_create(RegistryAccess pRegistryAccess, DedicatedServerProperties.WorldGenProperties pProperties, CallbackInfoReturnable<WorldGenSettings> callback) {
        String levelType = pProperties.levelType();
        if (levelType.equals(SP8_MOD_ID)) {
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

            callback.setReturnValue(worldGenSettings);
        }
    }
}
