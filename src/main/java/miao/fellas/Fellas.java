package miao.fellas;

import miao.fellas.commands.KitCommand;
import miao.fellas.commands.KitReloadCommand;
import miao.fellas.commands.Kits;
import miao.fellas.commands.Send;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.tabs.KitTabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

import java.util.Objects;

public final class Fellas extends JavaPlugin {

    private boolean vaultEnabled;
    private Economy economy;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // Plugin startup logic
        getLogger().info("Plugin enabled");

        Plugin vaultPlugin = getServer().getPluginManager().getPlugin("Vault");
        if (vaultPlugin == null) {
            getLogger().warning("Vault not found");
            vaultEnabled = false;
        } else {


            getLogger().info("Vault found");

            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);

            if (economyProvider == null) {
                getLogger().warning("Economy not found");
                vaultEnabled = false;

            } else {
                economy = economyProvider.getProvider();
                vaultEnabled = true;
                getLogger().info("Economy found");
            }
        }


        Objects.requireNonNull(getCommand("message")).setExecutor(new Send(this));



        KitManager kitManager = new KitManager(this);
        KitTimeManager kitTimeManager = new KitTimeManager();


        Objects.requireNonNull(getCommand("kits")).setExecutor(new Kits(kitManager, kitTimeManager));

        Objects.requireNonNull(getCommand("kitreload")).setExecutor(new KitReloadCommand(kitManager, this));

        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand(kitManager, kitTimeManager, this));
        Objects.requireNonNull(getCommand("kit")).setTabCompleter(new KitTabCompleter(kitManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin disabled");
    }

    public boolean isVaultEnabled() {
        return vaultEnabled;
    }

    public Economy getEconomy() {
        return economy;
    }
}
