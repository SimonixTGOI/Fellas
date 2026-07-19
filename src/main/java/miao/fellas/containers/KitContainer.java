package miao.fellas.containers;

import miao.fellas.Tasks.KitGuiUpdateTask;
import miao.fellas.constructor.Kit;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class KitContainer {

    private final Plugin plugin;
    private final KitManager kitManager;
    private final KitTimeManager kitTimeManager;
    private final NamespacedKey key;

    public KitContainer(Plugin plugin, KitManager kitManager, KitTimeManager kitTimeManager) {
        this.plugin = plugin;
        this.kitManager = kitManager;
        this.kitTimeManager = kitTimeManager;
        this.key = new NamespacedKey(plugin, "kit_key");
    }



    public void openKitContainer(Player player) {

            try {
                Inventory inventory = Bukkit.createInventory(player, 27, Component.text("Kits"));
                String[] kits = kitManager.getKitNames().toArray(new String[0]);

                for(int i = 0; i < kitManager.getKitCount(); i++) {
                    Kit kit = kitManager.getKit(kits[i]);
                    if(kit == null) {
                        player.sendMessage(MessageUtil.color("<red>Error opening the kit</red>"));
                        return;
                    }
                    ItemStack item = createKitItem(player, kit);
                    inventory.setItem(i, item);
                }

                player.openInventory(inventory);
                new KitGuiUpdateTask(player, this, inventory)
                        .runTaskTimer(plugin, 20L,20L);


            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning(e.getMessage());

            }

    }

    public void openKit(Player player, String key) {
        try {
            Inventory inventory = Bukkit.createInventory(player, 45, Component.text(key));

            Kit kit = kitManager.getKit(key);
            if(kit == null) {
                player.sendMessage(MessageUtil.color("<red>Error opening the kit</red>"));
                return;
            }
            ItemStack[] items = kit.getItems();
            if(items == null || items.length == 0) {
                player.sendMessage(MessageUtil.color("<red>Error opening the kit</red>"));
                return;
            }

            for(int i = 0; i < items.length; i++) {
                ItemStack item = items[i];
                inventory.setItem(i, item);
            }
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta meta = barrier.getItemMeta();
            meta.customName(MessageUtil.color("<red>Go back</red>")
                    .decoration(TextDecoration.ITALIC, false));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            barrier.setItemMeta(meta);
            inventory.setItem(36, barrier);
            player.openInventory(inventory);

        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }

    public ItemStack createKitItem(Player player, Kit kit) {
        ItemStack item = new ItemStack(kit.getMaterial());

        ItemMeta meta = item.getItemMeta();
        meta.customName(Component.text(kit.getName())
                .decoration(TextDecoration.ITALIC, false));

        meta.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                kit.getKey());

        List<Component> lore = new ArrayList<>();
        lore.add(MessageUtil.color("<yellow>Price:</yellow> <green>€" + kit.getPrice() + "</green>")
                .decoration(TextDecoration.ITALIC, false));


        int cooldown = kit.getCooldown();

        lore.add(MessageUtil.color("<yellow>Cooldown: " + cooldown + "s</yellow>")
                .decoration(TextDecoration.ITALIC, false));

        int remainingTime = kitTimeManager.getRemainingTime(player.getUniqueId(), kit.getKey(), cooldown);

        if(remainingTime > 0) {
            lore.add(MessageUtil.color("<yellow>Status:</yellow> <red>"+ remainingTime + "s</red>")
                    .decoration(TextDecoration.ITALIC, false));
            if(player.hasPermission("fellas.kit.bypasscooldown")) {
                lore.add(MessageUtil.color("<gray>You can claim it anyway.</gray>"));
            }
        } else {
            lore.add(MessageUtil.color("<yellow>Status:</yellow> <green>Ready.</green>")
                    .decoration(TextDecoration.ITALIC, false));
        }




        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }

    public void updateKitContainer(Player player, Inventory inventory) {
        String[] kits  = kitManager.getKitNames().toArray(new String[0]);
        for(int i = 0; i < kitManager.getKitCount(); i++) {
            Kit kit = kitManager.getKit(kits[i]);

            if(kit == null) continue;

            inventory.setItem(i, createKitItem(player, kit));
        }
    }
}
