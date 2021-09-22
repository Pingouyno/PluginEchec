package me.pepelucifer.echecs.events;

import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class Events extends EventLogique implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (isInLobby(event.getWhoClicked())){
            checkItemStack(event.getCurrentItem(),getLobbyPlayer(event.getWhoClicked()));
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event){
        if (!event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())){
            if (isNameInLobby(event.getPlayer().getName())){
                disconnectPlayer(getLobbyPlayer(event.getPlayer()));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player=event.getPlayer();
        if (isInLobby(player)){
            event.setCancelled(true);
            LobbyPlayer lobbyPlayer = getLobbyPlayer(player);
            if (player.getItemInHand()!=null && player.getItemInHand().getItemMeta()!=null){
                checkItemStack(event.getItem(),lobbyPlayer);
            }else{
                if (lobbyPlayer.isPlaying()){
                    Block block=player.getTargetBlockExact(50);
                    if (block!=null){
                        String nomCase = getValeurCase(block);
                        if (!nomCase.equals("null")){
                            checkJouerCoup(lobbyPlayer,nomCase);
                        }
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerChat (AsyncPlayerChatEvent event){
        if (isInLobby(event.getPlayer())){
            LobbyPlayer lobbyPlayer = getLobbyPlayer(event.getPlayer());
            if(lobbyPlayer.isPlaying()){
                event.setCancelled(true);
                lobbyPlayer.getSession().messageJoueurs(event.getMessage());
            }
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
            if (event.getRightClicked() instanceof ItemFrame){
                LobbyPlayer lobbyPlayer = getLobbyPlayer(event.getPlayer());
                if (lobbyPlayer.isPlaying()){
                    checkJouerCoup(lobbyPlayer,event.getRightClicked().getCustomName());
                    new BukkitRunnable() {
                        ItemFrame cadre=(ItemFrame) event.getRightClicked();                                               //boucle pour provoquer l'envoi du paquet pour render l'image du cadre cliqué à l'adversaire
                        public void run() {
                            cadre.setItem(cadre.getItem());
                            cancel();
                            return;
                        }
                    }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Echecs"), 1L, 0L);
                }
            }
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
        event.getPlayer().teleport(Logique.worldSpawn);                                                         //#Cette ligne devrait être transférée dans le among us
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
            Player player=(Player) event.getDamager();
            if (isInLobby(player)){
                event.setCancelled(true);
                LobbyPlayer lobbyPlayer = getLobbyPlayer(player);
                if (lobbyPlayer.isPlaying()){
                    checkJouerCoup(lobbyPlayer,event.getEntity().getCustomName());
                }
            }
        }
    }
}
