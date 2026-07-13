package miao.fellas.tabs;

import miao.fellas.managers.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KitTabCompleter implements TabCompleter {

    private final KitManager kitManager;

    public KitTabCompleter(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        List<String> suggestions = new ArrayList<>();

        if(args.length == 1) {
            String scritto = args[0].toLowerCase();

            for (String kitName : kitManager.getKitNames()) {
                if (kitName.toLowerCase().startsWith(scritto)) {
                    suggestions.add(kitName);
                }
            }
        }
        return suggestions;
    }
}
