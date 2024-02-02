package org.stellardev.minecraft.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.stellardev.minecraft.DisasterPlugin;
import org.stellardev.minecraft.disaster.NaturalDisaster;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Luiz O. F. Corrêa
 * @since 02/02/2024
 **/
public class ForceDisasterCommand extends Command {

    private final DisasterPlugin disasterPlugin;

    public ForceDisasterCommand(@NotNull DisasterPlugin disasterPlugin) {
        super(
          "forcedisaster",
          "Forces a disaster to happen.",
          "/forcedisaster [disaster]",
          Collections.emptyList()
        );

        this.disasterPlugin = disasterPlugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("naturaldisasters.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to execute this command.");
            return true;
        }

        NaturalDisaster naturalDisaster = null;
        if (args.length == 0) {
            double totalWeight = disasterPlugin.getDisasterRegistry()
              .getNaturalDisasters()
              .stream()
              .mapToDouble(NaturalDisaster::getChance)
              .sum();

            double randomWeight = ThreadLocalRandom.current()
              .nextDouble() * totalWeight,
              currentWeight = 0d;

            for (
              @NotNull NaturalDisaster disaster : disasterPlugin.getDisasterRegistry().getNaturalDisasters()
            ) {
                if (!disaster.isEnabled()) {
                    continue;
                }

                currentWeight += disaster.getChance();
                if (currentWeight >= randomWeight) {
                    naturalDisaster = disaster;
                    break;
                }
            }
        } else if (args.length == 1) {
            String name = args[0].toLowerCase();

            naturalDisaster = disasterPlugin.getDisasterRegistry()
              .getNaturalDisasters()
              .stream()
              .filter(disaster -> disaster.getName().equals(name))
              .findFirst()
              .orElse(null);

            if (naturalDisaster == null) {
                sender.sendMessage(ChatColor.RED + "We didn't found any disasters with this name. All available are: Thunderstorm, Random-Commands and Meteor.");
                return true;
            }
        }

        if (naturalDisaster == null) {
            return true;
        }

        disasterPlugin.getHandlerTask()
          .handle(naturalDisaster);

        sender.sendMessage(ChatColor.GREEN + "Applied the new natural disaster.");
        return true;
    }
}
