package miao.fellas.commands;

import miao.fellas.containers.KitContainer;
import miao.fellas.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kits implements CommandExecutor {
    private final MessageManager messageManager;
    private final KitContainer kitContainer;

    public Kits(MessageManager messageManager, KitContainer kitContainer) {
        this.messageManager = messageManager;
        this.kitContainer = kitContainer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(messageManager.get("onlyPlayer", "Only players may use this command"));
            return true;
        }

        if (!sender.hasPermission("fellas.kits")) {
            player.sendMessage(messageManager.get(
                    "noPermission",
                    "<red>Insufficient permissions.</red> <purple>{permission}</purple>",
                    "{permission}",
                    "fellas.kits"
            ));
            return true;
        }

        kitContainer.openKitContainer(player);
        return true;
    }
}

