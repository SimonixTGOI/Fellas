package miao.fellas.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitTimeManager {
    private final Map<UUID, Map<String,Long>> kitTimeMap;
    private final Plugin plugin;
    private final File cooldownFile;
    private final FileConfiguration cooldownConfig;


    public KitTimeManager(Plugin plugin) {
        this.kitTimeMap = new HashMap<>();
        this.plugin = plugin;
        this.cooldownFile = new File(plugin.getDataFolder(), "cooldowns.yml");

        setupFile();

        this.cooldownConfig = YamlConfiguration.loadConfiguration(cooldownFile);

        loadCooldowns();
    }

    public void setCooldown(UUID uuid, String name) {
        kitTimeMap.putIfAbsent(uuid, new HashMap<>());

        Map<String, Long> kitMap = kitTimeMap.get(uuid);
        kitMap.put(name, System.currentTimeMillis());

        saveCooldowns();
    }


    public boolean isOnCooldown(UUID uuid, String name, int cooldown) {
        Long current = System.currentTimeMillis();
        Map<String, Long> kitMap = kitTimeMap.get(uuid);

        if(kitMap == null) {
            return false;
        }

        Long time = kitMap.get(name);

        if(time == null) {
            return false;
        }

        return current - time < cooldown* 1000L;
    }

    public int getRemainingTime(UUID uuid, String name, int cooldown) {
        Long current = System.currentTimeMillis();

        Map<String, Long> kitMap = kitTimeMap.get(uuid);
        if(kitMap == null) {
            return 0;
        }
        Long time = kitMap.get(name);

        if(time == null) {
            return 0;
        }

        long difference = current-time;

        int result = Math.toIntExact((cooldown*1000L - difference)/1000);

        return Math.max(result, 0);

    }

    public void setupFile() {
        try {
            if(!plugin.getDataFolder().exists()) {
                boolean created = plugin.getDataFolder().mkdirs();
                if(!created) {
                    plugin.getLogger().warning("[KitTimeManager] Could not create the folder.");
                }
            }
            if(!cooldownFile.exists()) {
                boolean created = cooldownFile.createNewFile();
                if(!created) {
                    plugin.getLogger().warning("[KitTimeManager] Could not create the file.");
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Error creating cooldowns file: " + e.getMessage());
        }

    }

    public void loadCooldowns() {

        ConfigurationSection sectionUUID = cooldownConfig.getConfigurationSection("cooldowns");
        if(sectionUUID == null) {
            plugin.getLogger().warning("[KitTimeManager] No cooldowns found.");
            return;
        }

        ConfigurationSection sectionkits;

        Set<String> uuidStringSet = sectionUUID.getKeys(false);
        for(String uuidString : uuidStringSet) {
            sectionkits = sectionUUID.getConfigurationSection(uuidString);
            if(sectionkits == null) {
                continue;
            }
            Set<String> kitSet = sectionkits.getKeys(false);
            Map<String, Long> timeMapLoad = new HashMap<>();
            for(String kit : kitSet) {
                Long time = sectionkits.getLong(kit);
                timeMapLoad.put(kit, time);
            }
            try {
                UUID uuid = UUID.fromString(uuidString);
                kitTimeMap.put(uuid, timeMapLoad);
            } catch(IllegalArgumentException e) {
                plugin.getLogger().warning("[KitTimeManager] Error filling map at uuid: " + uuidString + ". Error: " + e.getMessage());
            }

        }



    }

    public void saveCooldowns() {
        cooldownConfig.set("cooldowns", null);

        for(Map.Entry<UUID, Map<String, Long>> kitEntry : kitTimeMap.entrySet()) {
            String uuid = kitEntry.getKey().toString();
            Map<String, Long> timeMap = kitEntry.getValue();
            for(Map.Entry<String, Long> timeEntry :  timeMap.entrySet() ) {
                String name = timeEntry.getKey();
                Long time = timeEntry.getValue();
                cooldownConfig.set("cooldowns." + uuid + "." + name, time);
            }

        }



        try {
            cooldownConfig.save(cooldownFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error saving cooldowns file: " + e.getMessage());
        }
    }



}
