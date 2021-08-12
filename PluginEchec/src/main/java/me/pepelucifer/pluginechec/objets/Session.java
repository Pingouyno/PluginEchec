package me.pepelucifer.pluginechec.objets;

import org.bukkit.World;
import org.bukkit.scoreboard.Scoreboard;

public class Session{
    int id;
    LobbyPlayer[] playerlist;
    boolean started;
    Scoreboard board;
    World world;

    public Session(int id, LobbyPlayer p1, LobbyPlayer p2) {
        this.id = id;
        this.playerlist = new LobbyPlayer[]{p1,p2};
        this.started = false;
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
        return playerlist.length;
    }

    public void setScoreBoard(Scoreboard scoreboard){
        this.board=scoreboard;
    }

    public void World(World world){
        this.world=world;
    }
}

