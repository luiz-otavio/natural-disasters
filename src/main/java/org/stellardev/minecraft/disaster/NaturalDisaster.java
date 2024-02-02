package org.stellardev.minecraft.disaster;

import org.jetbrains.annotations.NotNull;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
public interface NaturalDisaster {

    @NotNull
    String getName();

    boolean isEnabled();

    double getChance();

    void execute();

    void onStop();

}
