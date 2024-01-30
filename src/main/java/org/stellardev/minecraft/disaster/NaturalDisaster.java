package org.stellardev.minecraft.disaster;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
public interface NaturalDisaster {

    boolean isEnabled();

    double getChance();

    void execute();

    void onStop();

}
