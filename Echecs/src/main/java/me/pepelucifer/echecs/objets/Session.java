package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.Echecs;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class Session{
    Lobby lobby;
    int id;
    ArrayList<LobbyPlayer> players;
    boolean started;
    Scoreboard board;
    World world;

    public Session(int id, LobbyPlayer p1, LobbyPlayer p2) {
        this.id = id;
        this.players = new ArrayList<LobbyPlayer>();
        players.add(p1);
        players.add(p2);
        this.started = false;
        this.lobby= Echecs.lobby;
    }

    public int getSessionId(){
        return id;
    }

    public World getWorld() {
        return world;
    }

    public Scoreboard getScoreBoard() {
        return board;
    }

    public int getPlayerCount(){
        return players.size();
    }

    public void setScoreBoard(Scoreboard scoreboard){
        this.board=scoreboard;
    }

    public void getWorld(World world){
        this.world=world;
    }

    public Lobby getLobby(){
        return lobby;
    }

    public void endSession(){
        for (LobbyPlayer player:players){
            players.remove(player);
            getLobby().disconnectPlayer(player);
            player.getPlayer().sendMessage(ChatColor.RED+"La partie est terminée.");
        }
        getLobby().getSessions().remove(this);
    }

    public void checkSessionEnd(){
        if (players.size()!=2){
           endSession();
        }
    }
}

