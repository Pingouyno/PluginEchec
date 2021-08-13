package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.Echecs;
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
        this.world= Echecs.chessWorld;
        this.spawn= Echecs.lobbySpawn;
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

    public LobbyPlayer getLobbyPlayer(Player player){
        String nom=player.getName();
        for (LobbyPlayer joueur:getPlayingPlayers()){
            if (joueur.getName().equals(nom)){
                return joueur;
            }
        }
        for (LobbyPlayer joueur:getWaitingPlayers()){
            if (joueur.getName().equals(nom)){
                return joueur;
            }
        }
        return null;
    }

    public void createSession(LobbyPlayer p1, LobbyPlayer p2){
        Session session = new Session(count,p1,p2);
        sessionList.add(session);
        for (LobbyPlayer joueurs: session.players){
            waitingPlayerList.remove(joueurs);
            playingPlayerList.add(joueurs);
            joueurs.sessionId=count;
            joueurs.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"La partie est commencée!");
            joueurs.getPlayer().sendTitle("", ChatColor.GOLD+"Partie commencée!", 10, 60, 20);
            joueurs.getPlayer().teleport(getLocationDécalée(count));
        }
        count++;

    }

    public Location getLocationDécalée(int count){
        int x_décalé=50*count;
        return new Location(Echecs.lobbySpawn.getWorld(),x_décalé,Echecs.lobbySpawn.getY(),Echecs.lobbySpawn.getZ());
    }

    public void checkCreateNewSession(){
        if (waitingPlayerList.size()>=2){
            createSession(waitingPlayerList.get(0),waitingPlayerList.get(1));
        }
    }

    public void connectPlayer(Player player){
        player.teleport(Echecs.lobby.getSpawnLocation());
        player.sendMessage("Vous avez rejoint un lobby d'échecs.");
        player.getInventory().addItem(me.pepelucifer.pluginechec.items.ItemManager.chessPiece);
        waitingPlayerList.add(new LobbyPlayer(player));
        checkCreateNewSession();
    }

    public void disconnectPlayer(LobbyPlayer lobbyPlayer){
        lobbyPlayer.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        lobbyPlayer.getPlayer().sendMessage("Vous avez quitté un lobby d'échecs.");
        lobbyPlayer.getPlayer().getInventory().removeItem(me.pepelucifer.pluginechec.items.ItemManager.chessPiece);
        if (lobbyPlayer.isPlaying()){
            playingPlayerList.remove(lobbyPlayer);
            lobbyPlayer.getSession().players.remove(lobbyPlayer);
            lobbyPlayer.getSession().checkSessionEnd();

        }else{
            waitingPlayerList.remove(lobbyPlayer);
        }
    }
}
