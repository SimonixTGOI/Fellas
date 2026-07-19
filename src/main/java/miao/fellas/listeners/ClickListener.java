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
            Component component = item.getItemMeta().customName();
            if (component == null) return;
            String key = PlainTextComponentSerializer.plainText().serialize(component).toLowerCase();

            if (event.isLeftClick()) {

                boolean success = kitManager.kitGive(player, key);

                if (success) {
                    player.closeInventory();
                }
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
