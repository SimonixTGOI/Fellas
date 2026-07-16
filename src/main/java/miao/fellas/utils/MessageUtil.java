package miao.fellas.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MessageUtil {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component color(String message) {
        return MINI_MESSAGE.deserialize(message);
    }
}
