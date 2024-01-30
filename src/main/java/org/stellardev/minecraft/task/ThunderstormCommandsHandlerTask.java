package org.stellardev.minecraft.task;

import lombok.AllArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.stellardev.minecraft.disaster.impl.ThunderstormDisaster;

import java.util.List;

import static java.lang.String.valueOf;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
@AllArgsConstructor
public class ThunderstormCommandsHandlerTask extends BukkitRunnable {

    private final List<String> commands;

    private int subtract;
    private int ticks;

    private ThunderstormDisaster disaster;

    @Override
    public void run() {
        Player chosenPlayer = Bukkit.getOnlinePlayers()
          .stream()
          .findAny()
          .orElse(null);

        disaster.setRandom(chosenPlayer);
        if (chosenPlayer != null) {
            for (@NotNull String command : commands) {
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

        ticks -= subtract;
        if (ticks <= 0) {
            cancel();
        }
    }

}
