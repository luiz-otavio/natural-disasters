package org.stellardev.minecraft;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.stellardev.minecraft.codec.ConfigCodec;
import org.stellardev.minecraft.command.ForceDisasterCommand;
import org.stellardev.minecraft.config.DisasterConfigVO;
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

        Bukkit.getScheduler()
          .runTaskTimer(
            this,
            handlerTask = new DisasterTimerHandlerTask(disasterConfigVO, disasterRegistry),
            20L,
            20L
          );

        Bukkit.getCommandMap()
          .register("forcedisaster", new ForceDisasterCommand(this));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        Bukkit.getScheduler()
          .cancelTasks(this);
    }
}
