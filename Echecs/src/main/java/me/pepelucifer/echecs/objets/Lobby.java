package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.Echecs;
import me.pepelucifer.echecs.logique.Logique;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class Lobby{
    Location spawn;
    World world;
    int count;
    ArrayList<Session> sessionList;
    ArrayList<LobbyPlayer> waitingPlayerList;
    ArrayList<LobbyPlayer> playingPlayerList;

    public Lobby(){
        this.world= Logique.chessWorld;
        this.spawn=Logique.lobbySpawn;
        this.count=0;
        this.sessionList=new ArrayList<Session>();
        this.waitingPlayerList=new ArrayList<LobbyPlayer>();
        this.playingPlayerList=new ArrayList<LobbyPlayer>();
    }

    public ArrayList<Session> getSessions(){
        return sessionList;
    }

    public ArrayList<LobbyPlayer> getWaitingPlayers(){
        return waitingPlayerList;
    }

    public ArrayList<LobbyPlayer> getPlayingPlayers(){
        return playingPlayerList;
    }

    public World getWorld(){
        return world;
    }

    public Location getSpawnLocation(){
        return spawn;
    }

    public void startSession(){
        Session session = new Session(count, waitingPlayerList.get(0),waitingPlayerList.get(1));
        sessionList.add(session);
        for (LobbyPlayer joueurs: session.players){
            waitingPlayerList.remove(joueurs);
            playingPlayerList.add(joueurs);
        }
        count++;
        Logique.startGame(session);
    }

    public void endSession(Session session){
        Logique.endGame(session);
        for (LobbyPlayer player:session.getPlayers()){
            sessionList.remove(player);
        }
        getSessions().remove(this);
    }
}
