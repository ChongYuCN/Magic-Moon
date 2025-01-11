package com.chongyu.magicmoon.mixin.client;

import com.chongyu.magicmoon.core.Moons;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public abstract class WorldRenderMixin{
    @Shadow
    private static final Identifier MOON_PHASES = Identifier.ofVanilla("textures/environment/moon_phases.png");

    @Shadow
    private static final Identifier RAIN = Identifier.ofVanilla("textures/environment/rain.png");

    @Unique
    private static final Identifier HARVEST_MOON_PHASES = Identifier.of("magicmoon:textures/environment/harvest_moon_phases.png");
    @Unique
    private static final Identifier BLOOD_MOON_PHASES = Identifier.of("magicmoon:textures/environment/blood_moon_phases.png");
    @Unique
    private static final Identifier BLUE_MOON_PHASES = Identifier.of("magicmoon:textures/environment/blue_moon_phases.png");
    @Unique
    private static final Identifier MAGIC_MOON_PHASES = Identifier.of("magicmoon:textures/environment/magic_moon_phases.png");

    @Unique
    private static final Identifier BLOOD_RAIN = Identifier.of("magicmoon:textures/environment/blood_rain.png");
    @Unique
    private static final Identifier MAGIC_RAIN = Identifier.of("magicmoon:textures/environment/magic_rain.png");

    @Redirect(method = "renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 1))
    public void renderSkyMoon(int texture, Identifier id) {
        if(Moons.isBloodMoon()){
            RenderSystem.setShaderTexture(0, BLOOD_MOON_PHASES);
        }else if(Moons.isBlueMoon()){
            RenderSystem.setShaderTexture(0, BLUE_MOON_PHASES);
        }else if(Moons.isHarvestMoon()){
            RenderSystem.setShaderTexture(0, HARVEST_MOON_PHASES);
        }else if(Moons.isMagicMoon()){
            RenderSystem.setShaderTexture(0, MAGIC_MOON_PHASES);
        }else {
            RenderSystem.setShaderTexture(0, MOON_PHASES);
        }
    }

    @Redirect(method = "renderWeather", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 0))
    public void renderWeatherRain(int texture, Identifier id) {
        if(Moons.isBloodMoon()){
            RenderSystem.setShaderTexture(0, BLOOD_RAIN);
        }else if(Moons.isMagicMoon()){
            RenderSystem.setShaderTexture(0, MAGIC_RAIN);
        }else {
            RenderSystem.setShaderTexture(0, RAIN);
        }
    }
}
