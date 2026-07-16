package miao.fellas;

import miao.fellas.commands.KitCommand;
import miao.fellas.commands.KitReloadCommand;
import miao.fellas.commands.Kits;
import miao.fellas.commands.Send;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.managers.MessageManager;
import miao.fellas.tabs.KitTabCompleter;
import miao.fellas.tabs.SendTabCompleter;
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
        getLogger().info("Plugin is currently loading...");


        getLogger().info("Loading Vault...");

        Plugin vaultPlugin = getServer().getPluginManager().getPlugin("Vault");
        if (vaultPlugin == null) {
            getLogger().warning("Vault not found.");
            vaultEnabled = false;
        } else {


            getLogger().info("Vault found!");


            getLogger().info("Loading Economy...");

            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);

            if (economyProvider == null) {
                getLogger().warning("Economy not found.");
                vaultEnabled = false;

            } else {
                economy = economyProvider.getProvider();
                vaultEnabled = true;
                getLogger().info("Economy found!");
            }
        }

        KitManager kitManager = new KitManager(this);
        KitTimeManager kitTimeManager = new KitTimeManager();


        getLogger().info("Loading MessageManager...");
        MessageManager messageManager = new MessageManager(this);
        getLogger().info("MessageManager loaded!");


        getLogger().info("Loading Commands...");

        Objects.requireNonNull(getCommand("send")).setExecutor(new Send(messageManager));
        Objects.requireNonNull(getCommand("send")).setTabCompleter(new SendTabCompleter());






        Objects.requireNonNull(getCommand("kits")).setExecutor(new Kits(kitManager, kitTimeManager, messageManager));

        Objects.requireNonNull(getCommand("kitreload")).setExecutor(new KitReloadCommand(kitManager, messageManager, this));

        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand(kitManager, kitTimeManager, messageManager,this));
        Objects.requireNonNull(getCommand("kit")).setTabCompleter(new KitTabCompleter(kitManager));

        getLogger().info("Commands loaded!");

        getLogger().info("Plugin enabled!");
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
