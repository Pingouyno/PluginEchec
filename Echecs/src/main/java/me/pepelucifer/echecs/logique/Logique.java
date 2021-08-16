package me.pepelucifer.echecs.logique;
import me.pepelucifer.echecs.chesslib.Board;
import me.pepelucifer.echecs.chesslib.Side;
import me.pepelucifer.echecs.chesslib.move.Move;
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

import java.util.concurrent.ThreadLocalRandom;

public class Logique {

    public static World chessWorld;
    public static World spawnWorld;
    public static Location lobbySpawn;
    public static Location worldSpawn;
    public static Location sessionSpawn;
    public static String chessWorldName;
    public static String spawnWorldName;

    public static Lobby lobby;
    public static int decalage;

    public static Location premiereCaseUniverselleBlanc;
    public static Location premiereCaseUniverselleNoir;
    public static boolean isEnModeDeveloppement;
    public static Board devBoard;
    public static boolean devBoardTraitAuxBlancs;

    public static void init(){
        chessWorld = Bukkit.getWorlds().get(0);
        spawnWorld = Bukkit.getWorlds().get(0);
        lobbySpawn = new Location(chessWorld,86.5,86.5,303);
        worldSpawn = spawnWorld.getSpawnLocation();
        sessionSpawn = new Location(chessWorld,101.5,86.5,303.5);
        spawnWorldName=spawnWorld.getName();
        chessWorldName=chessWorld.getName();
        isEnModeDeveloppement=false;
        premiereCaseUniverselleBlanc = new Location(chessWorld,106,94,294);
        premiereCaseUniverselleNoir = new Location(chessWorld,106,94,305);

        lobby = new Lobby();
        decalage = 14;

        devBoard=new Board();
        devBoardTraitAuxBlancs=true;
    }


    public static void devJouerCoup(String coup){
        Move move;

        if (devBoardTraitAuxBlancs){
            move = new Move(coup, Side.WHITE);
        }else{
            move = new Move(coup,Side.BLACK);
        }
        if (isCoupCompletementCalissementValide(move)){
            devBoard.doMove(move,false);
            devBoardTraitAuxBlancs=!devBoardTraitAuxBlancs;
            Bukkit.broadcastMessage(devBoard.toString());
        }

    }

    public static boolean isCoupCompletementCalissementValide(Move move){
        Bukkit.broadcastMessage("\n|\n|\n|\n|\n|\n|\n|\n|COUPS LÉGAUX : "+devBoard.legalMoves().toString()+
                "\n|");

        int cpt=0;
        if (devBoard.legalMoves().contains(move)){
            Bukkit.broadcastMessage("\n§aListe coups légaux contient votre coup");
            cpt++;
        }
        /*
        if (devBoard.isMoveLegal(move,true)){
            Bukkit.broadcastMessage("§aMéthode vérification contient votre coup");
            cpt++;
        }*/
        if (cpt==1){
            Bukkit.broadcastMessage("§aCoup complètement valide!");
            return true;
        }else{
            Bukkit.broadcastMessage("§4Coup invalide ; non joué dans le board virtuel");
        }
        return false;
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
        player.teleport(lobbySpawn);
        player.sendMessage(ChatColor.LIGHT_PURPLE+"Bienvenue dans le salon d'échecs!");
        player.getInventory().setItem(8,ItemManager.porteQuitter);
        player.getInventory().setItem(0,ItemManager.chessMenuButton);
        lobby.getWaitingPlayers().add(new LobbyPlayer(player));
        checkGameStart(lobby);
    }

    public static void disconnectPlayer(LobbyPlayer lobbyPlayer){
        lobbyPlayer.getPlayer().teleport(worldSpawn);
        lobbyPlayer.getPlayer().getInventory().removeItem(ItemManager.porteQuitter);
        lobbyPlayer.getPlayer().getInventory().removeItem(ItemManager.chessMenuButton);
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

    public static Location getLocationDecalee(int count, Location location){
        double x_decale=location.getX()+(decalage*count);
        return new Location(location.getWorld(),x_decale,location.getY(),location.getZ());
    }

    public void checkGameStart(Lobby lobby){                                                                                            //ON DOIT CHANGER CA POUR UN MATCHMAKING FONCTIONNEL
        if (lobby.getWaitingPlayers().size()>=2){
            //lobby.startSession();
        }
    }

    public static void checkGameEnd(Session session){
        if (session.isStarted() && !session.isLocked()){                                                                                       //Changer éventuellement le fonctionnement
            if (isGameOver(session)){
                lobby.endSession(session);
            }
        }
    }

    public static boolean isGameOver(Session session){
        if (session.getPlayerCount()==1  ||  session.getEchiquier().isDraw() || session.getEchiquier().isMated()){
            return true;
        }else{
            return false;
        }
    }


    public static void startGame(Session session){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
        session.getPlayers().get(randomNum).isWhite = true;
        for (LobbyPlayer lobbyPlayers: session.getPlayers()){
            Player joueur = lobbyPlayers.getPlayer();
            lobbyPlayers.isPlaying=true;
            joueur.teleport(getLocationDecalee(session.getSessionId(),sessionSpawn));
            joueur.sendMessage(ChatColor.LIGHT_PURPLE+"La partie est commencée!\n\n"+ChatColor.WHITE+"Vous avez les "+getColorMessage(lobbyPlayers));
            joueur.sendTitle("", ChatColor.GOLD+"Partie commencée!", 10, 60, 20);
            joueur.closeInventory();
            joueur.setHealth(20.0);
            joueur.setFoodLevel(20);
            joueur.getInventory().remove(ItemManager.chessMenuButton);
            joueur.getInventory().setHeldItemSlot(0);
            session.drawBoards();
        }
    }

    public static void endGame(Session session){
        session.locked=true;
        for (int i=0;i<session.getPlayerCount();i++){
            LobbyPlayer lobbyPlayer = session.getPlayers().get(0);
            lobbyPlayer.getPlayer().sendMessage(ChatColor.RED+"La partie est terminée.");
            disconnectPlayer(lobbyPlayer);
        }
    }


    public static String getColorMessage(LobbyPlayer lobbyPlayer){
        if (lobbyPlayer.isWhite()){
            return (ChatColor.GOLD+"blancs");
        }else{
            return (ChatColor.GOLD+"noirs");
        }
    }
}
