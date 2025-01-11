package com.chongyu.magicmoon.mixin.block;

import com.chongyu.magicmoon.core.Moons;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(at = @At("RETURN"), method = "getDroppedStacks", cancellable = true)
    public void getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        if(state.getBlock() instanceof CropBlock){
            List<ItemStack> oldList = cir.getReturnValue();
            List<ItemStack> newList = new ArrayList<>();
            for (ItemStack itemStack : oldList){
                ItemStack newItemStack;
                newItemStack = itemStack.copy();
                if(Moons.isHarvestMoon() && Moons.isNight()){
                    if(new Random().nextInt(10) == 0 ){
                        newItemStack.setCount(itemStack.getCount() *2);
                        newList.add(newItemStack);
                    } else {
                        newList.add(newItemStack);
                    }
                }else {
                    newList.add(newItemStack);
                }
            }
            cir.setReturnValue(newList);
        }
    }
}
