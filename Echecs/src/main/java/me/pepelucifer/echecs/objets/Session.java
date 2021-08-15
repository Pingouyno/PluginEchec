package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.chesslib.Board;
import org.bukkit.World;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.ArrayList;

public class Session{
    Lobby lobby;
    int id;
    ArrayList<LobbyPlayer> players;
    boolean started;
    Scoreboard board;
    World world;
    Board echiquier;
    public Boolean aQuiDeJouer;

    public Session(int id, LobbyPlayer p1, LobbyPlayer p2) {
        this.id = id;
        this.players = new ArrayList<LobbyPlayer>();
        players.add(p1);
        players.add(p2);
        this.started = true;
        this.lobby=Logique.lobby;
        this.echiquier = new Board();
        this.aQuiDeJouer = true;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerChat (AsyncPlayerChatEvent event){
        if (isInLobby(event.getPlayer())){
            event.setCancelled(true);
            LobbyPlayer lobbyPlayer = getLobbyPlayer(event.getPlayer());
            if(lobbyPlayer.isPlaying()){
                Session session = lobbyPlayer.getSession();
                String[] coup = event.getMessage().split(" ");
                if(session.aQuiDeJouer && player.isWhite){
                    System.out.println("Les blancs jouent :");
                    System.out.println("Départ du coup : " + coup[0]);
                    System.out.println("Arrivée du coup : " + coup[1]);
                }else if(!session.aQuiDeJouer && !player.isWhite){
                    System.out.println("Les noirs jouent :");
                    System.out.println("Départ du coup : " + coup[0]);
                    System.out.println("Arrivée du coup : " + coup[1]);
                }
            }
            

        }
    }

    public int getSessionId(){
        return id;
    }

    public World getWorld() {
        return world;
    }

    public Scoreboard getScoreBoard() {
        return board;
    }

    public int getPlayerCount(){
        return players.size();
    }

    public void setScoreBoard(Scoreboard scoreboard){
        this.board=scoreboard;
    }

    public Lobby getLobby(){
        return lobby;
    }

    public boolean isStarted(){
        return started;
    }

    public ArrayList<LobbyPlayer> getPlayers(){
        return players;
    }
}

