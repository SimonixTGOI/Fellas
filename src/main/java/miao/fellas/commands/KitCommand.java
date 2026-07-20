package miao.fellas.commands;


import miao.fellas.containers.KitContainer;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;


public class KitCommand implements CommandExecutor {

    private final KitManager kitManager;
    private final MessageManager messageManager;


    public KitCommand(KitManager kitManager, MessageManager messageManager) {
        this.kitManager = kitManager;
        this.messageManager = messageManager;
    }




    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command , @NotNull String label, @NotNull String[] args) {


        if (!(sender instanceof Player player)) {
            sender.sendMessage(messageManager.get("onlyPlayer", "Only players may use this command"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(messageManager.get("kitUsage", "<red>Usage: /kit [kit]</red>"));
            return true;
        }


        kitManager.kitGive(player, args[0]);

        return true;
    }

}
