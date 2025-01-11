package com.chongyu.magicmoon.mixin.world;

import com.chongyu.magicmoon.core.Moons;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    @Mutable
    @Final
    @Shadow
    private final MinecraftServer server;
    @Mutable
    @Final
    @Shadow
    private final ServerWorldProperties worldProperties;

    @Mutable
    @Final
    @Shadow
    private final ServerChunkManager chunkManager;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates, MinecraftServer server, ServerWorldProperties worldProperties, ServerChunkManager chunkManager) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
        this.server = server;
        this.worldProperties = worldProperties;
        this.chunkManager = chunkManager;
    }

    @Shadow public abstract ServerChunkManager getChunkManager();

    @Inject(at=@At("HEAD"), method = "tickTime")
    public void tickTime(CallbackInfo info) {
        if(Moons.isBloodMoon() || Moons.isHarvestMoon() || Moons.isBlueMoon() || Moons.isMagicMoon()){
            long timeDayMoon = Moons.day_time - (Moons.day -1)*24000L;

            //6:0,7:1,8:2,9:3,10:4,11:5,12:6,13:7,14:8,
            // 15:9000L, 16:10000L, 17:11000L, 18:12000L, 19:13000L, 20:14000L, 21:15000L
            //22:16,  23:17, 24:18, 1:19, 2:20, 3:21, 4:22, 5:23, 6: 0
            if(Moons.isBloodMoon() || Moons.isMagicMoon()){
                if(timeDayMoon >= 0 && timeDayMoon <= 12000L){
                    if(!this.isThundering()){
                        Objects.requireNonNull(this.getServer()).getOverworld().setWeather(0,6000,true,true);
                    }
                }
            }
            if(timeDayMoon > 12000L && timeDayMoon < 23000L){
                Objects.requireNonNull(this.getServer()).getOverworld().setWeather(-1,0,false,false);

            }
        }

    }

    @Inject(at = @At("TAIL"), method = "tickChunk")
    public void tickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ca) {
        ChunkPos chunkPos = chunk.getPos();
        if (this.chunkManager.isChunkLoaded(chunkPos.x, chunkPos.z)) {
            ((ServerChunkManagerAccessor)this.getChunkManager()).setSpawnAnimals(Moons.day == 1 || Moons.isBlueMoon());
        }
    }
}
