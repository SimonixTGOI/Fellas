package miao.fellas.commands;


import miao.fellas.constructor.Kit;
import miao.fellas.managers.EconomyManager;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.managers.MessageManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class KitCommand implements CommandExecutor {

    private final KitManager kitManager;
    private final KitTimeManager kitTimeManager;
    private final MessageManager messageManager;
    private final EconomyManager economyManager;

    public KitCommand(KitManager kitManager, KitTimeManager kitTimeManager, MessageManager messageManager, EconomyManager economyManager) {
        this.kitManager = kitManager;
        this.kitTimeManager = kitTimeManager;
        this.messageManager = messageManager;
        this.economyManager = economyManager;
    }




    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command , @NotNull String label, @NotNull String[] args) {


        if(!(sender instanceof Player player)) {
            sender.sendMessage(messageManager.get("onlyPlayer", "Only players may use this command"));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(messageManager.get(
                    "kitUsage",
                    "<red>Usage: /kit [kit]</red>"
            ));
            return true;
        }


        Kit kit = kitManager.getKit(args[0]);
        if(kit == null) {

            player.sendMessage(messageManager.get(
                    "kitNotFound",
                    "<red>This kit does not exist</red>"
            ));
            return true;
        }

        if(!(player.hasPermission(kit.getPermission()) || player.hasPermission("fellas.kit.allkits"))) {
            player.sendMessage(messageManager.get(
                    "noPermission",
                    "<red>Insufficient permissions.</red> <dark_purple>({permission})</dark_purple>",
                    "{permission}",
                    kit.getPermission())
            );
            return true;
        }


        int cooldown = kit.getCooldown();
        String key = kit.getKey();
        String name = kit.getName();
        UUID uuid = player.getUniqueId();

        if((kitTimeManager.isOnCooldown(uuid,key, cooldown)) && !player.hasPermission("fellas.kit.bypasscooldown")) {
            String remainingCooldown = String.valueOf(kitTimeManager.getRemainingTime(uuid, key, cooldown));
            player.sendMessage(messageManager.get("kitCooldown",
                    "<red>Kit still in cooldown, remaining time: </red><dark_purple>{remainingCooldown}s</dark_purple><red>.</red>",
                    "{remainingCooldown}",
                    remainingCooldown
            ));
            return true;
        }



        double price = kit.getPrice();
        Component successMessage;

        if(price == 0) {

            successMessage = messageManager.get(
                    "kitClaimed",
                    "<dark_purple>Kit {kitName} has been claimed!</dark_purple>",
                    "{kitName}",
                    name
                    );

        } else if(economyManager.isEnabled()) {

            if(!economyManager.hasEnoughMoney(player, price)) {
                player.sendMessage(messageManager.get(
                        "notEnoughMoney",
                        "<red>You don't have enough money to claim this kit</red>"
                ));
                return true;
            }

            if(!economyManager.withdrawPlayer(player, price)) {
                player.sendMessage(messageManager.get(
                        "transactionFailed",
                        "<red>Transaction failed.</red>"
                ));
                return true;
            }
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("{kitName}", name);
            placeholders.put("{balance}", String.valueOf(economyManager.getBalance(player)));
            successMessage = messageManager.get(
                    "kitPurchase",
                    "<dark_purple>Kit {kitName} has been purchased!</dark_purple> <yellow>New balance:</yellow> <green>€{balance}</green>",
                    placeholders
            );

        } else {
            player.sendMessage(messageManager.get(
                    "economyDisabled",
                    "<red>Economy is temporarily disabled. This paid kit cannot be claimed.</red>"
            ));
            return true;
        }

        kitTimeManager.setCooldown(uuid, key);
        kit.give(player);
        player.sendMessage(successMessage);





        return true;
    }


}
