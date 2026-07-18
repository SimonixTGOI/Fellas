package miao.fellas.listeners;

import miao.fellas.containers.KitContainer;
import miao.fellas.managers.KitManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ClickListener implements Listener {
    private final KitManager kitManager;
    private final KitContainer kitContainer;


    public ClickListener(KitManager kitManager, KitContainer kitContainer) {
        this.kitManager = Objects.requireNonNull(kitManager);
        this.kitContainer = Objects.requireNonNull(kitContainer);
    }
    @EventHandler
    public void onKitSelection(InventoryClickEvent event) {

        if(!event.getView().title().equals(Component.text("Kits"))) {
            return;
        }

        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if(item == null || item.getType() == Material.AIR) {
            return;
        }

        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }
        Component component = item.getItemMeta().customName();
        if(component == null) return;
        String key = PlainTextComponentSerializer.plainText().serialize(component);

        if(event.isLeftClick()) {

            boolean success = kitManager.kitGive(player, key);

            if(success) {
                player.closeInventory();
            }
            return;
        }
        if(event.isRightClick()) {
            kitContainer.openKit(player, key);
        }


    }

    @EventHandler
    public void onKitClick(InventoryClickEvent event) {
        Component component = event.getView().title();
        String invName = PlainTextComponentSerializer.plainText().serialize(component).toLowerCase();

        if(!kitManager.getKitNames().contains(invName)) return;
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if(item == null || item.getType() == Material.AIR) return;

        if(item.getType() == Material.BARRIER) {

            Player player = (Player) event.getWhoClicked();
            kitContainer.openKitContainer(player);
        }
    }
}
