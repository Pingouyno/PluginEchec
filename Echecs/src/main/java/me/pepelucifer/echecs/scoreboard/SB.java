package me.pepelucifer.echecs.scoreboard;

import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.Lobby;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import me.pepelucifer.echecs.objets.Session;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SB extends Logique {

    public static Scoreboard newPlayerScoreBoard() {
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("test", "dummy", "hey");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD+"» §dÉchecs §a«");
        return board;
    }

    public static void redrawAllScoreBoard(Lobby lobby) {
        for (LobbyPlayer people:lobby.getWaitingPlayers()){
            redrawScoreBoard(people);
        }
    }

    public static void redrawAllScoreBoard(Session session) {
        for (LobbyPlayer people: session.getPlayers()){
            redrawScoreBoard(people);
        }
    }

    public static void redrawScoreBoard(LobbyPlayer lobbyPlayer){
        if (lobbyPlayer.isInSession()){
            drawGameSB(lobbyPlayer);
        }else{
            drawLobbySB(lobbyPlayer);
        }
    }


    public static void drawGameSB(LobbyPlayer lobbyPlayer) {
        Scoreboard board=lobbyPlayer.getOtherScoreBoard();
        Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
        Session session=lobbyPlayer.getSession();
        resetBoard(board);

        obj.getScore("Blancs -- "+getTicksToSecond(session.getWhite().getClockTime())).setScore(2);
        obj.getScore("Noirs -- "+getTicksToSecond(session.getBlack().getClockTime())).setScore(1);
        lobbyPlayer.getPlayer().setScoreboard(board);
    }

    public static void drawLobbySB(LobbyPlayer lobbyPlayer) {
        Scoreboard board = lobbyPlayer.getOtherScoreBoard();
        Objective obj= board.getObjective(DisplaySlot.SIDEBAR);
        Lobby lobby = Logique.lobby;
        resetBoard(board);

        obj.getScore("§1").setScore(3);
        obj.getScore("§3Nombres de joueurs : " + "[§a"+lobby.getWaitingPlayers().size()+ChatColor.WHITE+"§3]").setScore(2);
        obj.getScore("§3Parties en cours : " + "[§a"+lobby.getSessions().size()+ChatColor.WHITE+"§3]").setScore(1);
        lobbyPlayer.getPlayer().setScoreboard(board);
    }

    public static String getEntryVaryingColorCode(int step){                                                              //Parce que deux lignes ne doivent pas avoir exactement le même contenu
        return ("§"+step+" ");
    }

    public static void resetBoard(Scoreboard board) {
        for (String s : board.getEntries()) {
            board.resetScores(s);
        }
    }

    private static String getTicksToSecond(int tickValue){
        String paddedSecondes;
        int secondes = Math.round((tickValue%1200)/20);                                                                                  //20 ticks dans une seconde et 20*60 = 1200 ticks dans minute
        int minutes = (tickValue-(secondes*20))/1200;
        if (secondes<10){
            paddedSecondes="0"+secondes;
        }else{
            paddedSecondes=String.valueOf(secondes);
        }
        return minutes+":"+paddedSecondes;
    }
}

