package org.stellardev.minecraft.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stellardev.minecraft.config.DisasterConfigVO;
import org.stellardev.minecraft.disaster.NaturalDisaster;
import org.stellardev.minecraft.registry.DisasterRegistry;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
@RequiredArgsConstructor
public class DisasterTimerHandlerTask implements Runnable {

    private static final ThreadLocalRandom THREAD_LOCAL_RANDOM = ThreadLocalRandom.current();

    private final DisasterConfigVO configVO;
    private final DisasterRegistry disasterRegistry;

    private int currentTime = configVO.getTimer()
      .getTime();

    private BossBar bossBar;

    @Getter
    private NaturalDisaster current;

    @Override
    public void run() {
        if (bossBar == null) {
            bossBar = buildBoosBar();
        }

        int targetTime = currentTime--;
        if (targetTime > 0) {
            double maxTime = configVO.getTimer()
              .getTime();

            bossBar.setProgress(
              Math.max(0.0d, (targetTime / maxTime))
            );
        } else if (targetTime == 0) {
            bossBar.setProgress(0.0d);
        }

        for (@NotNull Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }

        if (targetTime > 0) {
            return;
        }

        if (current != null) {
            current.onStop();
            current = null;
        }

        DisasterConfigVO.Settings settings = configVO.getSettings();
        if (settings.getChance() != -1) {
            ThreadLocalRandom localRandom = ThreadLocalRandom.current();

            double chance = localRandom.nextDouble();
            if (chance > settings.getChance()) {
                reset(true);
                return;
            }
        }

        handle(null);
    }

    public void handle(@Nullable NaturalDisaster naturalDisaster) {
        if (naturalDisaster != null) {
            if (current != null) {
                current.onStop();
                current = null;
            }

            configVO.getTimer()
              .getEndCommands()
              .forEach(execute -> {
                  if (
                    Bukkit.getPluginManager()
                      .isPluginEnabled("PlaceholderAPI")
                  ) {
                      for (@NotNull Player player : Bukkit.getOnlinePlayers()) {
                          Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            PlaceholderAPI.setPlaceholders(player, execute)
                          );
                      }
                  } else {
                      for (@NotNull Player player : Bukkit.getOnlinePlayers()) {
                          Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            execute.replaceAll("%natural_disaster_player%", player.getName())
                          );
                      }
                  }
              });

            naturalDisaster.execute();
            current = naturalDisaster;

            reset(false);
        } else {
            double totalWeight = disasterRegistry.getNaturalDisasters()
              .stream()
              .mapToDouble(NaturalDisaster::getChance)
              .sum();

            double randomWeight = THREAD_LOCAL_RANDOM.nextDouble() * totalWeight,
              currentWeight = 0d;

            NaturalDisaster next = null;
            for (@NotNull NaturalDisaster disaster : disasterRegistry.getNaturalDisasters()) {
                if (!disaster.isEnabled()) {
                    continue;
                }

                currentWeight += naturalDisaster.getChance();
                if (currentWeight >= randomWeight) {
                    next = disaster;
                    break;
                }
            }

            if (next != null) {
                handle(next);
            }
        }
    }

    private void reset(boolean thrown) {
        currentTime = configVO.getTimer()
          .getTime();

        if (!thrown) {
            return;
        }

        configVO.getTimer()
          .getShowCommands()
          .forEach(execute -> {
              if (
                Bukkit.getPluginManager()
                  .isPluginEnabled("PlaceholderAPI")
              ) {
                  for (@NotNull Player player : Bukkit.getOnlinePlayers()) {
                      Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        PlaceholderAPI.setPlaceholders(player, execute)
                      );
                  }
              } else {
                  for (@NotNull Player player : Bukkit.getOnlinePlayers()) {
                      Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        execute.replaceAll("%natural_disaster_player%", player.getName())
                      );
                  }
              }
          });
    }

    private BossBar buildBoosBar() {
        BossBar bossBar = Bukkit.createBossBar("§aDISASTER BAR", BarColor.GREEN, BarStyle.SEGMENTED_10);
        bossBar.setVisible(true);

        return bossBar;
    }
}
