package org.stellardev.minecraft.disaster.impl;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.stellardev.minecraft.DisasterPlugin;
import org.stellardev.minecraft.config.DisasterConfigVO;
import org.stellardev.minecraft.disaster.AbstractNaturalDisaster;
import org.stellardev.minecraft.task.ThunderstormCommandsHandlerTask;
import org.stellardev.minecraft.task.ThunderstormLightingHandlerTask;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
@Getter
public class ThunderstormDisaster extends AbstractNaturalDisaster {

    private final DisasterConfigVO.Thunderstorm thunderstorm;
    private final DisasterConfigVO.Timer timer;

    @Setter
    private Player random;

    public ThunderstormDisaster(
      @NotNull DisasterConfigVO.Thunderstorm thunderstorm,
      @NotNull DisasterConfigVO.Timer timer
    ) {
        super("thunderstorm", thunderstorm.isEnabled(), thunderstorm.getChance());

        this.thunderstorm = thunderstorm;
        this.timer = timer;
    }

    @Override
    public void execute() {
        if (thunderstorm.isRandomNearLighting()) {
            Bukkit.getScheduler()
              .runTaskTimer(
                DisasterPlugin.getInstance(),
                new ThunderstormLightingHandlerTask(
                  thunderstorm.getLightingDistance(),
                  thunderstorm.getLightingTime(),
                  timer.getTime()
                ),
                thunderstorm.getLightingTime() * 20L,
                thunderstorm.getLightingTime() * 20L
              );
        }

        if (thunderstorm.isChangeWeather()) {
            for (@NotNull World world : Bukkit.getWorlds()) {
                world.setStorm(true);
            }
        }

        Bukkit.getScheduler()
          .runTaskTimer(
            DisasterPlugin.getInstance(),
            new ThunderstormCommandsHandlerTask(
              thunderstorm.getCommands(),
              thunderstorm.getCommandsTime(),
              timer.getTime(),
              this
            ),
            thunderstorm.getCommandsTime() * 20L,
            thunderstorm.getCommandsTime() * 20L
          );
    }

    @Override
    public void onStop() {
        if (thunderstorm.isChangeWeather()) {
            for (@NotNull World world : Bukkit.getWorlds()) {
                world.setStorm(false);
            }
        }
    }
}
