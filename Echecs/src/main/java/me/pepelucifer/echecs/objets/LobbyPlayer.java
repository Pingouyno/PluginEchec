package me.pepelucifer.echecs.objets;
import org.bukkit.entity.Player;

public class LobbyPlayer{
    Player player;
    Session session;
    boolean isPlaying;
    public boolean isWhite;
    int timeLeft;

    public LobbyPlayer(Player player) {
        this.player = (player);
        this.isPlaying=false;
        this.isWhite=false;
        this.timeLeft=600;
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
        return getSession().getSessionId();
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
