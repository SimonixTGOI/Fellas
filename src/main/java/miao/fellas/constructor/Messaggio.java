package miao.fellas.constructor;

import org.bukkit.entity.Player;

public class Messaggio {
    private final String testo; //dichiaro una variabile per la classe, final si usa quando quella variabile non andrà modificata

    public Messaggio(String testo) {
        this.testo = testo; //this serve a indicare la variabile di QUESTO oggetto, si usa quando hai più variabili con lo stesso nome all'interno della classe
    }

    public void send(Player player) {
        player.sendMessage(testo);
    }

}
