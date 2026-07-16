package miao.fellas.commands;

import miao.fellas.constructor.Kit;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class Kits implements CommandExecutor {
    private final KitManager kitManager;
    private final KitTimeManager kitTimeManager;

    public Kits(KitManager kitManager, KitTimeManager kitTimeManager) {
        this.kitManager = kitManager;
        this.kitTimeManager = kitTimeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command");
            return true;
        }

        if(!sender.hasPermission("fellas.kits")) {
            player.sendMessage(MessageUtil.color("<red>Insufficient permissions.</red> <purple>(fellas.kits)</purple>"));
            return true;
        }

        Set<String> namesSet = kitManager.getKitNames();
        for(String kitName : namesSet) {
            kitName = kitName.toLowerCase().strip();

            Kit kit = kitManager.getKit(kitName);

            int cooldown = kit.getCooldown();
            int price = kit.getPrice();
            String permission = kit.getPermission();
            String message = "<blue>" + kitName + "</blue> <dark_purple>-</dark_purple> <yellow>price:</yellow> <green>€" + price + "</green> <dark_purple>-</dark_purple> <yellow>cooldown: </yellow>";


            if(kitTimeManager.isOnCooldown(player.getUniqueId(), kitName, cooldown)) {
                int remainingCooldown = kitTimeManager.getRemainingTime(player.getUniqueId(), kitName, cooldown);
                message = message + remainingCooldown + "<dark_purple>/</dark_purple><yellow>" + cooldown + "s.</yellow>";
            } else {
                message = message + "<green>ready.</green>";
            }

            if(player.hasPermission(permission) || player.hasPermission("fellas.kit.allkits")) {
                    player.sendMessage(MessageUtil.color(message));
            }


        }
        return true;
    }
}
