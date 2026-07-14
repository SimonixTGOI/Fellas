package miao.fellas.commands;

import miao.fellas.constructor.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Send implements CommandExecutor { //richiede il public boolean
    final Plugin plugin;

    public Send(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override //dovrebbe tipo usare qualcosa che è già presente nella libreria di paper/bukkit
    public boolean onCommand(@NotNull /*non può essere null */CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("Solo i player possono eseguire questo comando");
            return true;
        }

        Message m1 = new Message(plugin);
        m1.send(player);

        return true;
    }
}
