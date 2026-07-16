package miao.fellas.managers;

import miao.fellas.constructor.Kit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class KitManager {
    private final Map<String, Kit> kits;
    private final JavaPlugin plugin;

    public void reloadKits() {
        plugin.getLogger().info("Loading kits...");
        kits.clear();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("kits");

        if(section == null) {
            plugin.getLogger().warning("No kits found in config.yml");
            return;
        }

        for(String name : section.getKeys(false)) {
            loadKit(name.toLowerCase());
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

        int price = plugin.getConfig().getInt(path + ".price");
        String permission = plugin.getConfig().getString(path + ".permission", "fellas.kit." + key);
        int cooldown = plugin.getConfig().getInt(path + ".cooldown");

        kits.put(key, new Kit(
                key,
                price,
                permission,
                cooldown,
                loadItems(path + ".items")));
    }


    public KitManager(JavaPlugin plugin) {
        this.kits = new HashMap<>();
        this.plugin = plugin;

        reloadKits();

    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }
    public Set<String> getKitNames() {

        return kits.keySet();
    }
}
