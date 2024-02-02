package org.stellardev.minecraft.disaster;

import org.jetbrains.annotations.NotNull;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
public abstract class AbstractNaturalDisaster implements NaturalDisaster {

    private final String name;

    private final boolean enabled;

    private final double chance;

    public AbstractNaturalDisaster(@NotNull String name, boolean enabled, double chance) {
        this.name = name;
        this.enabled = enabled;
        this.chance = chance;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
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
