package me.pepelucifer.echecs.objets;
import org.bukkit.entity.Player;

public class LobbyPlayer{
    Player player;
    Session session;
    boolean isPlaying;
    boolean isWhite;
    int timeLeft;
    int sessionId;

    public LobbyPlayer(Player player) {
        this.player = (player);
        this.isPlaying=false;
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

    public int getSessionId(){
        return sessionId;
    }

    public String getName(){
        return player.getName();
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public Player getPlayer(){
        return player;
    }
}
