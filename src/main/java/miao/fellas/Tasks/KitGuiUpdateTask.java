package miao.fellas.Tasks;

import miao.fellas.containers.KitContainer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class KitGuiUpdateTask extends BukkitRunnable {
    private final Player player;
    private final KitContainer kitContainer;
    private final Inventory inventory;

    public KitGuiUpdateTask(Player player, KitContainer kitContainer, Inventory inventory) {
        this.player = player;
        this.kitContainer = kitContainer;
        this.inventory = inventory;
    }

    @Override
    public void run() {
            if(!player.getOpenInventory().getTopInventory().equals(inventory)) {
                cancel();
                return;
            }
            kitContainer.updateKitContainer(player, inventory);
    }
}
