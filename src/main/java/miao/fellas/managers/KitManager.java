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
        name = name.toLowerCase();

        int price = plugin.getConfig().getInt("kits."+name+".price");
        String permission = plugin.getConfig().getString("kits."+name+".permission");
        int cooldown = plugin.getConfig().getInt("kits."+name+".cooldown");

        kits.put(name, new Kit(
                name,
                price,
                permission,
                cooldown,
                loadItems("kits."+name+".items")));
    }


    public KitManager(JavaPlugin plugin) {
        this.kits = new HashMap<>();
        this.plugin = plugin;

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("kits");

        if(section == null){
            plugin.getLogger().warning("No kits in config.yml");
            return;
        }

        for(String name : section.getKeys(false)){
            loadKit(name);
        }

    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }
    public Set<String> getKitNames() {

        return kits.keySet();
    }
}
