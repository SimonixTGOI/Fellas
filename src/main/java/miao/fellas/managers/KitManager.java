package miao.fellas.managers;

import miao.fellas.constructor.Kit;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class KitManager {
    private final Map<String, Kit> kits;
    private final JavaPlugin plugin;
    private final MessageManager messageManager;
    private final KitTimeManager kitTimeManager;
    private final EconomyManager economyManager;


    public void reloadKits() {
        kits.clear();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("kits");

        if(section == null) {
            plugin.getLogger().warning("No kits found in config.yml");
            return;
        }

        for(String name : section.getKeys(false)) {
            loadKit(name);
        }

        plugin.getLogger().info("Kits loaded.");
    }


    private ItemStack[] loadItems(String path) {
        List<String> itemsStringList = plugin.getConfig().getStringList(path);
        List<ItemStack> items = new ArrayList<>();
        for(String itemsString : itemsStringList){

            String[] parts = itemsString.split(":");
            if(parts.length != 2){
                plugin.getLogger().warning("Invalid items in kit: " + itemsString);
                continue;
            }
            Material material = Material.getMaterial(parts[0].trim().toUpperCase());

            if(material == null){
                plugin.getLogger().warning("Invalid material in kit: " + itemsString);
                continue;
            }

            try{
                int amount = Integer.parseInt(parts[1].trim());


                ItemStack item = new ItemStack(material, amount);
                items.add(item);

            } catch (NumberFormatException e){
                plugin.getLogger().warning("Invalid amount in kit: " +  itemsString);

            }

        }
        return items.toArray(new ItemStack[0]);
    }


    private void loadKit(String name) {
        String key = name.toLowerCase();
        String path = "kits." + name;
        try {
            if(plugin.getConfig().getString(path + ".material") == null) {
                plugin.getLogger().warning("Invalid material in kit: " + name);
                return;
            }
            Material material = Material.valueOf(plugin.getConfig().getString(path + ".material"));


            double price = plugin.getConfig().getInt(path + ".price");
            String permission = plugin.getConfig().getString(path + ".permission", "fellas.kit." + key);
            int cooldown = plugin.getConfig().getInt(path + ".cooldown");

            kits.put(key, new Kit(
                    name,
                    material,
                    price,
                    permission,
                    cooldown,
                    loadItems(path + ".items")));
        } catch (IllegalArgumentException e){
            plugin.getLogger().warning("Invalid material in kit: " + name);
        }
    }


    public KitManager(JavaPlugin plugin, MessageManager messageManager, KitTimeManager kitTimeManager, EconomyManager economyManager) {
        this.kits = new HashMap<>();
        this.plugin = plugin;
        this.messageManager = messageManager;
        this.kitTimeManager = kitTimeManager;
        this.economyManager = economyManager;

        reloadKits();

    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }
    public Set<String> getKitNames() {

        return kits.keySet();
    }

    public int getKitCount() {
        return kits.size();
    }

    public boolean kitGive(Player player, String key) {
        key = key.toLowerCase();
        Kit kit =  kits.get(key);

        if(kit == null) {

            player.sendMessage(messageManager.get(
                    "kitNotFound",
                    "<red>This kit does not exist</red>"
            ));
            return false;
        }

        if(!(player.hasPermission(kit.getPermission()) || player.hasPermission("fellas.kit.allkits"))) {
            player.sendMessage(messageManager.get(
                    "noPermission",
                    "<red>Insufficient permissions.</red> <dark_purple>({permission})</dark_purple>",
                    "{permission}",
                    kit.getPermission())
            );
            return false;
        }


        int cooldown = kit.getCooldown();
        String name = kit.getName();
        UUID uuid = player.getUniqueId();

        if((kitTimeManager.isOnCooldown(uuid,key, cooldown)) && !player.hasPermission("fellas.kit.bypasscooldown")) {
            String remainingCooldown = String.valueOf(kitTimeManager.getRemainingTime(uuid, key, cooldown));
            player.sendMessage(messageManager.get("kitCooldown",
                    "<red>Kit still in cooldown, remaining time: </red><dark_purple>{remainingCooldown}s</dark_purple><red>.</red>",
                    "{remainingCooldown}",
                    remainingCooldown
            ));
            return false;
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
                return false;
            }

            if(!economyManager.withdrawPlayer(player, price)) {
                player.sendMessage(messageManager.get(
                        "transactionFailed",
                        "<red>Transaction failed.</red>"
                ));
                return false;
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
            return false;
        }

        kitTimeManager.setCooldown(uuid, key);
        kit.give(player);
        player.sendMessage(successMessage);

        return true;
    }
}

