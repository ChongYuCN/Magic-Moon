package com.chongyu.magicmoon.mixin.entity;

import com.chongyu.magicmoon.core.Moons;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin {

    @Inject(
            method = {"canSpawn"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void allowSlimeSpawnsAnywhere(EntityType<SlimeEntity> slimeEntityType, WorldAccess accessor, SpawnReason spawnType, BlockPos pos, Random randomSource, CallbackInfoReturnable<Boolean> cir) {
        if (accessor instanceof ServerWorld) {
            if (Moons.isBloodMoon()) {
                boolean aboveY = pos.getY() > 50 && pos.getY() >= accessor.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) - 1;
                if (aboveY && randomSource.nextFloat() < 0.5F && randomSource.nextFloat() < accessor.getMoonSize() && accessor.getLightLevel(pos) <= randomSource.nextInt(8)) {
                    cir.setReturnValue(MobEntity.canMobSpawn(slimeEntityType, accessor, spawnType, pos, randomSource));
                }
            }
        }

    }
}
