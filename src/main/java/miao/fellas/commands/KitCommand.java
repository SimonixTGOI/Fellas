package miao.fellas.commands;



import miao.fellas.Fellas;
import miao.fellas.managers.KitManager;
import miao.fellas.constructor.Kit;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.utils.MessageUtil;
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command , @NotNull String label, @NotNull String[] args) {


        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(MessageUtil.color("<red>Usage: /kit [kit]</red>"));
            return true;
        }


        Kit kit = kitManager.getKit(args[0]);
        if(kit == null) {
            player.sendMessage(MessageUtil.color("<red>This kit does not exist</red>") );
            return true;
        }

        if(!(player.hasPermission(kit.getPermission()))) {
            player.sendMessage(MessageUtil.color("<red>Insufficient permissions.</red> <dark_purple>(" + kit.getPermission() + ")</dark_purple>"));
            return true;
        }


        int cooldown = kit.getCooldown();
        String name = kit.getName().toLowerCase();
        UUID uuid = player.getUniqueId();

        if((kitTimeManager.isOnCooldown(uuid,name, cooldown)) && !player.hasPermission("fellas.kit.bypasscooldown")) {
            int remainingTime = kitTimeManager.getRemainingTime(uuid, name, cooldown);
            player.sendMessage(MessageUtil.color("<red>Kit still in cooldown, remaining time: </dark_red><purple>" + remainingTime + "s</dark_purple><red>.</red>") );
            return true;
        }



        int price = kit.getPrice();
        Economy economy = plugin.getEconomy();

        if(price != 0) {

            if(!plugin.isVaultEnabled()) {
                player.sendMessage(MessageUtil.color("<red>Economy is temporarily disabled. This kit Cannot be claimed</red>"));
                return true;
            }




            if(!economy.has(player, price)) {
                player.sendMessage(MessageUtil.color("<red>You don't have enough money to claim this kit</red>"));
                return true;
            }

            if(!economy.withdrawPlayer(player, price).transactionSuccess()) {
                player.sendMessage(MessageUtil.color("<red>Transaction failed.</red>"));
                return true;
            }

        }
        kitTimeManager.setCooldown(uuid, name);
        kit.give(player);
        player.sendMessage(MessageUtil.color("<dark_purple>Kit " + kit.getName() + " has been purchased! New balance: <green>€" + economy.getBalance(player) + "</green>"));


        return true;
    }


}
