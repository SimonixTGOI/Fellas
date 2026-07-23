package simo.fellas.containers;

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
import simo.fellas.Tasks.KitGuiUpdateTask;
import simo.fellas.constructor.Kit;
import simo.fellas.managers.KitManager;
import simo.fellas.managers.KitTimeManager;
import simo.fellas.utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class KitContainer {

    private final Plugin plugin;
    private final KitManager kitManager;
    private final KitTimeManager kitTimeManager;
    private final NamespacedKey kitKey;
    private final NamespacedKey guiKey;
    private final NamespacedKey pageKey;

    private static final int KITS_PER_PAGE = 18;
    private static final int PREVIOUS_SLOT = 24;
    private static final int PAGE_SLOT = 25;
    private static final int NEXT_SLOT = 26;

    public KitContainer(Plugin plugin, KitManager kitManager, KitTimeManager kitTimeManager) {
        this.plugin = plugin;
        this.kitManager = kitManager;
        this.kitTimeManager = kitTimeManager;
        this.kitKey = new NamespacedKey(plugin, "kit_key");
        this.guiKey = new NamespacedKey(plugin, "gui_action");
        this.pageKey = new NamespacedKey(plugin, "page");
    }



    public void openKitContainer(Player player, int page) {

            try {
                Inventory inventory = Bukkit.createInventory(player, 27, Component.text("Kits"));
                String[] kits = kitManager.getKitNames().toArray(new String[0]);

                int slot = 0;
                int startIndex = page*KITS_PER_PAGE;
                int endIndex = Math.min(startIndex+KITS_PER_PAGE, kits.length);

                for(int i = startIndex; i < endIndex; i++) {
                    Kit kit = kitManager.getKit(kits[i]);
                    if(kit == null) {
                        player.sendMessage(MessageUtil.color("<red>Error opening the kit</red>"));
                        return;
                    }
                    ItemStack item = createKitItem(player, kit);
                    inventory.setItem(slot, item);
                    slot++;
                }

                if(page > 0) {
                    inventory.setItem(PREVIOUS_SLOT, createPageItem("Previous Page", "previous", page));
                }
                if(endIndex < kits.length) {
                    inventory.setItem(NEXT_SLOT, createPageItem("Next Page", "next", page));
                }
                inventory.setItem(PAGE_SLOT, createCurrentPageItem(page));

                player.openInventory(inventory);
                new KitGuiUpdateTask(player, this, inventory, page)
                        .runTaskTimer(plugin, 20L,20L);


            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning(e.getMessage());

            }

    }

    public void openKitContainer(Player player) {
        openKitContainer(player, 0);
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
                kitKey,
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

    public void updateKitContainer(Player player, Inventory inventory, int page) {
        String[] kits  = kitManager.getKitNames().toArray(new String[0]);
        int slot = 0;
        int startIndex = page*KITS_PER_PAGE;
        int endIndex = Math.min(startIndex+KITS_PER_PAGE,kits.length);

        for(int i = startIndex; i < endIndex; i++) {
            Kit kit = kitManager.getKit(kits[i]);

            if(kit == null) continue;

            inventory.setItem(slot, createKitItem(player, kit));
            slot++;
        }
    }

    private ItemStack createPageItem(String name, String action, int page) {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.customName(MessageUtil.color(name)
                .decoration(TextDecoration.ITALIC, false));
        meta.getPersistentDataContainer().set(guiKey, PersistentDataType.STRING, action);
        meta.getPersistentDataContainer().set(pageKey, PersistentDataType.INTEGER, page);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createCurrentPageItem(int page) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.customName(MessageUtil.color("Page: " + (page + 1))
                .decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
        return item;
    }
}
