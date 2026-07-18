package miao.fellas.constructor;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Kit {
    private final String name;
    private final Material material;
    private final double price;
    private final String permission;
    private final int cooldown;
    private final ItemStack[] items;


    public Kit(String name, Material material, double price, String permission,int cooldown, ItemStack... items) {
        this.name = name;
        this.material = material;
        this.price = price;
        this.permission = permission;
        this.cooldown = cooldown;
        this.items = items;

    }

    public String getKey() {
        return name.toLowerCase();
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public double getPrice() {
        return this.price;
    }

    public ItemStack[] getItems() {
        return this.items;
    }

    public String getPermission() {
        return this.permission;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void give(Player player) {
        for (ItemStack item : items) {
            player.getInventory().addItem(item.clone());
        }
    }



}