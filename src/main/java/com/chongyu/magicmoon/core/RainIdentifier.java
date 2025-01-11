package com.chongyu.magicmoon.core;

import net.minecraft.util.Identifier;

public class RainIdentifier extends Identifier {

    private final Identifier RAIN = new Identifier("textures/environment/rain.png");
    private final Identifier BLOOD_RAIN = new Identifier("magicmoon:textures/environment/blood_rain.png");
    private final Identifier MAGIC_RAIN = new Identifier("magicmoon:textures/environment/magic_rain.png");

    public RainIdentifier(String id) {
        super(id);
    }

    public Identifier getIdentifier() {
        return Moons.isBloodMoon() ? BLOOD_RAIN :
                Moons.isMagicMoon() ? MAGIC_RAIN :
                        RAIN;
    }

    public String getPath() {
        return getIdentifier().getPath();
    }

    public String getNamespace() {
        return getIdentifier().getNamespace();
    }

    public String toString() {
        return this.getNamespace() + ":" + this.getPath();
    }

    public boolean equals(Object o) {
        return getIdentifier().equals(o);
    }

    public int hashCode() {
        return getIdentifier().hashCode();
    }

    public int compareTo(Identifier identifier) {
        return getIdentifier().compareTo(identifier);
    }

    public String toUnderscoreSeparatedString() {
        return getIdentifier().toUnderscoreSeparatedString();
    }

}
