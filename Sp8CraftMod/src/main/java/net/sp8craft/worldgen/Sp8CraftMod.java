package net.sp8craft.worldgen;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import jdk.jshell.JShell;
import jdk.jshell.spi.ExecutionControlProvider;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeWorldPreset;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sp8craft.worldgen.biomes.Sp8CraftBiomeManager;
import org.slf4j.Logger;

import java.util.ServiceLoader;
import java.util.stream.Collectors;


@Mod("sp8craft")
public class Sp8CraftMod {
    public static final String SP8_MOD_ID = "sp8craft";

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // Chunk Generators
    private static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registry.CHUNK_GENERATOR_REGISTRY, SP8_MOD_ID);
    public static final RegistryObject<Codec<? extends ChunkGenerator>> SP8CRAFT_CHUNK_GEN = CHUNK_GENERATORS.register(SP8_MOD_ID, () -> Sp8CraftChunkGenerator.CODEC);

    // World Types
    public static final DeferredRegister<ForgeWorldPreset> WORLD_TYPES = DeferredRegister.create(ForgeRegistries.Keys.WORLD_TYPES, ForgeRegistries.Keys.WORLD_TYPES.location().getNamespace());
    public static final RegistryObject<ForgeWorldPreset> SP8_WORLD = WORLD_TYPES.register(SP8_MOD_ID, () -> new Sp8CraftWorldType(new Sp8CraftChunkGenerator.Sp8ChunkFactory()));

    // Biomes
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registry.BIOME_REGISTRY, SP8_MOD_ID);
    public static final Sp8CraftBiomeManager SP8_BIOME_MANAGER = new Sp8CraftBiomeManager();


    public Sp8CraftMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        WORLD_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CHUNK_GENERATORS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo(SP8_MOD_ID, "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
