package me.pepelucifer.echecs;

import me.pepelucifer.echecs.commands.Commands;
import me.pepelucifer.pluginechec.events.Events;
import me.pepelucifer.pluginechec.items.ItemManager;
import me.pepelucifer.echecs.objets.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class Echecs extends JavaPlugin {

    public static Location lobbySpawn = new Location(Bukkit.getWorlds().get(0),-210.5,91,-371.5);
    public static World chessWorld = Bukkit.getWorlds().get(0);
    public static Lobby lobby = new Lobby();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(),this);
        this.getCommand("chess").setExecutor(new Commands());
        this.getCommand("quit").setExecutor(new Commands());
        ItemManager.init();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Plugin Echecs démarré.");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED+"PluginEchec arrêté.");
    }
}


