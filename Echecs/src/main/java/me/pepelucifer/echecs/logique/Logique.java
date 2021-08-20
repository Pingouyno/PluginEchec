package me.pepelucifer.echecs.logique;
import me.pepelucifer.echecs.chesslib.*;
import me.pepelucifer.echecs.chesslib.move.Move;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.objets.Lobby;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import me.pepelucifer.echecs.objets.Session;
import me.pepelucifer.echecs.sons.PlaySound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
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
    public static String pluginFolder;

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

    public LobbyPlayer getLobbyPlayer(HumanEntity player){
        return privateFindLobbyPlayer(player.getName());
    }

    public LobbyPlayer getLobbyPlayer(Player player){
        return privateFindLobbyPlayer(player.getName());
    }

    public LobbyPlayer getLobbyPlayer(String nomJoueur){
        return privateFindLobbyPlayer(nomJoueur);
    }

    private LobbyPlayer privateFindLobbyPlayer(String nomJoueur){                                            //Pour raison de propreté, ne pas appeler directement cette méthode
        for (LobbyPlayer joueur:lobby.getWaitingPlayers()){
            if (joueur.getName().equals(nomJoueur)){
                return joueur;
            }
        }
        for (LobbyPlayer joueur: lobby.getPlayingPlayers()){
            if (joueur.getName().equals(nomJoueur)){
                return joueur;
            }
        }
        return null;
    }


    public void broadcastConnectMessage(LobbyPlayer lobbyPlayer){
        String name=lobbyPlayer.getName();
        for (LobbyPlayer people:lobby.getWaitingPlayers()){
            if (people.getName().equals(name)){
                lobbyPlayer.getPlayer().sendMessage(ChatColor.GOLD+"\nBienvenue dans le salon d'échecs!");
            }else{
                people.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+name+" a §arejoint§d le salon");
            }
        }
    }


    public static void broadcastDisconnectMessage(LobbyPlayer lobbyPlayer){
        String name=lobbyPlayer.getName();
        if (lobbyPlayer.isPlaying()){
            for (LobbyPlayer people:lobbyPlayer.getSession().getPlayers()){
                if (people.getName().equals(name)){
                    if (!lobbyPlayer.getSession().isLocked()){
                        lobbyPlayer.getPlayer().sendMessage(ChatColor.GOLD+"\nVous avez quitté la partie.");
                    }
                }else{
                    people.getPlayer().sendMessage("\n"+ChatColor.GOLD+name+" a quitté la partie");
                }
            }
        }else{
            for (LobbyPlayer people:lobby.getWaitingPlayers()){
                if (!people.getName().equals(name)) {
                    people.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+name+" a §cquitté§d le salon");
                }
            }
        }
    }

    public void connectPlayer(Player player){
        player.teleport(lobbySpawn);
        //player.getInventory().clear();                                                                                            //REMETTRE AVANT MISE EN PROD!
        player.getInventory().setItem(8,ItemManager.porteQuitter);
        player.getInventory().setItem(0,ItemManager.chessMenuButton);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.getInventory().setHeldItemSlot(0);
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
        lobby.getWaitingPlayers().add(lobbyPlayer);
        broadcastConnectMessage(lobbyPlayer);
        for (LobbyPlayer people:lobby.getWaitingPlayers()){
            PlaySound.playVotingSound(people.getPlayer());
        }
    }

    public static void disconnectPlayer(LobbyPlayer lobbyPlayer){
        Player player=lobbyPlayer.getPlayer();
        //player.setFlying(false);                                                                                                   //REMETTRE CES LIGNES AVANT MISE EN PROD
        //player.setAllowFlight(false);
        player.teleport(worldSpawn);
        player.getInventory().removeItem(ItemManager.porteQuitter);
        player.getInventory().removeItem(ItemManager.chessMenuButton);
        player.closeInventory();
        broadcastDisconnectMessage(lobbyPlayer);
        if (lobbyPlayer.isPlaying()){
            lobby.getPlayingPlayers().remove(lobbyPlayer);
            lobbyPlayer.getSession().getPlayers().remove(lobbyPlayer);
        }else{
            lobby.getWaitingPlayers().remove(lobbyPlayer);
        }
        if (lobbyPlayer.isPlaying()){
            checkGameEnd(lobbyPlayer.getSession());
            if (lobbyPlayer.getSession().getPlayerCount()==0){
                lobby.endSession(lobbyPlayer.getSession());
            }
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
        if (session.isStarted() && !session.isLocked() && !session.isOver()){                                                                            //Changer éventuellement le fonctionnement
            if (isGameOver(session)){
                endGame(session);
            }
        }
    }


    public static String traduireVainqueur(LobbyPlayer lobbyPlayer){
        if (lobbyPlayer.isWhite()){
            return "blancs";
        }else{
            return "noirs";
        }
    }

    public static boolean isGameOver(Session session){
        Boolean isGameOver=false;
        Board echiq=session.getEchiquier();

        if (session.getPlayerCount()==1) {
            session.winType=1;
            session.setVainqueur(traduireVainqueur(session.getPlayers().get(0)));
            isGameOver=true;
        }

        else if (echiq.isMated()){
            session.winType=2;
            if (session.getEchiquier().getSideToMove().equals(Side.BLACK)){
                session.setVainqueur(traduireVainqueur(session.getWhite()));
            }else{
                session.setVainqueur(traduireVainqueur(session.getBlack()));
            }
            isGameOver=true;
        }

        else if (echiq.isStaleMate()){
            session.winType=3;
            session.setVainqueur("nulle");
            isGameOver=true;
        }


        else if (echiq.isInsufficientMaterial()){
            session.winType=4;
            session.setVainqueur("nulle");
            isGameOver=true;
        }

        else if (echiq.isRepetition()){
            session.winType=5;
            session.setVainqueur("nulle");
            isGameOver=true;
        }

        else if (echiq.isDraw()){                                                                                       //si aucune autre condition vérifiée, mais partie nulle, alors forcément une 50 coups
            session.winType=6;
            session.setVainqueur("nulle");
            isGameOver=true;
        }

        return isGameOver;
    }



    public static void broadcastEndGameMessage(Session session){
        String msg = getEndGameMessage(session);
        if (session.winType!=1){                                                                                        //PARCE QUE DÉCONNEXION EST UN CAS SPÉCIAL
            session.messageJoueurs(ChatColor.GOLD+"\n"+msg);
        }
    }


    public static String getEndGameMessage(Session session){
        String msg="";
        switch (session.getWinType()){
            case 1:                                                                                  //Déconnexion de l'adversaire
                msg="Par forfait";
                break;
            case 2:                                                                                  //Mat
                msg="Échec et mat.";
                break;
            case 3:                                                                                  //Pat
                msg="Pat";
                break;
            case 4:                                                                                  //Manque de matériel
                msg="Matériel insuffisant";
                break;
            case 5:                                                                                  //Répétition
                msg="Répétition";
                break;
            case 6:                                                                                  //50 coups
                msg="Règle des 50 coups";
                break;
            case 7:                                                                                  //Accord mutuel
                msg="Partie annulée par accord mutuel";
                break;
            case 8:                                                                                  //Abandon
                if (session.getVainqueur().equals("blancs")) {
                    msg="Les noirs abandonnent";
                }else if (session.getVainqueur().equals("noirs")) {
                    msg="Les blancs abandonnent";
                    break;
                }
        }
        return msg;
    }


    public static void broadcastVictoryMessage(Session session){
        String vainqueur = session.getVainqueur();
        String msg="";
        switch (vainqueur){
            case "nulle":
                msg=ChatColor.GOLD+"Partie nulle : aucun vainqueur.";
                break;
            case "blancs":
                msg=ChatColor.GOLD+"Victoire aux blancs!";
                break;
            case "noirs":
                msg=ChatColor.GOLD+"Victoire aux noirs!";
                break;
        }
        session.messageJoueurs(msg);
    }


    public static void broadcastVictorySound(Session session){
        String vainqueur = session.getVainqueur();
        String msg="";
        switch (vainqueur){
            case "nulle":
                for (LobbyPlayer people:session.getPlayers()){
                    PlaySound.playStartEmergencySound(session);
                }

                break;
            case "blancs":
                for (LobbyPlayer people:session.getPlayers()){
                    if (people.isWhite()){
                        PlaySound.playVictorySound(people.getPlayer());
                    }else{
                        PlaySound.playDefeatSound(people.getPlayer());
                    }
                }
                break;
            case "noirs":
                for (LobbyPlayer people:session.getPlayers()){
                    if (people.isWhite()){
                        PlaySound.playDefeatSound(people.getPlayer());
                    }else{
                        PlaySound.playVictorySound(people.getPlayer());
                    }
                }
                break;
        }
        session.messageJoueurs(msg);
    }


    public static void broadcastVictoryTitle(Session session){
        String vainqueur = session.getVainqueur();
        String titre="";
        String sousTitre=getEndGameMessage(session);
        switch (vainqueur){
            case "nulle":
                titre="§bNULLE";
                for (LobbyPlayer people:session.getPlayers()){
                    people.getPlayer().sendTitle(titre,sousTitre, 10, 80, 10);
                }
                break;
            case "blancs":
                for (LobbyPlayer people:session.getPlayers()){
                    if (people.isWhite()){
                        titre="§aVICTOIRE";
                        people.getPlayer().sendTitle(titre,sousTitre, 10, 80, 10);
                    }else{
                        titre="§4DÉFAITE";
                        people.getPlayer().sendTitle(titre,sousTitre, 10, 80, 10);
                    }
                }
                break;
            case "noirs":
                for (LobbyPlayer people:session.getPlayers()){
                    if (people.isWhite()){
                        titre="§4DÉFAITE";
                        people.getPlayer().sendTitle(titre,sousTitre, 10, 80, 10);
                    }else{
                        titre="§aVICTOIRE";
                        people.getPlayer().sendTitle(titre,sousTitre, 10, 80, 10);
                    }
                }
                break;
        }
    }


    public static void startGame(Session session){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
        session.getPlayers().get(randomNum).isWhite = true;
        for (LobbyPlayer lobbyPlayers: session.getPlayers()){
            Player joueur = lobbyPlayers.getPlayer();
            lobbyPlayers.setPlaying(true);
            joueur.teleport(getLocationDecalee(session.getSessionId(),sessionSpawn));
            String colorMessage="Vous avez les "+getColorMessage(lobbyPlayers);
            joueur.sendMessage(ChatColor.LIGHT_PURPLE+"\nLa partie est commencée!\n"+colorMessage);
            joueur.sendTitle(ChatColor.GOLD+"Partie commencée!", colorMessage, 10, 60, 20);
            joueur.closeInventory();
            joueur.getInventory().remove(ItemManager.chessMenuButton);
            joueur.getInventory().setHeldItemSlot(0);
            joueur.setAllowFlight(true);
            joueur.setFlying(true);
            for (LobbyPlayer people: session.getPlayers()){
                PlaySound.playStartGameSound(people.getPlayer());
            }
        }
        session.drawBoards();
    }


    public static void endGame(Session session){
        session.locked=true;
        session.over=true;
        broadcastEndGameMessage(session);
        for (LobbyPlayer people: session.getPlayers()){
            PlaySound.playTaskSound(people.getPlayer());
        }
        new BukkitRunnable() {
            int time = 2;
            public void run() {
                if (time == 0) {
                    broadcastVictorySound(session);
                    broadcastVictoryMessage(session);
                    broadcastVictoryTitle(session);
                    cancel();
                    return;
                }
                time--;
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Echecs"), 0L, 20L);
    }



    public static String getColorMessage(LobbyPlayer lobbyPlayer){
        if (lobbyPlayer.isWhite()){
            return (ChatColor.GOLD+"blancs");
        }else{
            return (ChatColor.GOLD+"noirs");
        }
    }


    public void checkJouerCoup(LobbyPlayer lobbyPlayer, String caseClique){
    Session session = lobbyPlayer.getSession();
        if (!session.isOver() && session.isTraitAuxBlancs() == (lobbyPlayer.isWhite())) {
            Board echiq=session.getEchiquier();
            Piece piece=echiq.getPiece(Square.valueOf(caseClique));
            if (session.getDernierClic()==null){
                if (!piece.equals(Piece.NONE) && piece.getPieceSide().equals(session.getEchiquier().getSideToMove())){
                    session.setDernierClic(caseClique);
                    session.redessinerCase(caseClique,true);
                }
            }else{
                Piece lastPiece=echiq.getPiece(Square.valueOf(session.getDernierClic()));
                if (piece.getPieceType()!=null && lastPiece.getPieceSide().equals(echiq.getSideToMove()) && piece.getPieceSide().equals(lastPiece.getPieceSide())) {
                    session.redessinerCase(session.getDernierClic(), false);
                    session.setDernierClic(caseClique);
                    session.redessinerCase(caseClique, true);
                }else{
                    session.jouer(session.getDernierClic(),caseClique);
                }
            }
        }
    }


    public static void setValeurCase(Block block, String valeurCase){
        block.setMetadata("case", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("Echecs"), valeurCase));
    }

    public static String getValeurCase(Block block){
        List<MetadataValue> metaDataValues = block.getMetadata("case");
        for (MetadataValue value : metaDataValues) {
            return value.asString();
        }
        return "null";
    }
}
