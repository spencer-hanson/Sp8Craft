package net.sp8craft.worldgen.biomes;


import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.RegistryObject;
import net.sp8craft.worldgen.Sp8CraftMod;
import org.jetbrains.annotations.NotNull;

public class Sp8CraftBiomeManager {
    public static Sp8CraftBiome BASIC = new Sp8CraftBiome();
    public static String BASIC_NAME = "sp8craft_basic";
//    public static ResourceKey<Biome> BASIC_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BASIC_NAME));

    public static final RegistryObject<Biome> SP8_BIOME;
    @NotNull public static final ResourceKey<Biome> SP8_BIOME_KEY;



    public final int shutUpLinter;
    public Sp8CraftBiomeManager() {
        // put stuff in here for init
        shutUpLinter = 8;
    }

    static { // Called when class is loaded, class is force loaded with Constuctor call of SP8_BIOME_MANAGER
        SP8_BIOME = Sp8CraftMod.BIOMES.register(
                Sp8CraftBiomeManager.BASIC_NAME, () -> Sp8CraftBiomeManager.BASIC.basicBiome()
        );

        assert SP8_BIOME.getKey() != null;
        SP8_BIOME_KEY = SP8_BIOME.getKey();

    }
}
