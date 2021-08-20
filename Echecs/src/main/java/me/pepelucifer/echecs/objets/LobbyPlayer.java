package me.pepelucifer.echecs.objets;
import org.bukkit.entity.Player;

public class LobbyPlayer{
    private Player player;
    private Session session;
    private boolean playing;
    public boolean isWhite;
    private int timeLeft;

    public LobbyPlayer(Player player) {
        this.player = (player);
        this.playing=false;
        this.isWhite=false;
        this.timeLeft=600;
    }

    public void setSession(Session session){this.session=session;}

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

    public void setPlaying(boolean isPlaying) {
        playing=isPlaying;
    }

    public String getName(){
        return player.getName();
    }

    public boolean isPlaying(){
        return playing;
    }

    public boolean isInSession() { return (getSession()!=null);}

    public Player getPlayer(){
        return player;
    }
}
