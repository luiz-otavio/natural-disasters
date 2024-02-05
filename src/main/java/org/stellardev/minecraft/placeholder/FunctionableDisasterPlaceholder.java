package org.stellardev.minecraft.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stellardev.minecraft.DisasterPlugin;

import java.util.function.Function;

/**
 * @author Luiz O. F. Corrêa
 * @since 05/02/2024
 **/
public class FunctionableDisasterPlaceholder extends PlaceholderExpansion {

    private final String placeholder;
    private final Function<OfflinePlayer, String> function;

    public FunctionableDisasterPlaceholder(
      @NotNull String placeholder,
      @NotNull Function<OfflinePlayer, String> function
    ) {
        this.placeholder = placeholder;
        this.function = function;
    }

    @Override
    public @NotNull String getIdentifier() {
        return placeholder;
    }

    @Override
    public @NotNull String getAuthor() {
        return DisasterPlugin.getInstance()
          .getName();
    }

    @Override
    public @NotNull String getVersion() {
        return DisasterPlugin.getInstance()
          .getPluginMeta()
          .getVersion();
    }

    @Override
    public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {
        return function.apply(player);
    }
}
