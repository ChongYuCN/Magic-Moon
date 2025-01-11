package com.chongyu.magicmoon.mixin.client.bloodmoonworld;

import com.chongyu.magicmoon.core.Moons;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin implements AutoCloseable{

    @Inject(
            method = {"update"},
            at = {@At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/LightmapTextureManager;flickerIntensity:F"
            )},
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    //获取常量flickerIntensity之前的局部变量，并修改目标变量
    public void update(float delta, CallbackInfo ci, ClientWorld clientWorld, float f, float g, float h, float i, float j, float l, float k, Vec3f vec3f){
        if(Moons.isBloodMoon() || Moons.isMagicMoon()){
            Vec3f modifiedColor = new Vec3f(1.0F, 0.0F, 0.0F); // 血红色
            float skyBlend = 1.0F - f - clientWorld.getRainGradient(1.0F);
            vec3f.lerp(modifiedColor, skyBlend);
        }
    }
}
