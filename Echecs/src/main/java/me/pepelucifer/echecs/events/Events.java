package me.pepelucifer.pluginechec.events;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage(ChatColor.GREEN+"Bonjour, essaie de rejoindre une partie d'échecs avec 'chess'!");
    }
}
