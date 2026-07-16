package miao.fellas.commands;

import miao.fellas.utils.MessageUtil;
import miao.fellas.managers.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class KitReloadCommand implements CommandExecutor {
    private final KitManager kitManager;
    private final Plugin plugin;

    public KitReloadCommand(KitManager kitManager, Plugin plugin) {
        this.kitManager = kitManager;
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             String @NotNull []  args) {

        if (!sender.hasPermission("fellas.kit.reload")) {
            sender.sendMessage(MessageUtil.color("<red>Permessi insufficienti.</red> <purple>(fellas.kit.reload)</purple>"));
            return true;
        }

        plugin.reloadConfig();
        kitManager.reloadKits();

        sender.sendMessage(MessageUtil.color("<green>Kits reloaded.</green>"));

        return true;
    }
}
