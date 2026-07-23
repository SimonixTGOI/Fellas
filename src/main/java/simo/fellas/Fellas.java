package simo.fellas;

import simo.fellas.commands.*;
import simo.fellas.containers.KitContainer;
import simo.fellas.listeners.ClickListener;
import simo.fellas.managers.EconomyManager;
import simo.fellas.managers.KitManager;
import simo.fellas.managers.KitTimeManager;
import simo.fellas.managers.MessageManager;
import simo.fellas.tabs.KitTabCompleter;
import simo.fellas.tabs.KitsTabCompleter;
import simo.fellas.tabs.SendTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Fellas extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // Plugin startup logic
        getLogger().info("Plugin is currently loading...");

        MessageManager messageManager = new MessageManager(this);

        EconomyManager economyManager = new EconomyManager(this);
        KitTimeManager kitTimeManager = new KitTimeManager(this);
        KitManager kitManager = new KitManager(this, messageManager, kitTimeManager, economyManager);
        KitContainer kitContainer = new KitContainer(this, kitManager, kitTimeManager);
        ClickListener clickListener = new ClickListener(this, kitManager, kitContainer);

        economyManager.setup();


        getLogger().info("MessageManager loaded!");



        Objects.requireNonNull(getCommand("send")).setExecutor(new Send(messageManager));
        Objects.requireNonNull(getCommand("send")).setTabCompleter(new SendTabCompleter());
        Objects.requireNonNull(getCommand("ping")).setExecutor(new Ping(messageManager));






        Objects.requireNonNull(getCommand("kits")).setExecutor(new Kits(messageManager, kitContainer));
        Objects.requireNonNull(getCommand("kits")).setTabCompleter(new KitsTabCompleter());

        Objects.requireNonNull(getCommand("kitreload")).setExecutor(new KitReloadCommand(kitManager, messageManager, this));

        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand(kitManager, messageManager));
        Objects.requireNonNull(getCommand("kit")).setTabCompleter(new KitTabCompleter(kitManager));

        getLogger().info("Commands loaded!");

        getServer().getPluginManager().registerEvents(clickListener, this);

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin disabled");
    }
}
