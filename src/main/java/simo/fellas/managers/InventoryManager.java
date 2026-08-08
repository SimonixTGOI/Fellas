package simo.fellas.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

    public boolean canFitItems(Player player, ItemStack[] items) {
        ItemStack[] inventory = player.getInventory().getStorageContents().clone();
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null || inventory[i].getType() == Material.AIR) continue;
            ItemStack item = inventory[i].clone();
            inventory[i] = item;
        }

        for(ItemStack singleItem : items) {
            if (singleItem == null || singleItem.getType() == Material.AIR) continue;
            int maxStack = singleItem.getMaxStackSize();

            int quantityToAdd = singleItem.getAmount();

            for(ItemStack  slot : inventory) {
                if(quantityToAdd == 0) continue;
                if(slot == null || slot.getType() == Material.AIR) continue;
                if(singleItem.isSimilar(slot)) {
                    int amount = slot.getAmount();
                    if(amount < maxStack) {
                        int toAdd = Math.min(quantityToAdd, maxStack - amount);
                        quantityToAdd -= toAdd;
                        slot.setAmount(amount+toAdd);
                    }
                }
            }
            if(quantityToAdd != 0) {

                for(int i = 0; i < inventory.length && quantityToAdd != 0; i++) {
                    if(inventory[i] == null || inventory[i].getType() == Material.AIR) {
                        int toAdd = Math.min(quantityToAdd, maxStack);

                        ItemStack item = singleItem.clone();
                        item.setAmount(toAdd);
                        inventory[i] = item;
                        quantityToAdd -= toAdd;
                    }

                }
            }
            if(quantityToAdd > 0) {
                return false;
            }
        }

        return true;
    }
}
