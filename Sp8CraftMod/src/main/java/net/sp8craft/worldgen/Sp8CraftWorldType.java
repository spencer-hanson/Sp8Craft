package net.sp8craft.worldgen;

import net.minecraft.Util;
import net.minecraftforge.common.world.ForgeWorldPreset;

class Sp8CraftWorldType extends ForgeWorldPreset {

    public Sp8CraftWorldType() {
        super(new Sp8CraftChunkGenerator.Sp8ChunkFactory());
    }

    public Sp8CraftWorldType(IChunkGeneratorFactory factory) {
        super(factory);
    }

    public Sp8CraftWorldType(IBasicChunkGeneratorFactory factory) {
        super(factory);
    }


    public String getTranslationKey()
    {
        return "Sp8Craft";
//        return Util.makeDescriptionId("generator", getRegistryName());
    }
}