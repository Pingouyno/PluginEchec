package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.logique.Logique;
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
        this.started = true;
        this.lobby=Logique.lobby;
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

    public Lobby getLobby(){
        return lobby;
    }

    public boolean isStarted(){
        return started;
    }

    public ArrayList<LobbyPlayer> getPlayers(){
        return players;
    }
}

