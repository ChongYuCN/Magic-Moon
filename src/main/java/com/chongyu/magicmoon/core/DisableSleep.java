package com.chongyu.magicmoon.core;

import com.chongyu.magicmoon.MagicMoon;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public class DisableSleep {
    public static void init(){
        //中午6000=12点，日落晚上13000=7点，子夜18000=24点,23000=凌晨3点
        EntitySleepEvents.ALLOW_SLEEP_TIME.register( (player, sleepingPos, vanillaResult) -> {
            if(player.getWorld() instanceof ServerWorld){

                if((Moons.isBloodMoon() || Moons.isMagicMoon()) && Moons.isNight()){
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();
            if (!player.getWorld().isClient) {
                if((Moons.isBloodMoon() || Moons.isMagicMoon()) && Moons.isNight()){
                    if (block instanceof BedBlock) {
                        //test
//                        System.out.println("配置isCustomModel="+ MagicMoon.config.getCommonConfig().isCustomModel);
//                        System.out.println("配置startDay="+ MagicMoon.config.getCommonConfig().startDay);
//                        System.out.println("配置perDay="+ MagicMoon.config.getCommonConfig().perDay);
//                        System.out.println("计算余数="+ ((Moons.day - MagicMoon.config.getCommonConfig().startDay)/ MagicMoon.config.getCommonConfig().perDay - 1) % 4 +1);
                        return ActionResult.FAIL;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
