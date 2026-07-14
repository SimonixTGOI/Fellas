package miao.fellas.commands;



import miao.fellas.Fellas;
import miao.fellas.managers.KitManager;
import miao.fellas.constructor.Kit;
import miao.fellas.managers.KitTimeManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class KitCommand implements CommandExecutor {

    private final KitManager kitManager;
    private final KitTimeManager kitTimeManager;
    private final Fellas plugin;

    public KitCommand(KitManager kitManager, KitTimeManager kitTimeManager, Fellas plugin) {
        this.kitManager = kitManager;
        this.kitTimeManager = kitTimeManager;
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command , @NotNull String label, @NotNull String [] args) {


        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage("usage: /kit [kit]");
            return true;
        }


        Kit kit = kitManager.getKit(args[0]);
        if(kit == null) {
            player.sendMessage("This kit does not exist");
            return true;
        }

        if(!(player.hasPermission(kit.getPermission()))) {
            player.sendMessage("Insufficient permissions. (" + kit.getPermission() + ")");
            return true;
        }


        int cooldown = kit.getCooldown();
        String name = kit.getName().toLowerCase();
        UUID uuid = player.getUniqueId();

        if((kitTimeManager.isOnCooldown(uuid,name, cooldown)) && !player.hasPermission("fellas.kit.bypasscooldown")) {
            int remainingTime = kitTimeManager.getRemainingTime(uuid, name, cooldown);
            player.sendMessage("Kit still in cooldown, remaining time: " + remainingTime + ".");
            return true;
        }



        int price = kit.getPrice();

        if(price != 0) {

            if(!plugin.isVaultEnabled()) {
                player.sendMessage("Economy is temporarily disabled. This kit Cannot be claimed");
                return true;
            }

            Economy economy = plugin.getEconomy();


            if(!economy.has(player, price)) {
                player.sendMessage("You don't have enough money to claim this kit");
                return true;
            }

            if(!economy.withdrawPlayer(player, price).transactionSuccess()) {
                player.sendMessage("Transaction failed.");
                return true;
            }

        }
        kitTimeManager.setCooldown(uuid, name);
        kit.give(player);


        return true;
    }


}
