package miao.fellas.commands;

import miao.fellas.managers.KitManager;
import miao.fellas.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class KitReloadCommand implements CommandExecutor {
    private final KitManager kitManager;
    private final Plugin plugin;
    private final MessageManager messageManager;


    public KitReloadCommand(KitManager kitManager, MessageManager messageManager, Plugin plugin) {
        this.kitManager = kitManager;
        this.messageManager = messageManager;
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             String @NotNull []  args) {

        if (!sender.hasPermission("fellas.kit.reload")) {
            sender.sendMessage(messageManager.get(
                    "noPermission",
                    "<red>Insufficient Permissions.</red> <purple>{permission}</purple>",
                    "{permission}",
                    "fellas.kit.reload"
            ));
            return true;
        }

        plugin.reloadConfig();
        kitManager.reloadKits();

        sender.sendMessage(messageManager.get(
                "kitReload",
                "<green>Kits reloaded.</green>"
        ));

        return true;
    }
}
