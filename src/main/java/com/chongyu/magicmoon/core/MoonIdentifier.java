package com.chongyu.magicmoon.core;

import net.minecraft.util.Identifier;

public class MoonIdentifier extends Identifier {

    private final Identifier MOON_PHASES = new Identifier("textures/environment/moon_phases.png");
    private final Identifier HARVEST_MOON_PHASES = new Identifier("magicmoon:textures/environment/harvest_moon_phases.png");
    private final Identifier BLOOD_MOON_PHASES = new Identifier("magicmoon:textures/environment/blood_moon_phases.png");
    private final Identifier BLUE_MOON_PHASES = new Identifier("magicmoon:textures/environment/blue_moon_phases.png");
    private final Identifier MAGIC_MOON_PHASES = new Identifier("magicmoon:textures/environment/magic_moon_phases.png");

    public MoonIdentifier(String id) {
        super(id);
    }

    public Identifier getIdentifier() {
        return Moons.isBloodMoon() ? BLOOD_MOON_PHASES :
                Moons.isBlueMoon() ? BLUE_MOON_PHASES :
                        Moons.isHarvestMoon() ? HARVEST_MOON_PHASES :
                                Moons.isMagicMoon() ? MAGIC_MOON_PHASES :
                                    MOON_PHASES;
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
