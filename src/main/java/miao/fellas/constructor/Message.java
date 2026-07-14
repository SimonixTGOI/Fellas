package miao.fellas.constructor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Message {
    private final Plugin plugin;

    public Message(Plugin plugin) {
         this.plugin = plugin;
    }

    public void send(Player player) {
        String text = plugin.getConfig().getString("messaggio.text");

        if(text == null) {
            player.sendMessage("Non è presente un messaggio");
        }else{
            player.sendMessage(text);
        }


    }

}
