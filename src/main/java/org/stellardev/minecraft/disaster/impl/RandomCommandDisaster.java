package org.stellardev.minecraft.disaster.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.stellardev.minecraft.config.DisasterConfigVO;
import org.stellardev.minecraft.disaster.AbstractNaturalDisaster;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
public class RandomCommandDisaster extends AbstractNaturalDisaster {

    private final DisasterConfigVO.RandomCommands randomCommands;

    public RandomCommandDisaster(@NotNull DisasterConfigVO.RandomCommands randomCommands) {
        super("random-commands", randomCommands.isEnabled(), (double) 1 / randomCommands.getCommands().size());

        this.randomCommands = randomCommands;
    }

    @Override
    public void execute() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        double totalWeight = 0d;
        for (@NotNull DisasterConfigVO.RandomCommands.Command command : randomCommands.getCommands()) {
            totalWeight += command.getChance();
        }

        double randomWeight = threadLocalRandom.nextDouble() * totalWeight,
          currentWeight = 0;

        DisasterConfigVO.RandomCommands.Command next = null;
        for (@NotNull DisasterConfigVO.RandomCommands.Command command : randomCommands.getCommands()) {
            currentWeight += command.getChance();
            if (currentWeight >= randomWeight) {
                next = command;
                break;
            }
        }

        requireNonNull(next, "Cannot execute a random command.");

        Player chosenPlayer = Bukkit.getOnlinePlayers()
          .stream()
          .findAny()
          .orElse(null);

        if (chosenPlayer != null) {
            for (@NotNull String command : next.getCommands()) {
                if (
                  Bukkit.getPluginManager()
                    .isPluginEnabled("PlaceholderAPI")
                ) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(chosenPlayer, command));
                } else {
                    Location location = chosenPlayer.getLocation();

                    Bukkit.dispatchCommand(
                      Bukkit.getConsoleSender(),
                      command.replaceAll("%random%", chosenPlayer.getName())
                        .replaceAll("%random_x%", valueOf(location.getBlockX()))
                        .replaceAll("%random_y%", valueOf(location.getBlockY()))
                        .replaceAll("%random_z%", valueOf(location.getBlockZ()))
                    );
                }
            }
        }
    }

    @Override
    public void onStop() {
    }
}
