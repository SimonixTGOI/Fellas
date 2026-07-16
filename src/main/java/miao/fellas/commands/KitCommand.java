package miao.fellas.commands;



import miao.fellas.Fellas;
import miao.fellas.managers.KitManager;
import miao.fellas.constructor.Kit;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.managers.MessageManager;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
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
    private final Fellas plugin;

    public KitCommand(KitManager kitManager, KitTimeManager kitTimeManager, MessageManager messageManager, Fellas plugin) {
        this.kitManager = kitManager;
        this.kitTimeManager = kitTimeManager;
        this.messageManager = messageManager;
        this.plugin = plugin;
    }




    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command , @NotNull String label, @NotNull String[] args) {


        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command");
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

        if(!(player.hasPermission(kit.getPermission()))) {
            player.sendMessage(messageManager.get(
                    "insufficientPermissions",
                    "<red>Insufficient permissions.</red> <dark_purple>(" + kit.getPermission() + ")</dark_purple>",
                    "{permission}",
                    kit.getPermission())
            );
            return true;
        }


        int cooldown = kit.getCooldown();
        String name = kit.getName().toLowerCase();
        UUID uuid = player.getUniqueId();

        if((kitTimeManager.isOnCooldown(uuid,name, cooldown)) && !player.hasPermission("fellas.kit.bypasscooldown")) {
            String remainingCooldown = String.valueOf(kitTimeManager.getRemainingTime(uuid, name, cooldown));
            player.sendMessage(messageManager.get("kitCooldown",
                    "<red>Kit still in cooldown, remaining time: </red><dark_purple>" + remainingCooldown + "s</dark_purple><red>.</red>",
                    "{remainingCooldown}",
                    remainingCooldown
            ));
            return true;
        }



        int price = kit.getPrice();
        Component successMessage;

        if(price == 0) {

            successMessage = messageManager.get(
                    "kitClaimed",
                    "<dark_purple>Kit " + kit.getName() + " has been claimed!</dark_purple>",
                    "{kitName}",
                    kit.getName()
                    );

        } else if(plugin.isVaultEnabled()) {
            Economy economy = plugin.getEconomy();

            if(!economy.has(player, price)) {
                player.sendMessage(messageManager.get(
                        "notEnoughMoney",
                        "<red>You don't have enough money to claim this kit</red>"
                ));
                return true;
            }

            if(!economy.withdrawPlayer(player, price).transactionSuccess()) {
                player.sendMessage(messageManager.get(
                        "transactionFailed",
                        "<red>Transaction failed.</red>"
                ));
                return true;
            }
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("{kitName}", kit.getName());
            placeholders.put("{balance}", String.valueOf(economy.getBalance(player)));
            successMessage = messageManager.get(
                    "kitPurchase",
                    "<dark_purple>Kit " + kit.getName() + " has been purchased!</dark_purple> <yellow>New balance:</yellow> <green>€" + economy.getBalance(player) + "</green>",
                    placeholders
            );

        } else {
            player.sendMessage(messageManager.get(
                    "economyDisabled",
                    "<red>Economy is temporarily disabled. This paid kit cannot be claimed.</red>"
            ));
            return true;
        }

        kitTimeManager.setCooldown(uuid, name);
        kit.give(player);
        player.sendMessage(successMessage);





        return true;
    }


}
