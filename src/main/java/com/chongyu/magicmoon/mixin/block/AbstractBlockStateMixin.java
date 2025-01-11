package com.chongyu.magicmoon.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

    @Inject(at = @At("HEAD"), method = "allowsSpawning", cancellable = true)
    public void allowsSpawning(BlockView world, BlockPos pos, EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        if(world.getFluidState(pos.down()).isIn(FluidTags.WATER) && new Random().nextInt(10) == 0) {
            cir.setReturnValue(true);
        }
    }
}
