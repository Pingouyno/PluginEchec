package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.chesslib.Side;
import me.pepelucifer.echecs.chesslib.Square;
import me.pepelucifer.echecs.chesslib.move.Move;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.chesslib.Board;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.ArrayList;

public class Session{
    Lobby lobby;
    int id;
    ArrayList<LobbyPlayer> players;
    boolean started;
    World world;
    Board echiquier;
    Boolean auxBlancsAJouer;

    public Session(int id, LobbyPlayer p1, LobbyPlayer p2) {
        this.id = id;
        this.players = new ArrayList<LobbyPlayer>();
        players.add(p1);
        players.add(p2);
        this.started = true;
        this.lobby=Logique.lobby;
        this.echiquier = new Board();
        this.auxBlancsAJouer = true;
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

    public void inverserTrait(){
        setTraitAuxBlancs(!isTraitAuxBlancs());
    }

    public boolean isStarted(){
        return started;
    }

    public ArrayList<LobbyPlayer> getPlayers(){
        return players;
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
            messageJoueurs(ChatColor.RED+"Coup invalide!");
        }
        Bukkit.broadcastMessage("\n"+getEchiquier().toString());
    }

    public boolean isCoupLegal(Move move){
        return (getEchiquier().legalMoves().contains(move));
    }

    public void messageJoueurs(String msg){
        for (LobbyPlayer people:getPlayers()){
            people.getPlayer().sendMessage(msg);
        }
    }
}
