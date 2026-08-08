package simo.fellas.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import simo.fellas.managers.MessageManager;

public class HatCommand implements CommandExecutor {
    private final MessageManager messageManager;

    public HatCommand(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage(messageManager.get("onlyPlayer", "Only players may use this command"));
            return true;
        }

        if(!player.hasPermission("fellas.hat")) {
            sender.sendMessage(messageManager.get("noPermission", "<red>Insufficient permissions.</red> <dark_purple>({permission})</dark_purple>"));
            return true;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand().clone();

        if(heldItem.equals(new ItemStack(Material.AIR))) {
            return true;
        }

        ItemStack helmet = player.getInventory().getHelmet().clone();

        player.getInventory().setItemInMainHand(helmet);
        player.getEquipment().setHelmet(heldItem);
        player.sendMessage(messageManager.get("hat", "<green>Now you have an hat!</green>"));
        return true;
    }
}
