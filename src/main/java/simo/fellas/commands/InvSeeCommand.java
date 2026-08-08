package simo.fellas.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simo.fellas.managers.MessageManager;

public class InvSeeCommand implements CommandExecutor {
    private final MessageManager messageManager;

    public InvSeeCommand(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage(messageManager.get("onlyPlayer", "Only players may use this command"));
            return true;
        }

        if(!player.hasPermission("fellas.invsee")) {
            sender.sendMessage(messageManager.get(
                    "noPermission",
                    "<red>Insufficient permissions.</red> <dark_purple>({permission})</dark_purple>",
                    "{permission}",
                    "fellas.invsee"));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(messageManager.get("invseeUsage", "<red>Usage: /invsee [player]</red>"));
            return true;
        }

        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if(target == null) {
            sender.sendMessage(messageManager.get("invseePlayerNotFound", "<red>No player found.</red>"));
            return true;
        }

        player.openInventory(target.getInventory());

        return true;
    }
}
