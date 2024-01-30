package org.stellardev.minecraft.task;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class ThunderstormLightingHandlerTask extends BukkitRunnable {

    private static final ThreadLocalRandom LOCAL_RANDOM = ThreadLocalRandom.current();

    private final int maxDistance;

    private int subtract;
    private int ticks;

    @Override
    public void run() {
        for (@NotNull Player player : Bukkit.getOnlinePlayers()) {
            int targetX = LOCAL_RANDOM.nextInt(-maxDistance, maxDistance),
              targetZ = LOCAL_RANDOM.nextInt(-maxDistance, maxDistance);

            Location location = player.getLocation()
              .clone()
              .add(targetX, 0, targetZ);

            player.getWorld()
              .strikeLightning(location);
        }

        ticks -= subtract;
        if (ticks <= 0) {
            cancel();
        }
    }
}
