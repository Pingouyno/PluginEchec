package me.pepelucifer.pluginechec.objets;

import me.pepelucifer.pluginechec.PluginEchec;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.ArrayList;

public class Lobby {
    Location spawn;
    World world;
    int count;
    ArrayList<Session> sessionList;
    ArrayList<LobbyPlayer> playerList;

    public Lobby(){
        this.world=PluginEchec.chessWorld;
        this.spawn= PluginEchec.lobbySpawn;
        this.count=0;
        this.sessionList=new ArrayList<Session>();
        this.playerList=new ArrayList<LobbyPlayer>();
    }

    public ArrayList<Session> getSessions(){
        return sessionList;
    }

    public ArrayList<LobbyPlayer> getWaitingPlayers(){
        return playerList;
    }

    public World getWorld(){
        return world;
    }

    public Location getSpawnLocation(){
        return spawn;
    }

    public void createSession(LobbyPlayer p1, LobbyPlayer p2){
        sessionList.add(new Session(count,p1,p2));
    }
}
