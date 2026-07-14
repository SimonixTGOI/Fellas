package miao.fellas;

import miao.fellas.commands.KitCommand;
import miao.fellas.commands.KitReloadCommand;
import miao.fellas.commands.Send;
import miao.fellas.managers.KitManager;
import miao.fellas.managers.KitTimeManager;
import miao.fellas.tabs.KitTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Fellas extends JavaPlugin {

    @Override //override serve a riscrivere o implementare un metodo già presente nella classe padre (JavaPlugin o CommandexEcutor)
    public void onEnable() {

        saveDefaultConfig();

        // Plugin startup logic
        getLogger().info("Plugin enabled");


        //il coso requireNonNull abbastanza easy semplicemente non deve essere null sennò ferma tutto prima ancora di iniziare

        Objects.requireNonNull(getCommand("message")).setExecutor(new Send(this));


        KitManager kitManager = new KitManager(this);
        KitTimeManager kitTimeManager = new KitTimeManager();

        Objects.requireNonNull(getCommand("kitreload")).setExecutor(new KitReloadCommand(kitManager, this));

        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand(kitManager, kitTimeManager));
        Objects.requireNonNull(getCommand("kit")).setTabCompleter(new KitTabCompleter(kitManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin disabled");
    }
}
