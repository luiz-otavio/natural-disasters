package org.stellardev.minecraft.disaster;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
public abstract class AbstractNaturalDisaster implements NaturalDisaster {

    private final boolean enabled;

    private final double chance;

    public AbstractNaturalDisaster(boolean enabled, double chance) {
        this.enabled = enabled;
        this.chance = chance;
    }

    @Override
    public double getChance() {
        return chance;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}
