package net.sp8craft.config;

import java.util.HashMap;

public class Sp8CraftConfig {
    public static final String KEY_USE_WORLDGEN = "use_worldgen";

    public static HashMap<String, Boolean> BOOL_CONFIGS;

    static {
        BOOL_CONFIGS = new HashMap<>();
        BOOL_CONFIGS.put(KEY_USE_WORLDGEN, false);

    }
}
