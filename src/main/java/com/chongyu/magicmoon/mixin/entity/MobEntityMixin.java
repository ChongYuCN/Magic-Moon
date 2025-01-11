package com.chongyu.magicmoon.mixin.entity;

import com.chongyu.magicmoon.core.Moons;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("TAIL"), method = "tick")
    public void tick(CallbackInfo ci) {
        if ((Moons.isBloodMoon() || Moons.isMagicMoon()) && Moons.isNight()) {
            if((MobEntity)(Object)this instanceof HostileEntity){
                if (!this.hasStatusEffect(StatusEffects.STRENGTH)){
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 2 * 60 * 20,2));
                }
            }
        }
    }

    @Inject(at=@At("HEAD"), method="baseTick")
    public void baseTick(CallbackInfo ca) {
        //血月
        if ((Moons.isBloodMoon() || Moons.isMagicMoon()) && Moons.isNight()) {
            if((MobEntity)(Object)this instanceof HostileEntity && !((MobEntity)(Object)this instanceof EndermanEntity)){
                if(((MobEntity)(Object) this).getTarget() == null) {
                    PlayerEntity player = this.getWorld().getClosestPlayer(this.getX(), this.getY(), this.getZ(), 48, false);
                    if (player != null) {
                        ((MobEntity) (Object) this).setTarget(player);
                    }
                }
            }
        }
    }
}