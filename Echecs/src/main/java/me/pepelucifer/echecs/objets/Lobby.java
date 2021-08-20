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

    private int trouverCompteurValeurBasse(){
        int pointeurSession=0;
        if (getSessions().size()==0){
            return pointeurSession;
        }else{
            while (true){                                                                                                       //Pas sécuritaire mais ne devrait jamais causer de problèmes
                for (Session session:getSessions()){
                    if (session.getSessionId()==count){
                        pointeurSession++;
                        continue;
                    }
                    return pointeurSession;
                }
            }
        }
    }

    public void startSession(LobbyPlayer player1, LobbyPlayer player2){
        count=trouverCompteurValeurBasse();
        Session session = new Session(count, player1,player2);
        sessionList.add(session);
        for (LobbyPlayer joueurs: session.players){
            joueurs.setSession(session);
            waitingPlayerList.remove(joueurs);
            playingPlayerList.add(joueurs);
        }
        count++;
        Logique.startGame(session);
    }

    public void endSession(Session session){
        session.locked=true;
        session.over=true;
        int pCount=session.getPlayerCount();
        for (int i=0;i<pCount;i++){
            LobbyPlayer lobbyPlayer = session.getPlayers().get(0);
            Logique.disconnectPlayer(lobbyPlayer);
        }
        session.retirerCadres();
        getSessions().remove(session);
    }
}
