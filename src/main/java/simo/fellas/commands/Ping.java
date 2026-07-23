package simo.fellas.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simo.fellas.managers.MessageManager;

public class Ping implements CommandExecutor {
    private final MessageManager messageManager;

    public Ping(MessageManager messageManager) {
        this.messageManager = messageManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        if (!sender.hasPermission("fellas.ping")) {
            player.sendMessage(messageManager.get(
                    "noPermission",
                    "<red>Insufficient permissions.</red> <purple>{permission}</purple>",
                    "{permission}",
                    "fellas.ping"
            ));
            return true;
        }

        int ping = player.getPing();

        player.sendMessage("Ping: " + ping);

        return true;
    }
}
