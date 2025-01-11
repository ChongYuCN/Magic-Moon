package com.chongyu.magicmoon.mixin.world;

import com.chongyu.magicmoon.core.Moons;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess, AutoCloseable {
    @Mutable
    @Final
    @Shadow
    protected final MutableWorldProperties properties;

    protected WorldMixin(MutableWorldProperties properties) {
        this.properties = properties;
    }

    @Inject(at=@At("HEAD"), method = "tickBlockEntities")
    public void tickBlockEntities(CallbackInfo info) {
        Moons.day_time = getTimeOfDay();
        Moons.day = (int)(getTimeOfDay()/ 24000L)+1;
    }

    @Inject(at=@At("HEAD"), method = "tickEntity")
    public <T extends Entity> void tickEntity(Consumer<T> tickConsumer, T entity, CallbackInfo info) {
        if(entity instanceof ServerPlayerEntity player){
            long timeDayMoon = Moons.day_time - (Moons.day -1)*24000L;
            if((Moons.isBloodMoon() || Moons.isMagicMoon()) && Moons.isNight()){
                if(player.isSleeping()){
                    player.wakeUp();
                }
            }
            magick$sendMessage(player,timeDayMoon);
        }
    }

    @Shadow
    public long getTimeOfDay() {
        return this.properties.getTimeOfDay();
    }

    @Unique
    public void magick$sendMessage(ServerPlayerEntity player,long time) {
        //6:0,7:1,8:2,9:3,10:4,11:5,12:6,13:7,14:8,
        // 15:9000L, 16:10000L, 17:11000L, 18:12000L, 19:13000L, 20:14000L, 21:15000L
        //22:16,  23:17, 24:18, 1:19, 2:20, 3:21, 4:22, 5:23, 6: 0
        if(time == 12000L){
            if(Moons.isBloodMoon()){
                player.sendMessage(Text.translatable("magicmoon.info.bloodmoon").formatted(Formatting.DARK_RED));
            }else if(Moons.isBlueMoon()){
                player.sendMessage(Text.translatable("magicmoon.info.bluemoon").formatted(Formatting.DARK_BLUE));
            }else if(Moons.isHarvestMoon()){
                player.sendMessage(Text.translatable("magicmoon.info.havestmoon").formatted(Formatting.YELLOW));
            }else if(Moons.isMagicMoon()){
                player.sendMessage(Text.translatable("magicmoon.info.magicmoon").formatted(Formatting.LIGHT_PURPLE));
            }
        }

    }
}
