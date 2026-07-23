package simo.fellas.tasks;

import simo.fellas.containers.KitContainer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class KitGuiUpdateTask extends BukkitRunnable {
    private final Player player;
    private final KitContainer kitContainer;
    private final Inventory inventory;
    private final int page;

    public KitGuiUpdateTask(Player player, KitContainer kitContainer, Inventory inventory, int page) {
        this.player = player;
        this.kitContainer = kitContainer;
        this.inventory = inventory;
        this.page = page;
    }

    @Override
    public void run() {
            if(!player.getOpenInventory().getTopInventory().equals(inventory)) {
                cancel();
                return;
            }
            kitContainer.updateKitContainer(player, inventory, page);
    }
}
