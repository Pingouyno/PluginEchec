package me.pepelucifer.echecs.events;

import me.pepelucifer.echecs.inventories.InventoryManager;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EventLogique extends Logique {
    public void checkItemStack(ItemStack item, Object objectPlayer) {
        Player player = (Player)objectPlayer;
        if ((item != null && item.getItemMeta()!=null && item.getItemMeta().getLocalizedName()!=null)){
            if (!checkByLocalizedName(item,player));{
                checkByObject(item,player);
            }
        }
    }


    public boolean checkByLocalizedName(ItemStack item, Player player){
        String localizedName = item.getItemMeta().getLocalizedName();
        if (localizedName.startsWith("chess")){
            int subType=Integer.parseInt(localizedName.substring(6,7));
            switch (subType){
                case 0:                                                                 //chessEmptySlot
                    return true;
                case 1:                                                                 //chessMenuButton
                    InventoryManager.giveChessMenuInventory(player);
                    return true;
                case 2:                                                                 //chessHeadItem
                    LobbyPlayer ceJoueur = getLobbyPlayer(player);
                    LobbyPlayer autreJoueur = getLobbyPlayer(((SkullMeta) item.getItemMeta()).getOwningPlayer().getPlayer());
                    if (ceJoueur!=null && autreJoueur!=null){
                        if (!ceJoueur.isInSession() && !autreJoueur.isInSession()){
                            lobby.startSession(ceJoueur,autreJoueur);
                        }else{
                            player.sendMessage(ChatColor.RED+"Ce joueur est déja en train de jouer!");
                        }
                    }else{
                        player.sendMessage(ChatColor.RED+"Joueur introuvable!");
                    }
                    return true;
                default:
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"ERREUR : OBJET LOCALIZEDNAME 'CHESS' SANS SUBTYPE VALIDE");
                    return true;
            }
        }
        return false;
    }


    public void checkByObject(ItemStack item, Player player){
        if (item.equals(ItemManager.porteQuitter)) {
            disconnectPlayer(getLobbyPlayer(player));
        }
    }
}
