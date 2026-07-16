package miao.fellas.commands;

import miao.fellas.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Send implements CommandExecutor {
    private final MessageManager messageManager;

    public Send(MessageManager messageManager) {
        this.messageManager = messageManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players may use this command");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(messageManager.get("sendTargetMissing", "<red>Target player missing.</red>"));
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if(target == null) {
             sender.sendMessage(messageManager.get("sendTargetNotOnline", "<red>Player not online.</red>", "{player}", args[0]));
             return true;
        }

        if(args.length == 1) {
            sender.sendMessage((messageManager.get("sendMessageMissing", "<red>Message missing.</red>")));
            return true;
        }
        String message = "";
        for(int i = 1; i<args.length; i++) {

            message = message.concat(args[i]);
        }
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{sender}", sender.getName());
        placeholders.put("{message}", message);
        target.sendMessage(messageManager.get("sendMessageSent", "<blue>"+sender.getName()+"</blue> <dark_purple>sent you a message: </dark_purple>" + message, placeholders));
        return true;
    }
}
