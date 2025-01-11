package com.chongyu.magicmoon.mixin.client;

import com.chongyu.magicmoon.core.MoonIdentifier;
import com.chongyu.magicmoon.core.RainIdentifier;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;

@Mixin(WorldRenderer.class)
public abstract class WorldRenderMixin {
    @Mutable
    @Final
    @Shadow
    private static final Identifier MOON_PHASES;

    @Mutable
    @Final
    @Shadow
    private static final Identifier RAIN;

    static {
        MOON_PHASES = new MoonIdentifier("textures/environment/moon_phases.png");
        RAIN = new RainIdentifier("textures/environment/rain.png");
    }

}
