package com.chongyu.magicmoon.mixin.spawn;

import com.chongyu.magicmoon.core.Moons;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    //HOGLIN: 疣猪兽 ,SKELETON_HORSE，ZOMBIE_HORSE
    @Unique
    private static final Pool<Weighted> MAGIC_MOON_MAGIC_SPAWNS = Pool.of((Weighted[])new SpawnSettings.SpawnEntry[]{new SpawnSettings.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4), new SpawnSettings.SpawnEntry(EntityType.GHAST, 10, 1, 2),new SpawnSettings.SpawnEntry(EntityType.CAVE_SPIDER, 5, 4, 4),new SpawnSettings.SpawnEntry(EntityType.EVOKER, 5, 1, 2),new SpawnSettings.SpawnEntry(EntityType.VINDICATOR, 5, 1, 2),new SpawnSettings.SpawnEntry(EntityType.PILLAGER, 5, 1, 2),new SpawnSettings.SpawnEntry(EntityType.STRAY, 5, 1, 2)});

    //    @Unique
//    private static final SpawnSettings.SpawnEntry ABLOODMOON_ZOMBIFIED_PIGLIN_SPAWNS = new SpawnSettings.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 5, 4, 4);
//    @Unique
//    private static final SpawnSettings.SpawnEntry ABLOODMOON_GHAST_SPAWNS = new SpawnSettings.SpawnEntry(EntityType.GHAST, 3, 1, 1);

    @Inject(
            method = {"getSpawnEntries"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void getSpawnEntriesMagicMoon(ServerWorld world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, SpawnGroup spawnGroup, BlockPos pos, RegistryEntry<Biome> biomeEntry, CallbackInfoReturnable<Pool<Weighted>> cir) {
        if (Moons.isMagicMoon() && Moons.isNight()) {
            cir.setReturnValue(MAGIC_MOON_MAGIC_SPAWNS);
        }
    }

    @Inject(
            method = {"getSpawnEntries"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private static void getSpawnEntries(ServerWorld world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, SpawnGroup spawnGroup, BlockPos pos, @Nullable RegistryEntry<Biome> biomeEntry, CallbackInfoReturnable<Pool<SpawnSettings.SpawnEntry>> cir) {
        SpawnSettings spawnSettings = world.getBiome(pos).value().getSpawnSettings();
        if (Moons.isBloodMoon() && Moons.isNight()) {
            List<SpawnSettings.SpawnEntry> spawnEntriesList = new ArrayList<>(spawnSettings.getSpawnEntries(spawnGroup).getEntries());
            spawnEntriesList.addAll(((Pool<SpawnSettings.SpawnEntry>) cir.getReturnValue()).getEntries());
            cir.setReturnValue(Pool.of(spawnEntriesList));
        } else {
            cir.setReturnValue(spawnSettings.getSpawnEntries(spawnGroup));
        }

    }

    @Inject(
            method = {"getRandomPosInChunkSection"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private static void getRandomPosInChunkSection(World world, WorldChunk chunk, CallbackInfoReturnable<BlockPos> cir) {
        if (Moons.isBloodMoon() && Moons.isNight()) {
            BlockPos returnValue = (BlockPos) cir.getReturnValue();
            PlayerEntity closestPlayer = world.getClosestPlayer((double) returnValue.getX(), (double) returnValue.getY(), (double) returnValue.getZ(), -1.0, false);
            if (closestPlayer != null) {
                BlockPos closestPlayerPosition = closestPlayer.getBlockPos();
                if (closestPlayerPosition.getY() > world.getTopY(Heightmap.Type.WORLD_SURFACE, closestPlayerPosition.getX(), closestPlayerPosition.getZ())) {
                    cir.setReturnValue(new BlockPos(returnValue.getX(), world.getTopY(Heightmap.Type.WORLD_SURFACE, returnValue.getX(), returnValue.getZ()) + 1, returnValue.getZ()));
                }
            }
        }

        if(Moons.isBlueMoon() && Moons.isNight()){
            ChunkPos chunkPos = chunk.getPos();
            int i = chunkPos.getStartX() + world.random.nextInt(16);
            int j = chunkPos.getStartZ() + world.random.nextInt(16);
            int k = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, i, j) -5;
            int l = MathHelper.nextBetween(world.random, world.getBottomY(), k);
            if(l >= 60){
                l = 60;
            }
            cir.setReturnValue(new BlockPos(i, l, j));
        }
    }

    @Inject(method = {"canSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;Lnet/minecraft/util/math/BlockPos$Mutable;D)Z"}, at = {@At("RETURN")}, cancellable = true)
    private static void canSpawn(ServerWorld world, SpawnGroup group, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, SpawnSettings.SpawnEntry spawnEntry, BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
        if (Moons.isBlueMoon() && Moons.isNight()) {
            if (Moons.checkIfOutdoor(world,pos)) {
                cir.setReturnValue(false);
            }
        }
    }


}
