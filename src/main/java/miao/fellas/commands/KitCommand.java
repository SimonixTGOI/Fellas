package miao.fellas.commands;



import miao.fellas.managers.KitManager;
import miao.fellas.constructor.Kit;
import miao.fellas.managers.KitTimeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class KitCommand implements CommandExecutor {

    private final KitManager kitManager;
    private final KitTimeManager kitTimeManager;

    public KitCommand(KitManager kitManager, KitTimeManager kitTimeManager) {
        this.kitManager = kitManager;
        this.kitTimeManager = kitTimeManager;
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command , @NotNull String label, @NotNull String [] args) {


        if(!(sender instanceof Player player)) {
            sender.sendMessage("Solo i player possono eseguire questo comando");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage("usage: /kit [starter/pvp/archer]");
            return true;
        }


        Kit kit = kitManager.getKit(args[0]);
        if(kit == null) {
            player.sendMessage("Il kit non esiste.");
            return true;
        }

        if(!(player.hasPermission(kit.getPermission()))) {
            player.sendMessage("Permessi insufficienti. (" + kit.getPermission() + ")");
            return true;
        }


        int cooldown = kit.getCooldown();
        String name = kit.getName().toLowerCase();
        UUID uuid = player.getUniqueId();

        if(kitTimeManager.isOnCooldown(uuid,name, cooldown)) {
            int remainingTime = kitTimeManager.getRemainingTime(uuid, name, cooldown);
            player.sendMessage("Il kit è in cooldown, potrai riscattare di nuovo questo kit tra: " + remainingTime + ".");
            return true;
        }



        kitTimeManager.setCooldown(uuid, name);

        kit.give(player);







        return true;
    }


}
