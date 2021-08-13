package me.pepelucifer.echecs.logique;

import me.pepelucifer.echecs.objets.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Logique {

    public static Location lobbySpawn;
    public static World chessWorld;
    public static Lobby lobby;

    public static void init(){
        lobbySpawn = new Location(Bukkit.getWorlds().get(0),-210.5,91,-371.5);
        chessWorld = Bukkit.getWorlds().get(0);
        lobby = new Lobby();
    }


    //ÉCRIRE LES MÉTHODES CI-DESSOUS
    public void test(Player player){
        //Les nouvelles méthodes auront ce format. il faut s'assurer que toutes les autres classes ont "Extends Logique"
    }

}
