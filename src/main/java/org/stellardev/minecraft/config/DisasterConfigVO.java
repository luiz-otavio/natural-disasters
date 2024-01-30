package org.stellardev.minecraft.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * @author Luiz O. F. Corrêa
 * @since 30/01/2024
 **/
@RequiredArgsConstructor
@Getter
@Setter
public class DisasterConfigVO {

    private final FileConfiguration parent;

    private final Settings settings;
    private final Timer timer;

    private final Meteor meteor;
    private final Thunderstorm thunderstorm;

    private final RandomCommands randomCommands;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Settings {

        private double chance;

    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Timer {

        private int time;

        private String showMethod;

        private List<String> showCommands;

        private List<String> endCommands;

    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Meteor {

        private boolean enabled;

        private double chance;

        private FilteredCoordinates filteredCoordinates;

        private List<String> commands;

        @AllArgsConstructor
        @Getter
        @Setter
        public static class FilteredCoordinates {

            private int maxX;
            private int maxY;
            private int maxZ;
            private int minX;
            private int minY;
            private int minZ;

        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Thunderstorm {

        private boolean enabled;

        private double chance;

        private boolean randomNearLighting;

        private int lightingTime;
        private int lightingDistance;

        private int commandsTime;

        private List<String> commands;
    }


    @AllArgsConstructor
    @Getter
    @Setter
    public static class RandomCommands {

        private List<Command> commands;

        @AllArgsConstructor
        @Getter
        @Setter
        public static class Command {

            private List<String> commands;

        }

    }

}
