package net.sp8craft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.sp8craft.worldgen.Sp8CraftChunkGenerator;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;

public class Sp8WorldGenCommand {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("regen").executes((context) -> {
            CommandSourceStack stack = context.getSource();
            MinecraftServer server = stack.getServer();
            ServerLevel level = server.getLevel(Objects.requireNonNull(stack.getEntity()).getLevel().dimension());

            long chunkPos = ChunkPos.asLong(stack.getEntity().blockPosition());
            int chunkX = ChunkPos.getX(chunkPos);
            int chunkZ = ChunkPos.getZ(chunkPos);
            Sp8CraftChunkGenerator sp8Gen = ((Sp8CraftChunkGenerator)level.getChunkSource().getGenerator());
            sp8Gen.genFunc.reloadJSON();

            for(int chX = -4;chX < 4;chX++) {
                for(int chZ = -4; chZ < 4; chZ++) {
                    ChunkAccess chAcc = level.getChunkSource().getChunk(chunkX + chX, chunkZ + chZ, ChunkStatus.FULL, true);
                    sp8Gen.genFunc.setBlockState(chAcc, Optional.of(level));
                }
            }


            //SectionPos.blockToSectionCoord(x) chunkX

            LOGGER.info("Got command here!");

            return 0;
            })
        );
    }

//    private static
}
