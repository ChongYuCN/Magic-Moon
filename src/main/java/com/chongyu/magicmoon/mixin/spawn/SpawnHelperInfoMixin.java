package com.chongyu.magicmoon.mixin.spawn;

import com.chongyu.magicmoon.core.Moons;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnDensityCapper;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.Info.class)
public class SpawnHelperInfoMixin {
    @Mutable
    @Final
    @Shadow private final int spawningChunkCount;
    @Mutable
    @Final
    @Shadow private final Object2IntOpenHashMap<SpawnGroup> groupToCount;

    @Mutable
    @Final
    @Shadow private final SpawnDensityCapper densityCapper;

    public SpawnHelperInfoMixin(int spawningChunkCount, Object2IntOpenHashMap<SpawnGroup> groupToCount, SpawnDensityCapper densityCapper) {
        this.spawningChunkCount = spawningChunkCount;
        this.groupToCount = groupToCount;
        this.densityCapper = densityCapper;
    }

    @Inject(
            method = {"isBelowCap"},
            at = {@At("HEAD")},
            cancellable = true
    )
    void isBelowCap(SpawnGroup group, ChunkPos chunkPos, CallbackInfoReturnable<Boolean> cir) {
        //2:刷怪量为两倍
        int spawnMultiplier = 1;
        if((Moons.isBloodMoon() || Moons.isMagicMoon()) && Moons.isNight()){
            spawnMultiplier=10;
        }

        int i = group.getCapacity() * this.spawningChunkCount * spawnMultiplier/SpawnHelperAccess.getChunkArea();
        if (this.groupToCount.getInt(group) >= i) {
            cir.setReturnValue(false);
        }
        cir.setReturnValue(this.densityCapper.canSpawn(group, chunkPos));
    }
}
