package org.stellardev.minecraft;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.stellardev.minecraft.codec.ConfigCodec;
import org.stellardev.minecraft.command.ForceDisasterCommand;
import org.stellardev.minecraft.config.DisasterConfigVO;
import org.stellardev.minecraft.disaster.NaturalDisaster;
import org.stellardev.minecraft.disaster.impl.MeteorDisaster;
import org.stellardev.minecraft.disaster.impl.RandomCommandDisaster;
import org.stellardev.minecraft.disaster.impl.ThunderstormDisaster;
import org.stellardev.minecraft.placeholder.FunctionableDisasterPlaceholder;
import org.stellardev.minecraft.registry.DisasterRegistry;
import org.stellardev.minecraft.task.DisasterTimerHandlerTask;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
@Getter
public class DisasterPlugin extends JavaPlugin {

    public static DisasterPlugin getInstance() {
        return getPlugin(DisasterPlugin.class);
    }

    private DisasterConfigVO disasterConfigVO;
    private DisasterRegistry disasterRegistry;

    private DisasterTimerHandlerTask handlerTask;

    @Override
    public void onLoad() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        saveDefaultConfig();

        disasterConfigVO = ConfigCodec.readFromConfiguration(getConfig());
    }

    @Override
    public void onEnable() {
        disasterRegistry = new DisasterRegistry();
        disasterRegistry.addAll(
          new MeteorDisaster(disasterConfigVO.getMeteor()),
          new ThunderstormDisaster(
            disasterConfigVO.getThunderstorm(),
            disasterConfigVO.getTimer()
          ),
          new RandomCommandDisaster(disasterConfigVO.getRandomCommands())
        );

        Bukkit.getScheduler()
          .runTaskTimer(
            this,
            handlerTask = new DisasterTimerHandlerTask(disasterConfigVO, disasterRegistry),
            20L,
            20L
          );

        Bukkit.getCommandMap()
          .register("forcedisaster", new ForceDisasterCommand(this));

        registerPlaceholders();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        Bukkit.getScheduler()
          .cancelTasks(this);
    }

    private void registerPlaceholders() {
        new FunctionableDisasterPlaceholder("natural_disaster_coords", unused -> {
            NaturalDisaster current = handlerTask.getCurrent();
            if (current == null) {
                return null;
            }

            if (current instanceof MeteorDisaster meteorDisaster) {
                return "%.2f, %.2f, %.2f".formatted(
                  meteorDisaster.getCurrentX(),
                  meteorDisaster.getCurrentY(),
                  meteorDisaster.getCurrentZ()
                );
            }

            return null;
        }).register();

        new FunctionableDisasterPlaceholder("natural_disaster_coords_x", unused -> {
            NaturalDisaster current = handlerTask.getCurrent();
            if (current == null) {
                return null;
            }

            if (current instanceof MeteorDisaster meteorDisaster) {
                return "%.2f".formatted(meteorDisaster.getCurrentX());
            }

            return null;
        }).register();

        new FunctionableDisasterPlaceholder("natural_disaster_coords_y", unused -> {
            NaturalDisaster current = handlerTask.getCurrent();
            if (current == null) {
                return null;
            }

            if (current instanceof MeteorDisaster meteorDisaster) {
                return "%.2f".formatted(meteorDisaster.getCurrentY());
            }

            return null;
        }).register();

        new FunctionableDisasterPlaceholder("natural_disaster_coords_z", unused -> {
            NaturalDisaster current = handlerTask.getCurrent();
            if (current == null) {
                return null;
            }

            if (current instanceof MeteorDisaster meteorDisaster) {
                return "%.2f".formatted(meteorDisaster.getCurrentZ());
            }

            return null;
        }).register();

        new FunctionableDisasterPlaceholder("natural_disaster_random", unused -> {
            NaturalDisaster current = handlerTask.getCurrent();
            if (current == null) {
                return null;
            }

            if (current instanceof ThunderstormDisaster thunderstormDisaster) {
                Player player = thunderstormDisaster.getRandom();
                if (player != null) {
                    Location location = player.getLocation();
                    return "%.2f, %.2f, %.2f".formatted(
                      location.getX(),
                      location.getY(),
                      location.getZ()
                    );
                }
            }

            return null;
        }).register();

        new FunctionableDisasterPlaceholder("natural_disaster_random_x", unused -> {
            NaturalDisaster current = handlerTask.getCurrent();
            if (current == null) {
                return null;
            }

            if (current instanceof ThunderstormDisaster thunderstormDisaster) {
                Player player = thunderstormDisaster.getRandom();
                if (player != null) {
                    return "%.2f".formatted(
                      player.getLocation()
                        .getX()
                    );
                }
            }

            return null;
        }).register();

        new FunctionableDisasterPlaceholder("natural_disaster_random_y", unused -> {
            NaturalDisaster current = handlerTask.getCurrent();
            if (current == null) {
                return null;
            }

            if (current instanceof ThunderstormDisaster thunderstormDisaster) {
                Player player = thunderstormDisaster.getRandom();
                if (player != null) {
                    return "%.2f".formatted(
                      player.getLocation()
                        .getY()
                    );
                }
            }

            return null;
        }).register();

        new FunctionableDisasterPlaceholder("natural_disaster_random_z", unused -> {
            NaturalDisaster current = handlerTask.getCurrent();
            if (current == null) {
                return null;
            }

            if (current instanceof ThunderstormDisaster thunderstormDisaster) {
                Player player = thunderstormDisaster.getRandom();
                if (player != null) {
                    return "%.2f".formatted(
                      player.getLocation()
                        .getZ()
                    );
                }
            }

            return null;
        }).register();
    }
}
