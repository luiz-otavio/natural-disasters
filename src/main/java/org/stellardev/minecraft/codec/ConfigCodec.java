package org.stellardev.minecraft.codec;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.stellardev.minecraft.config.DisasterConfigVO;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
public class ConfigCodec {

    public static DisasterConfigVO readFromConfiguration(@NotNull FileConfiguration fileConfiguration) {
        ConfigurationSection settingsSection = fileConfiguration.getConfigurationSection("settings"),
          timerSection = fileConfiguration.getConfigurationSection("timer"),
          meteorSection = fileConfiguration.getConfigurationSection("meteor"),
          thunderstormSection = fileConfiguration.getConfigurationSection("thunderstorm"),
          randomCommandsSection = fileConfiguration.getConfigurationSection("random_commands");

        requireNonNull(settingsSection, "Settings section cannot be null.");
        requireNonNull(timerSection, "Timer section cannot be null.");
        requireNonNull(meteorSection, "Meteor section cannot be null.");
        requireNonNull(thunderstormSection, "Thunderstorm section cannot be null.");
        requireNonNull(randomCommandsSection, "Random commands section cannot be null.");

        return new DisasterConfigVO(
          fileConfiguration,
          buildSettings(settingsSection),
          buildTimer(timerSection),
          buildMeteor(meteorSection),
          buildThunderstorm(thunderstormSection),
          buildRandomCommands(randomCommandsSection)
        );
    }

    private static DisasterConfigVO.Settings buildSettings(@NotNull ConfigurationSection section) {
        double chance = section.getDouble("chance_to_happen", 0.1d);
        checkArgument(chance >= -1 && chance != 0, "Chance cannot be negative or zero unless its -1");

        return new DisasterConfigVO.Settings(chance);
    }

    private static DisasterConfigVO.Timer buildTimer(@NotNull ConfigurationSection section) {
        int time = section.getInt("time", 120);
        checkArgument(time > 0, "Time cannot be negative or zero.");

        String showMethod = section.getString("show_method", "default");
        checkArgument(showMethod.equals("default"), "Show method just supports default method.");

        List<String> showCommands = section.getStringList("show_commands"),
          endCommands = section.getStringList("end_commands");

        showCommands.replaceAll(string -> ChatColor.translateAlternateColorCodes('&', string));
        endCommands.replaceAll(string -> ChatColor.translateAlternateColorCodes('&', string));

        return new DisasterConfigVO.Timer(time, showMethod, showCommands, endCommands);
    }

    private static DisasterConfigVO.Meteor buildMeteor(@NotNull ConfigurationSection section) {
        boolean enabled = section.getBoolean("enabled", false);

        double chance = section.getDouble("chance", 0.1d);
        checkArgument(chance >= -1 && chance != 0, "Chance cannot be negative or zero unless its -1");

        DisasterConfigVO.Meteor.FilteredCoordinates filteredCoordinates;

        ConfigurationSection coordinatesSection = section.getConfigurationSection("filtered_coordinates");
        requireNonNull(coordinatesSection, "Coordinates section cannot be null");

        int maxX = coordinatesSection.getInt("max_x", -1),
          maxY = coordinatesSection.getInt("max_y", -1),
          maxZ = coordinatesSection.getInt("max_z", -1),
          minX = coordinatesSection.getInt("min_x", -1),
          minY = coordinatesSection.getInt("min_y", -1),
          minZ = coordinatesSection.getInt("min_z", -1);

        filteredCoordinates = new DisasterConfigVO.Meteor.FilteredCoordinates(maxX, maxY, maxZ, minX, minY, minZ);

        List<String> commands = section.getStringList("commands_when_appear");
        commands.replaceAll(string -> ChatColor.translateAlternateColorCodes('&', string));

        return new DisasterConfigVO.Meteor(enabled, chance, filteredCoordinates, commands);
    }

    private static DisasterConfigVO.Thunderstorm buildThunderstorm(@NotNull ConfigurationSection section) {
        boolean enabled = section.getBoolean("enabled", false);

        double chance = section.getDouble("chance", 0.1d);
        checkArgument(chance > 0 || chance == -1, "Chance cannot be negative or zero unless its -1");

        boolean randomNearLighting = section.getBoolean("random_near_lighting", false);

        int lightingTime = section.getInt("random_near_lighting_time", 30);
        checkArgument(lightingTime > 0, "Near lighting time cannot be negative or zero.");

        int maxDistance = section.getInt("random_near_lighting_max_distance", 15);
        checkArgument(maxDistance > 0, "Near lighting max distance cannot be negative or zero.");

        int commandsTime = section.getInt("random_near_commands_time", 15);
        checkArgument(commandsTime > 0, "Near commands time cannot be negative or zero.");

        List<String> commands = section.getStringList("random_near_commands");
        commands.replaceAll(string -> ChatColor.translateAlternateColorCodes('&', string));

        return new DisasterConfigVO.Thunderstorm(enabled, chance,  randomNearLighting, lightingTime, maxDistance, commandsTime, commands);
    }

    private static DisasterConfigVO.RandomCommands buildRandomCommands(@NotNull ConfigurationSection section) {
        List<DisasterConfigVO.RandomCommands.Command> commands = new ArrayList<>();

        for (@NotNull String key : section.getKeys(false)) {
            List<String> commandsKey = section.getStringList(key);
            commandsKey.replaceAll(string -> ChatColor.translateAlternateColorCodes('&', string));

            commands.add(new DisasterConfigVO.RandomCommands.Command(commandsKey));
        }

        return new DisasterConfigVO.RandomCommands(commands);
    }

}
