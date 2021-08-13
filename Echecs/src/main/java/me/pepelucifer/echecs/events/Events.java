package me.pepelucifer.echecs.events;
import me.pepelucifer.echecs.logique.Logique;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Events extends Logique implements Listener{

    @EventHandler
    public void onPlayerAttack(PlayerInteractEvent event){
        event.getPlayer().sendMessage(ChatColor.GREEN+"Évènement annulé");
    }
}
