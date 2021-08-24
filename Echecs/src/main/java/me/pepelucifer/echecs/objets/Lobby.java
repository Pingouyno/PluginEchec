package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.Echecs;
import me.pepelucifer.echecs.chesslib.Square;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.scoreboard.SB;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public final class Lobby{
    Location spawn;
    World world;
    int count;
    ArrayList<Session> sessionList;
    ArrayList<LobbyPlayer> waitingPlayerList;
    ArrayList<LobbyPlayer> playingPlayerList;
    UUID[] cadresUUIDs = new UUID[52];                                                                              //peut causer des erreur si on ajoute une pièce

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

    public int getTotalPlayerCount(){
        return (getPlayingPlayers().size()+getWaitingPlayers().size());
    }

    public World getWorld(){
        return world;
    }

    public Location getSpawnLocation(){
        return spawn;
    }

    private int trouverCompteurValeurBasse() {
        int pointeurSession = 0;
        if (getSessions().size() == 0) {
            return pointeurSession;
        } else {
            while (true) {                                                                                                       //Pas sécuritaire mais ne devrait jamais causer de problèmes
                for (Session session : getSessions()) {
                    if (session.getSessionId() == pointeurSession) {
                        pointeurSession++;
                        break;
                    }
                    return pointeurSession;
                }
            }
        }
    }

    public void checkStartSession(LobbyPlayer p1, LobbyPlayer p2){
        if (p1!=null && p1!=null){
            if (p1!=p2){
                if (!p1.isInSession() && !p1.isInSession()){
                    startSession(p1,p2);
                }
            }else{
                Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"ERREUR : Le jeu a tenté d'apparier un joueur avec soi-même.");
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
        SB.redrawAllScoreBoard(this);
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


    public void updeateDummyBoards(){
        for (UUID uuid:cadresUUIDs){
            ItemFrame cadre = (ItemFrame) Bukkit.getEntity(uuid);
            cadre.setItem(cadre.getItem());
        }
    }

    public void despawnDummyBoards(){
        for (UUID uuid:cadresUUIDs){
            Bukkit.getEntity(uuid).remove();
        }
    }


    public void spawnDummyBoardLater(){
        new BukkitRunnable() {
            int time = 1;
            public void run() {
                if (time == 0) {
                    spawnDummyBoards();
                    cancel();
                    return;
                }
                time--;
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Echecs"), 0L, 20L);
    }


    public void spawnDummyBoards(){
        ItemStack[][] allpieces = {ItemManager.whiteChessPiecesWhiteSquare,ItemManager.whiteChessPiecesBlackSquare,ItemManager.blackChessPiecesWhiteSquare,
                ItemManager.blackChessPiecesBlackSquare,ItemManager.whiteChessPiecesWhiteSquareYellow,ItemManager.whiteChessPiecesBlackSquareYellow,ItemManager.blackChessPiecesWhiteSquareYellow,
                ItemManager.blackChessPiecesBlackSquareYellow,ItemManager.emptySquares,ItemManager.emptySquaresYellow};
        Location caseCourante=Logique.premiereCaseDummyBoard.clone();
        int cpt = 0;
        for (ItemStack[] itemList:allpieces){
            for (ItemStack piece:itemList){
                spawnCadreCaseCourante(caseCourante,cpt,piece);
                cpt++;
                decalerDummyCadreNorth(caseCourante,cpt);
            }
        }
    }


    private void spawnCadreCaseCourante(Location caseCourante, int cpt, ItemStack piece){
        Block bloc = getWorld().getBlockAt(caseCourante.getBlockX(),caseCourante.getBlockY(),caseCourante.getBlockZ()-1);                           //NORTH
        //Block bloc = getWorld().getBlockAt(caseCourante.getBlockX()+1,caseCourante.getBlockY(),caseCourante.getBlockZ());                           //EAST
        //Block bloc = getWorld().getBlockAt(caseCourante.getBlockX()-1,caseCourante.getBlockY(),caseCourante.getBlockZ());                           //WEST
        //Block bloc = getWorld().getBlockAt(caseCourante.getBlockX(),caseCourante.getBlockY(),caseCourante.getBlockZ()+1);                           //SOUTH
        bloc.setType(Material.STONE);
        Entity cadre=getWorld().spawnEntity(caseCourante, EntityType.ITEM_FRAME);
        cadresUUIDs[cpt] = cadre.getUniqueId();
        ItemFrame frame = (ItemFrame) cadre;
        frame.setVisible(false);
        frame.setItem(piece);
        frame.setFacingDirection(BlockFace.SOUTH);                                                                                          //doit être inverse de la direction en haut
    }

    private void decalerDummyCadreWest(Location caseCourante, int cpt){
        if (cpt%13==0 && cpt!=0){
            caseCourante.add(0,-1,12);
        }else{
            caseCourante.add(0,0,-1);
        }
    }

    private void decalerDummyCadreNorth(Location caseCourante, int cpt){
        if (cpt%13==0 && cpt!=0){
            caseCourante.add(-12,-1,0);
        }else{
            caseCourante.add(1,0,0);
        }
    }
}
