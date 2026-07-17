package miao.fellas;

import miao.fellas.commands.KitCommand;
import miao.fellas.commands.KitReloadCommand;
import miao.fellas.commands.Kits;
import miao.fellas.commands.Send;
import miao.fellas.managers.EconomyManager;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.managers.MessageManager;
import miao.fellas.tabs.KitTabCompleter;
import miao.fellas.tabs.KitsTabCompleter;
import miao.fellas.tabs.SendTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Fellas extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // Plugin startup logic
        getLogger().info("Plugin is currently loading...");




        KitManager kitManager = new KitManager(this);
        KitTimeManager kitTimeManager = new KitTimeManager(this);
        EconomyManager economyManager = new EconomyManager(this);

        economyManager.setup();

        MessageManager messageManager = new MessageManager(this);
        getLogger().info("MessageManager loaded!");



        Objects.requireNonNull(getCommand("send")).setExecutor(new Send(messageManager));
        Objects.requireNonNull(getCommand("send")).setTabCompleter(new SendTabCompleter());






        Objects.requireNonNull(getCommand("kits")).setExecutor(new Kits(kitManager, kitTimeManager, messageManager));
        Objects.requireNonNull(getCommand("kits")).setTabCompleter(new KitsTabCompleter());

        Objects.requireNonNull(getCommand("kitreload")).setExecutor(new KitReloadCommand(kitManager, messageManager, this));

        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand(kitManager, kitTimeManager, messageManager, economyManager));
        Objects.requireNonNull(getCommand("kit")).setTabCompleter(new KitTabCompleter(kitManager));

        getLogger().info("Commands loaded!");

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin disabled");
    }
}
