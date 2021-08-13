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
        return new Location(Logique.lobbySpawn.getWorld(),x_décalé,Logique.lobbySpawn.getY(),Logique.lobbySpawn.getZ());
    }

    public void checkCreateNewSession(){
        if (waitingPlayerList.size()>=2){
            createSession(waitingPlayerList.get(0),waitingPlayerList.get(1));
        }
    }

    public void checkValidConnect(Player player){
        if (!isInLobby(player)){
            connectPlayer(player);
        }else{
            player.sendMessage(ChatColor.RED+"Vous êtes déjà dans un salon d'échecs!");
        }
    }

    public void checkValidDisconnect(Player player){
        if (isInLobby(player)){
            disconnectPlayer(getLobbyPlayer(player));
        }else{
            player.sendMessage(ChatColor.RED+"Vous n'êtes pas dans un salon d'échecs!");
        }
    }

    public boolean isInLobby(Player player){
        /*
        if (player.getWorld().getName().equals(getWorld().getName())){                          //Ce code est plus performant, mais n'est pas fonctionnel avec le serveur test. On va l'intégrer dans M2C.
            return true;
        }else{
            return false;
        }
         */
        String name=player.getName();
        for (LobbyPlayer joueurs:getPlayingPlayers()){
            if (joueurs.getName().equals(name)){
                return true;
            }
        }
        for (LobbyPlayer joueurs:getWaitingPlayers()){
            if (joueurs.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public void connectPlayer(Player player){
        player.teleport(Logique.lobby.getSpawnLocation());
        player.sendMessage(ChatColor.LIGHT_PURPLE+"Bienvenue dans le salon d'échecs!");
        player.getInventory().addItem(me.pepelucifer.echecs.items.ItemManager.chessPiece);
        waitingPlayerList.add(new LobbyPlayer(player));
        checkCreateNewSession();
    }

    public void disconnectPlayer(LobbyPlayer lobbyPlayer){
        lobbyPlayer.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        lobbyPlayer.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"Vous avez quitté la partie");
        lobbyPlayer.getPlayer().getInventory().removeItem(me.pepelucifer.echecs.items.ItemManager.chessPiece);
        if (lobbyPlayer.isPlaying()){
            playingPlayerList.remove(lobbyPlayer);
            lobbyPlayer.getSession().players.remove(lobbyPlayer);
            lobbyPlayer.getSession().checkSessionEnd();

        }else{
            waitingPlayerList.remove(lobbyPlayer);
        }
    }
}
