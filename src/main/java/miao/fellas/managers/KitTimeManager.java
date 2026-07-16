package miao.fellas.managers;

import java.util.*;

public class KitTimeManager {
    private final Map<UUID, Map<String,Long>> timeMap;


    public KitTimeManager() {
        this.timeMap = new HashMap<>();
    }

    public void setCooldown(UUID uuid, String name) {
        timeMap.putIfAbsent(uuid, new HashMap<>());

        Map<String, Long> kitMap = timeMap.get(uuid);
        kitMap.put(name, System.currentTimeMillis());
    }


    public boolean isOnCooldown(UUID uuid, String name, int cooldown) {
        Long current = System.currentTimeMillis();
        Map<String, Long> kitMap = timeMap.get(uuid);

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

        Map<String, Long> kitMap = timeMap.get(uuid);
        if(kitMap == null) {
            return 0;
        }
        Long time = kitMap.get(name);

        if(time == null) {
            return 0;
        }

        long difference = current-time;


        return Math.toIntExact((cooldown*1000L - difference)/1000);
    }


}
