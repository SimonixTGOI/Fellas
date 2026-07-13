package miao.fellas.managers;

import miao.fellas.constructor.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class KitManager {
    private final Map<String, Kit> kits;

    public KitManager(JavaPlugin plugin) {
        this.kits = new HashMap<>();


        int starterPrice = plugin.getConfig().getInt("kits.starter.price");
        String starterPermission = plugin.getConfig().getString("kits.starter.permission");
        int starterCooldown = plugin.getConfig().getInt("kits.starter.cooldown");

        kits.put("starter", new Kit(
                "starter",
                starterPrice,
                starterPermission,
                starterCooldown,
                new ItemStack(Material.IRON_SWORD, 1),
                new ItemStack(Material.COOKED_BEEF, 8)

        ));

        int pvpPrice = plugin.getConfig().getInt("kits.pvp.price");
        String pvpPermission = plugin.getConfig().getString("kits.pvp.permission");
        int pvpCooldown = plugin.getConfig().getInt("kits.pvp.cooldown");

        kits.put("pvp", new Kit(
                "pvp",
                pvpPrice,
                pvpPermission,
                pvpCooldown,
                new ItemStack(Material.DIAMOND_SWORD, 1),
                new ItemStack(Material.GOLDEN_APPLE, 4),
                new ItemStack(Material.IRON_CHESTPLATE, 1)
        ));

        int archerPrice = plugin.getConfig().getInt("kits.archer.price");
        String archerPermission = plugin.getConfig().getString("kits.archer.permission");
        int archerCooldown = plugin.getConfig().getInt("kits.archer.cooldown");

        kits.put("archer", new Kit(
                "archer",
                archerPrice,
                archerPermission,
                archerCooldown,
                new ItemStack(Material.BOW, 1),
                new ItemStack(Material.LEATHER_LEGGINGS, 1),
                new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                new ItemStack(Material.GOLDEN_APPLE, 8)
        ));

    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }
    public Set<String> getKitNames() {

        return kits.keySet();
    }
}
