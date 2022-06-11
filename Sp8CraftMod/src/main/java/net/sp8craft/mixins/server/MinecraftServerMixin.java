package net.sp8craft.mixins.server;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.WorldData;
import net.sp8craft.mixins.worldgen.WorldGenSettingsMixin;
import net.sp8craft.worldgen.ISp8CraftLevelTypeMixin;
import net.sp8craft.worldgen.Sp8CraftChunkGenerator;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static net.sp8craft.worldgen.Sp8CraftMod.MOD_ID;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> {
    // Directly reference a slf4j logger
    @Final
    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();

    @Shadow
    @Final
    protected WorldData worldData;

    public MinecraftServerMixin(String p_18765_) {
        super(p_18765_);
    }


    @Inject(
            at = @At("HEAD"),
            method = "createLevels"
    )
    private void sp8craft_server_createLevels(ChunkProgressListener p_129816_, CallbackInfo ci) {
        WorldGenSettings worldGenSettings = this.worldData.worldGenSettings();

        if (((ISp8CraftLevelTypeMixin) worldGenSettings).getLevelType().equals(MOD_ID)) {
            LOGGER.info("CreateLevels called from mixin");
//            ci.cancel();
        }


    }
}
