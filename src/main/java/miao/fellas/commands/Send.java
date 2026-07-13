package miao.fellas.commands;

import miao.fellas.constructor.Messaggio;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Send implements CommandExecutor { //richiede il public boolean
    @Override //dovrebbe tipo usare qualcosa che è già presente nella libreria di paper/bukkit
    public boolean onCommand(@NotNull /*non può essere null */CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("Solo i player possono eseguire questo comando");
            return true;
        }

        Messaggio m1 = new Messaggio("Ciao!");
        m1.send(player);

        return true;
    }
}
