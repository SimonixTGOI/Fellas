package miao.fellas.managers;

import miao.fellas.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class MessageManager {
    private final Plugin plugin;

    public MessageManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public Component get(String path, String defaultMessage) {
        String message = plugin.getConfig().getString("messages." + path, defaultMessage);
        return MessageUtil.color(message);
    }

    public Component get(String path, String defaultMessage, String placeholder, String value) {
        String message = plugin.getConfig().getString("messages." + path, defaultMessage);
        message = message.replace(placeholder, value);
        return MessageUtil.color(message);
    }

    public Component get(String path, String defaultMessage, Map<String, String> placeholders) {
        String message = plugin.getConfig().getString("messages." + path, defaultMessage);
        for (Map.Entry<String, String> keyValue : placeholders.entrySet()) {
            message = message.replace(keyValue.getKey(), keyValue.getValue());
        }
        return MessageUtil.color(message);
    }
}
