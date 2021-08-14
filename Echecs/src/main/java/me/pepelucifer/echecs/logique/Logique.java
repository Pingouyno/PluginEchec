package me.pepelucifer.echecs.logique;

import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.objets.Lobby;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import me.pepelucifer.echecs.objets.Session;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class Logique {

    public static Location lobbySpawn;
    public static World chessWorld;
    public static Lobby lobby;
    public static int decalage;

    public static void init(){
        lobbySpawn = new Location(Bukkit.getWorlds().get(0),-210.5,91,-371.5);
        chessWorld = Bukkit.getWorlds().get(0);
        lobby = new Lobby();
        decalage = 50;
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
        for (LobbyPlayer joueurs:lobby.getPlayingPlayers()){
            if (joueurs.getName().equals(name)){
                return true;
            }
        }
        for (LobbyPlayer joueurs:lobby.getWaitingPlayers()){
            if (joueurs.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isInLobby(HumanEntity player){
        /*
        if (player.getWorld().getName().equals(getWorld().getName())){                          //Ce code est plus performant, mais n'est pas fonctionnel avec le serveur test. On va l'intégrer dans M2C.
            return true;
        }else{
            return false;
        }
         */
        String name=player.getName();
        for (LobbyPlayer joueurs:lobby.getPlayingPlayers()){
            if (joueurs.getName().equals(name)){
                return true;
            }
        }
        for (LobbyPlayer joueurs:lobby.getWaitingPlayers()){
            if (joueurs.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public LobbyPlayer getLobbyPlayer(Player player){
        String nom=player.getName();
        for (LobbyPlayer joueur:lobby.getWaitingPlayers()){
            if (joueur.getName().equals(nom)){
                return joueur;
            }
        }
        for (LobbyPlayer joueur: lobby.getPlayingPlayers()){
            if (joueur.getName().equals(nom)){
                return joueur;
            }
        }
        return null;
    }

    public LobbyPlayer getLobbyPlayer(HumanEntity player){
        String nom=player.getName();
        for (LobbyPlayer joueur:lobby.getWaitingPlayers()){
            if (joueur.getName().equals(nom)){
                return joueur;
            }
        }
        for (LobbyPlayer joueur: lobby.getPlayingPlayers()){
            if (joueur.getName().equals(nom)){
                return joueur;
            }
        }
        return null;
    }

    public void connectPlayer(Player player){
        player.teleport(Logique.lobby.getSpawnLocation());
        player.sendMessage(ChatColor.LIGHT_PURPLE+"Bienvenue dans le salon d'échecs!");
        player.getInventory().setItem(8,ItemManager.porteQuitter);
        lobby.getWaitingPlayers().add(new LobbyPlayer(player));
        checkGameStart(lobby);
    }

    public static void disconnectPlayer(LobbyPlayer lobbyPlayer){
        lobbyPlayer.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        lobbyPlayer.getPlayer().getInventory().removeItem(ItemManager.porteQuitter);
        if (lobbyPlayer.isPlaying()){
            lobby.getPlayingPlayers().remove(lobbyPlayer);
            lobbyPlayer.getSession().getPlayers().remove(lobbyPlayer);
        }else{
            lobby.getWaitingPlayers().remove(lobbyPlayer);
        }
        if (lobbyPlayer.isPlaying()){
            checkGameEnd(lobbyPlayer.getSession());
        }
    }

    public static Location getLocationDecalee(int count){
        int x_decale=decalage*count;
        return new Location(Logique.lobbySpawn.getWorld(),x_decale,Logique.lobbySpawn.getY(),Logique.lobbySpawn.getZ());
    }

    public void checkGameStart(Lobby lobby){
        if (lobby.getWaitingPlayers().size()>=2){
            lobby.startSession();
        }
    }

    public static void checkGameEnd(Session session){
        if (session.isStarted() && session.getPlayerCount()==1){                                                                                       //Changer éventuellement le fonctionnement
            lobby.endSession(session);
        }
    }


    public static void startGame(Session session){
        for (LobbyPlayer lobbyPlayers: session.getPlayers()){
            Player joueur = lobbyPlayers.getPlayer();
            joueur.teleport(getLocationDecalee(session.getSessionId()));
            joueur.sendMessage(ChatColor.LIGHT_PURPLE+"La partie est commencée!");
            joueur.sendTitle("", ChatColor.GOLD+"Partie commencée!", 10, 60, 20);
            joueur.closeInventory();
            joueur.setHealth(20.0);
            joueur.setFoodLevel(20);
            joueur.getInventory().setHeldItemSlot(0);
        }
    }

    public static void endGame(Session session){
        for (LobbyPlayer player: session.getPlayers()){
            player.getPlayer().sendMessage(ChatColor.RED+"La partie est terminée.");
            disconnectPlayer(player);
        }
    }
}
