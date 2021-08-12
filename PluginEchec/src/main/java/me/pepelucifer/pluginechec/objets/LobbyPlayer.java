package me.pepelucifer.pluginechec.objets;
import org.bukkit.entity.Player;

public class LobbyPlayer{
    Player player;
    Session session;
    boolean isWhite;
    int timeLeft;

    public LobbyPlayer(Player player) {
        this.player = (player);
    }

    public Session getSession(){
        return session;
    }

    public boolean isWhite(){
        return isWhite;
    }

    public int getClockTime(){
        return timeLeft;
    }
}
