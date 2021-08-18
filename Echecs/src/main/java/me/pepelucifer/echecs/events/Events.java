package me.pepelucifer.echecs.events;
import me.pepelucifer.echecs.Echecs;
import me.pepelucifer.echecs.custommaps.TraceurImage;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import me.pepelucifer.echecs.objets.Session;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Events extends EventLogique implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (isInLobby(event.getWhoClicked())){
            checkItemStack(event.getCurrentItem(),event.getWhoClicked());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if (isInLobby(event.getPlayer())){
            checkItemStack(event.getItem(),event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerChat (AsyncPlayerChatEvent event){
        if (isInLobby(event.getPlayer())){
            event.setCancelled(true);
            LobbyPlayer lobbyPlayer = getLobbyPlayer(event.getPlayer());
            if(lobbyPlayer.isPlaying()){
                Session session = lobbyPlayer.getSession();
                if(session.isTraitAuxBlancs() == (lobbyPlayer.isWhite())) {
                    String coup = event.getMessage();

                    new BukkitRunnable() {                                                //boucle pour rendre le reste du code synchrone, on va changer la manière de jouer les coups dans le futur donc ce n'est pas un problème
                        int time=1;
                        public void run() {
                            if (time==0){
                                session.jouer(coup);
                                cancel();
                                return;
                            }
                            time--;
                        }
                    }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Echecs"), 1L, 2L);
                }else{
                    event.getPlayer().sendMessage(ChatColor.RED+"Ce n'est pas votre tour!");
                }
            }
        }else if (Logique.isEnModeDeveloppement){
            String coup = event.getMessage();
            Logique.devJouerCoup(coup);
        }else{
            new BukkitRunnable() {                                                //boucle pour rendre le reste du code synchrone, oui oui je sais c'est DÉGUEULASSE
                int time=0;
                public void run() {
                    if (time==1){
                        event.getPlayer().getInventory().setItem(0,ItemManager.getCustomMap("test",false,false));
                        cancel();
                        return;
                    }
                    time++;
                }
            }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Echecs"), 1L, 20L);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (isInLobby(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (isInLobby(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLoseHunger(FoodLevelChangeEvent event) {
        if (isInLobby(event.getEntity())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isInLobby(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryItemDrop(InventoryDragEvent event){
        if (isInLobby(event.getWhoClicked())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isInLobby(event.getPlayer())){
            LobbyPlayer lobbyPlayer = getLobbyPlayer(event.getPlayer());
            disconnectPlayer(lobbyPlayer);
        }
    }


    @EventHandler
    public void onBlockBreak (BlockBreakEvent event){
        if (isInLobby(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player){
            if (isInLobby((Player) event.getEntity())){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            if (isInLobby((Player) event.getDamager())){
                event.setCancelled(true);
            }
        }
    }
}
