package net.sp8craft.worldgen.biomes;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;

import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;

import javax.annotation.Nullable;

public class Sp8CraftBiome {

    protected static int calculateSkyColor(float p_194844_) {
        float $$1 = p_194844_ / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

    public Biome basicBiome() {


        Biome.Precipitation precip = Biome.Precipitation.NONE;
// one of
//        NONE("none"),
//                RAIN("rain"),
//                SNOW("snow");

        Biome.BiomeCategory bioCategory = Biome.BiomeCategory.NONE;

        // Temp and Downfall https://www.reddit.com/r/minecraft_configs/comments/hq60cg/i_made_a_graph_for_how_temperature_and_downfall/
        float temp = 0.0f; // -0.5 to 2.0
        float downfall = 0.0f; // 0.0 to 1.0
        int waterColor = 12638463; // Decimal value to HEX RGB
        int waterFogColor = 12638463;
        int fogColor = 12638463;

        BiomeGenerationSettings.Builder biomeGenSettings = new BiomeGenerationSettings.Builder();

// default settings for overworld, added to normal biomes
//        BiomeDefaultFeatures.addDefaultCarversAndLakes(p_194870_);
//        BiomeDefaultFeatures.addDefaultCrystalFormations(p_194870_);
//        BiomeDefaultFeatures.addDefaultMonsterRoom(p_194870_);
//        BiomeDefaultFeatures.addDefaultUndergroundVariety(p_194870_);
//        BiomeDefaultFeatures.addDefaultSprings(p_194870_);
//        BiomeDefaultFeatures.addSurfaceFreezing(p_194870_);
// Sand, gravel and clay "disks"
//        BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
// for more biomes and stuff see package net.minecraft.data.worldgen.biome.OverworldBiomes

        MobSpawnSettings.Builder mobSpawnSettings = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(mobSpawnSettings);
        mobSpawnSettings.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 4));
        mobSpawnSettings.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 3));
        mobSpawnSettings.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 8, 2, 4));

        @Nullable Music music = null;


        return (new Biome.BiomeBuilder())
                .precipitation(precip)
                .biomeCategory(bioCategory)
                .temperature(temp)
                .downfall(downfall)
                .specialEffects((
                        new BiomeSpecialEffects.Builder())
                        .waterColor(waterColor)
                        .waterFogColor(waterFogColor)
                        .fogColor(fogColor)
                        .skyColor(calculateSkyColor(temp))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(music).build())
                .mobSpawnSettings(mobSpawnSettings.build())
                .generationSettings(biomeGenSettings.build())
                .build();
    }
}