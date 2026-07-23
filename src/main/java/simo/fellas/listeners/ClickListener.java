package simo.fellas.listeners;

import org.bukkit.inventory.meta.ItemMeta;
import simo.fellas.containers.KitContainer;
import simo.fellas.managers.KitManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class ClickListener implements Listener {
    private final KitManager kitManager;
    private final KitContainer kitContainer;
    private final NamespacedKey kitKey;
    private final NamespacedKey guiKey;
    private final NamespacedKey pageKey;


    public ClickListener(Plugin plugin, KitManager kitManager, KitContainer kitContainer) {
        this.kitManager = Objects.requireNonNull(kitManager);
        this.kitContainer = Objects.requireNonNull(kitContainer);
        this.kitKey = new NamespacedKey(plugin, "kit_key");
        this.guiKey = new NamespacedKey(plugin, "gui_action");
        this.pageKey = new NamespacedKey(plugin, "page");
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        //Kit list
        if (event.getView().title().equals(Component.text("Kits"))) {

            event.setCancelled(true);



            if (item == null || item.getType() == Material.AIR) {
                return;
            }

            if (!item.hasItemMeta() || item.getItemMeta().customName() == null) {
                return;
            }
            ItemMeta meta = item.getItemMeta();
            String action = meta.getPersistentDataContainer().get(guiKey, PersistentDataType.STRING);
            if (action != null) {
                Integer pageInteger = meta.getPersistentDataContainer().get(pageKey, PersistentDataType.INTEGER);
                if(pageInteger == null) {
                    return;
                }
                int targetPage;
                int page = pageInteger;
                switch (action) {
                    case "next" -> {
                        targetPage = page + 1;
                        kitContainer.openKitContainer(player, targetPage);
                        return;
                    }
                    case "previous" -> {
                        targetPage = page - 1;
                        kitContainer.openKitContainer(player, targetPage);
                        return;
                    }
                    case "page" -> {
                        return;
                    }
                }
            }



            String key = meta.getPersistentDataContainer().get(kitKey,  PersistentDataType.STRING);
            if (key == null) return;
            if (event.isLeftClick()) {

                kitManager.kitGive(player, key);

                return;
            }
            if (event.isRightClick()) {
                kitContainer.openKit(player, key);
            }
            return;
        }


        //Kit Preview
        Component component = event.getView().title();
        String title = PlainTextComponentSerializer.plainText().serialize(component).toLowerCase();

        if (kitManager.getKitNames().contains(title)) {
            event.setCancelled(true);
            if (item == null || item.getType() == Material.AIR) return;

            if (item.getType() == Material.BARRIER) {

                kitContainer.openKitContainer(player);
            }
        }
    }

}
