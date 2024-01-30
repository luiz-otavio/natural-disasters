package org.stellardev.minecraft.registry;

import org.jetbrains.annotations.NotNull;
import org.stellardev.minecraft.disaster.NaturalDisaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
public class DisasterRegistry {

    private final List<NaturalDisaster> naturalDisasters = new ArrayList<>();

    public void add(@NotNull NaturalDisaster naturalDisaster) {
        naturalDisasters.add(naturalDisaster);
    }

    public void remove(@NotNull NaturalDisaster naturalDisaster) {
        naturalDisasters.remove(naturalDisaster);
    }

    public void addAll(@NotNull NaturalDisaster... naturalDisasters) {
        for (@NotNull NaturalDisaster naturalDisaster : naturalDisasters) {
            add(naturalDisaster);
        }
    }

    public void removeAll(@NotNull NaturalDisaster... naturalDisasters) {
        for (@NotNull NaturalDisaster naturalDisaster : naturalDisasters) {
            remove(naturalDisaster);
        }
    }

    public List<NaturalDisaster> getNaturalDisasters() {
        return Collections.unmodifiableList(naturalDisasters);
    }
}
