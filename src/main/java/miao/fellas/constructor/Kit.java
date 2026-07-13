package miao.fellas.constructor;


import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Kit {
    private final String name;
    private final int price;
    private final String permission;
    private final int cooldown;
    private final ItemStack[] items;


    public Kit(String name, int price, String permission,int cooldown, ItemStack... items) {
        this.name = name;
        this.price = price;
        this.permission = permission;
        this.cooldown = cooldown;
        this.items = items;

    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
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
        player.sendMessage("Kit " + name + " riscattato, prezzo: " + price + ".");
    }



}