package miao.fellas.commands;

import miao.fellas.constructor.Kit;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Kits implements CommandExecutor {
    private final KitManager kitManager;
    private final KitTimeManager kitTimeManager;
    private final MessageManager messageManager;

    public Kits(KitManager kitManager, KitTimeManager kitTimeManager, MessageManager messageManager) {
        this.kitManager = kitManager;
        this.kitTimeManager = kitTimeManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command");
            return true;
        }

        if(!sender.hasPermission("fellas.kits")) {
            player.sendMessage(messageManager.get(
                    "insufficientPermission",
                    "<red>Insufficient permissions.</red> <purple>(fellas.kits)</purple>",
                    "{permission}",
                    "fellas.kits"
            ));
            return true;
        }

        Set<String> namesSet = kitManager.getKitNames();
        boolean atLeastOne = false;
        for(String kitName : namesSet) {
            kitName = kitName.toLowerCase().strip();

            Kit kit = kitManager.getKit(kitName);

            int cooldown = kit.getCooldown();
            int price = kit.getPrice();
            String permission = kit.getPermission();
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("{kitName}", kit.getName());
            placeholders.put("{price}", String.valueOf(price));


            if(player.hasPermission("fellas.kit.allkits") || player.hasPermission(permission)) {

                if(kitTimeManager.isOnCooldown(player.getUniqueId(), kitName, cooldown)) {
                    int remainingCooldown = kitTimeManager.getRemainingTime(player.getUniqueId(), kitName, cooldown);
                    placeholders.put("{remainingCooldown}", String.valueOf(remainingCooldown));
                    placeholders.put("{cooldown}", String.valueOf(cooldown));
                    player.sendMessage(messageManager.get("kitsWithCooldown", "<blue>" + kitName + "</blue> <dark_purple>-</dark_purple> <yellow>price:</yellow> <green>€" + price + "</green> <dark_purple>-</dark_purple> <yellow>cooldown: " + remainingCooldown + "</yellow><dark_purple>/</dark_purple><yellow>" + cooldown + "s.</yellow>", placeholders));
                } else {
                    player.sendMessage(messageManager.get("kitsWoutCooldown", "<blue>" + kitName + "</blue> <dark_purple>-</dark_purple> <yellow>price:</yellow> <green>€" + price + "</green> <dark_purple>-</dark_purple> <yellow>cooldown: </yellow>" + "<green>ready.</green>", placeholders));
                }
                atLeastOne = true;
            }





        }
        if(!atLeastOne) {
            player.sendMessage(messageManager.get("kitsNotOne", "<dark_purple>No kits available.</dark_purple>"));
        }
        return true;
    }
}
