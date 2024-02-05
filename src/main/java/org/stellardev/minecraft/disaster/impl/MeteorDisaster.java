package org.stellardev.minecraft.disaster.impl;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.stellardev.minecraft.config.DisasterConfigVO;
import org.stellardev.minecraft.disaster.AbstractNaturalDisaster;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.valueOf;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
@Getter
public class MeteorDisaster extends AbstractNaturalDisaster {

    private static final ThreadLocalRandom LOCAL_RANDOM = ThreadLocalRandom.current();

    private final DisasterConfigVO.Meteor meteor;

    private double currentX, currentY, currentZ;

    public MeteorDisaster(@NotNull DisasterConfigVO.Meteor meteor) {
        super("meteor", meteor.isEnabled(), meteor.getChance());

        this.meteor = meteor;
    }

    @Override
    public void execute() {
        DisasterConfigVO.Meteor.FilteredCoordinates coordinates = meteor.getFilteredCoordinates();

        int maxX = coordinates.getMaxX(),
          maxY = coordinates.getMaxY(),
          maxZ = coordinates.getMaxZ(),
          minX = coordinates.getMinX(),
          minY = coordinates.getMinY(),
          minZ = coordinates.getMinZ();

        currentX = randomNear(maxX, minX);
        currentY = randomNear(maxY, minY);
        currentZ = randomNear(maxZ, minZ);

        List<String> commands = meteor.getCommands();
        if (
          Bukkit.getPluginManager()
            .isPluginEnabled("PlaceholderAPI")
        ) {
            for (@NotNull String command : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(null, command));
            }
        } else {
            for (@NotNull String command : commands) {
                Bukkit.dispatchCommand(
                  Bukkit.getConsoleSender(),
                  command.replaceAll(
                      "%coords%",
                      "%.2f, %.2f, %.2f".formatted(currentX, currentY, currentZ)
                    ).replaceAll("%natural_disaster_coords_x%", valueOf(currentX))
                    .replaceAll("%natural_disaster_coords_y%", valueOf(currentY))
                    .replaceAll("%natural_disaster_coords_z%", valueOf(currentZ))
                );
            }
        }

    }

    @Override
    public void onStop() {}

    private double randomNear(int max, int min) {
        if (max != -1 && min != -1) {
            return LOCAL_RANDOM.nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE) + 0.5d;
        } else if (max == -1 && min != -1) {
            return LOCAL_RANDOM.nextInt(Integer.MIN_VALUE, max) + 0.5d;
        } else if (max != -1 && min == -1) {
            return LOCAL_RANDOM.nextInt(min, Integer.MAX_VALUE) + 0.5d;
        } else {
            return LOCAL_RANDOM.nextInt(min, max) + 0.5d;
        }
    }
}
