package simo.fellas.managers;

import simo.fellas.Fellas;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;



public class EconomyManager {
    private Economy economy;
    private final Fellas plugin;
    private boolean enabled;

    public EconomyManager(Fellas plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        Plugin vaultPlugin = plugin.getServer().getPluginManager().getPlugin("Vault");
        if(vaultPlugin == null) {
            plugin.getLogger().warning("Vault not found.");
            enabled = false;
            return;
        }

        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if(economyProvider == null) {
            plugin.getLogger().warning("Economy not found.");
            enabled = false;
            return;
        }

        plugin.getLogger().info("Economy setup complete.");

        economy = economyProvider.getProvider();
        enabled = true;
    }



    public boolean isEnabled() {
        return enabled;
    }

    public boolean hasEnoughMoney(Player player, double amount) {
        return economy.has(player, amount);
    }

    public double getBalance(Player player) {
        return economy.getBalance(player);
    }

    public boolean withdrawPlayer(Player player, double amount) {
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

}
