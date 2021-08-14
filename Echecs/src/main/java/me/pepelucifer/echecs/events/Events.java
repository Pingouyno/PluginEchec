package me.pepelucifer.echecs.events;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
            if (isInLobby((Player) event.getRemover())){
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player){
            if (isInLobby((Player) event.getRemover())){
                event.setCancelled(false);
            }
        }
    }


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            if (isInLobby((Player) event.getDamager())){
                event.setCancelled(false);
            }
        }
    }
}
