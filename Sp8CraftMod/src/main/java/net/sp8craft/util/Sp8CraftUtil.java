package net.sp8craft.util;


import com.mojang.datafixers.util.Either;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.concurrent.CompletableFuture;

public class Sp8CraftUtil {
    public static class Math {

        public static int getSign(int num) {
            return num < 0 ? -1 : 1;
        }

        public static int oppSign(int num) {
            return num < 0 ? 1 : -1;
        }

        public static int getSpiralIndex(int x, int y) {
            int index;
            if (y * y >= x * x) {
                index = (4 * y * y - y) - x;
                if (y < x) {
                    index -= 2 * (y - x);
                }
            } else {
                index = (4 * x * x - y) - x;
                if (y < x) {
                    index += 2 * (y - x);
                }
            }
            return index;
        }
    }

}
