package me.pepelucifer.echecs.events;
import me.pepelucifer.echecs.chesslib.move.Move;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import me.pepelucifer.echecs.objets.Session;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
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


                    //session.jouer(coup);
                }else{
                    event.getPlayer().sendMessage(ChatColor.RED+"Ce n'est pas votre tour!");
                }
            }
        }else if (Logique.isEnModeDeveloppement){
            String coup = event.getMessage();
            Logique.devJouerCoup(coup);
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
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            if (isInLobby((Player) event.getDamager())){
                event.setCancelled(false);
            }
        }
    }
}
