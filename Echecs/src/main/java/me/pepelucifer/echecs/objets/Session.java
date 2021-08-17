package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.chesslib.Side;
import me.pepelucifer.echecs.chesslib.Square;
import me.pepelucifer.echecs.chesslib.move.Move;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.chesslib.Board;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.map.MapCanvas;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.ArrayList;
import java.util.Random;

public class Session{
    Lobby lobby;
    int id;
    ArrayList<LobbyPlayer> players;
    boolean started;
    public boolean locked;
    World world;
    Board echiquier;
    Boolean auxBlancsAJouer;
    Location premiereCaseBlanc;
    Location premiereCaseNoir;

    public Session(int id, LobbyPlayer p1, LobbyPlayer p2) {
        this.id = id;
        this.players = new ArrayList<LobbyPlayer>();
        players.add(p1);
        players.add(p2);
        this.started = true;
        this.lobby=Logique.lobby;
        this.echiquier = new Board();
        this.auxBlancsAJouer = true;
        this.premiereCaseBlanc = Logique.getLocationDecalee(id,Logique.premiereCaseUniverselleBlanc);
        this.premiereCaseNoir = Logique.getLocationDecalee(id,Logique.premiereCaseUniverselleNoir);
        this.world=Logique.chessWorld;
        this.locked=false;
    }

    public boolean isTraitAuxBlancs(){
        return auxBlancsAJouer;
    }

    public void setTraitAuxBlancs(boolean trait){
        auxBlancsAJouer=trait;
    }

    public int getSessionId(){
        return id;
    }

    public World getWorld() {
        return world;
    }

    public Board getEchiquier(){
        return this.echiquier;
    }

    public int getPlayerCount(){
        return players.size();
    }

    public Lobby getLobby(){
        return lobby;
    }

    public boolean isLocked(){return locked;}

    public void inverserTrait(){
        setTraitAuxBlancs(!isTraitAuxBlancs());
    }

    public boolean isStarted(){
        return started;
    }

    public ArrayList<LobbyPlayer> getPlayers(){
        return players;
    }

    public boolean isCoupLegal(Move move){
        return (getEchiquier().legalMoves().contains(move));
    }

    public void messageJoueurs(String msg){
        for (LobbyPlayer people:getPlayers()){
            people.getPlayer().sendMessage(msg);
        }
    }

    public LobbyPlayer getPlayer(boolean getWhite){
        if (getWhite){
            return getWhite();
        }else{
            return getBlack();
        }
    }

    public LobbyPlayer getWhite(){
        if (getPlayers().get(0).isWhite()){
            return getPlayers().get(0);
        }else{
            return getPlayers().get(1);
        }
    }

    public LobbyPlayer getBlack(){
        if (!getPlayers().get(0).isWhite()){
            return getPlayers().get(0);
        }else{
            return getPlayers().get(1);
        }
    }

    public void jouer(String coup){
        Move move;
        if (isTraitAuxBlancs()){
            move = new Move(coup,Side.WHITE);
        }else{
            move = new Move(coup,Side.BLACK);
        }
        if (isCoupLegal(move)){
            echiquier.doMove(move);
            inverserTrait();
            if (isTraitAuxBlancs()){
                Bukkit.broadcastMessage("Les blancs jouent :");
            }else{
                Bukkit.broadcastMessage("Les noirs jouent :");
            }
            Bukkit.broadcastMessage("Depart du coup : " + move.getFrom());
            Bukkit.broadcastMessage("Arrivee du coup : " + move.getTo());
        }else{
            getPlayer(isTraitAuxBlancs()).getPlayer().sendMessage(ChatColor.RED+"Coup invalide!");
        }
        drawBoards();
    }

    public void drawBoards(){
        drawBoardFromPerspective(true);
        drawBoardFromPerspective(false);
    }

    public void decalerCase(Location caseCourante, int cpt){
        if (cpt%8==0){
            caseCourante.setY(caseCourante.getY()-1);
            caseCourante.setZ(caseCourante.getZ()-7);
        }else{
            caseCourante.setZ(caseCourante.getZ()+1);
        }
    }

    public void drawBoardFromPerspective(boolean asWhite){
        Location caseCourante;
        String[] echiq;
        if (asWhite){
            caseCourante=premiereCaseBlanc.clone();
            echiq=echiquier.toStringFromWhiteViewPoint().split("\n");
        }else{
            caseCourante=premiereCaseNoir.clone();
            echiq=echiquier.toStringFromBlackViewPoint().split("\n");
        }
        int cpt=0;
        char c;
        for (String str:echiq){
            while (cpt<8){
                c=str.charAt(cpt);
                Boolean isUpper=(Character.isUpperCase(c));
                String couleur;
                if (isUpper){
                    couleur="blanc";
                }else{
                    couleur="noir";
                }
                Boolean isPiece=true;
                String piece="";


                switch (Character.toLowerCase(c)) {
                    case 'p':
                        piece="Pion ";
                        break;
                    case 'r':
                        piece="Tour ";
                        break;
                    case 'n':
                        piece="Cavalier ";
                        break;
                    case 'b':
                        piece="Fou ";
                        break;
                    case 'q':
                        piece="Dame ";
                        break;
                    case 'k':
                        piece="Roi ";
                        break;
                    case '.':
                        isPiece=false;
                        break;
                }
                if (isPiece){
                    Block block=getWorld().getBlockAt(caseCourante);
                    block.setType(Material.OAK_WALL_SIGN);
                    Sign panneau = (Sign) block.getState();
                    panneau.setLine(1, piece+couleur);
                    org.bukkit.material.Sign matSign =  new org.bukkit.material.Sign(Material.OAK_WALL_SIGN);
                    matSign.setFacingDirection(BlockFace.WEST);
                    panneau.setData(matSign);
                    panneau.update(true);
                }else{
                    getWorld().getBlockAt(caseCourante).setType(Material.AIR);
                }
                cpt++;
                decalerCase(caseCourante,cpt);
            }
            cpt=0;
        }
    }
}
