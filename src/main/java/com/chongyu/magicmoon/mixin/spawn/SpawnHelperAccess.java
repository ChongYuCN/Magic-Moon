package com.chongyu.magicmoon.mixin.spawn;

import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({SpawnHelper.class})
public interface SpawnHelperAccess {
    @Accessor("CHUNK_AREA")
    static int getChunkArea() {
        throw new Error("Mixin did not apply!");
    }
}