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
    public void checkItemStack(ItemStack item, LobbyPlayer lobbyPlayer) {
        if ((item != null && item.getItemMeta()!=null && item.getItemMeta().getLocalizedName()!=null) && lobbyPlayer!=null){
            if (!checkByLocalizedName(item,lobbyPlayer));{
                checkByObject(item,lobbyPlayer);
            }
        }
    }

    public void checkByObject(ItemStack item, LobbyPlayer lobbyPlayer){
        if (item.equals(ItemManager.porteQuitter)) {
            disconnectPlayer(lobbyPlayer);
        }
    }

    public boolean checkByLocalizedName(ItemStack item, LobbyPlayer lobbyPlayer){
        String localizedName = item.getItemMeta().getLocalizedName();
        if (localizedName.startsWith("chess")){
            int subType=Integer.parseInt(localizedName.substring(6,7));
            switch (subType){
                case 0:                                                                 //alors objets qui correspondent à menu lobby
                    switchMenuType(lobbyPlayer,localizedName,item);
                    return true;
                case 1:                                                                 //alors objets d'abandon
                    switchAbandonType(lobbyPlayer,localizedName,item);
                    return true;
                case 2:
                    lobbyPlayer.checkOfferDraw();
                    return true;                                                        //alors objets de nulle
            }
        }
        return false;
    }



                                                                                                                            //index: 0 1 2 3 4 5 6 7 8 9
    public void switchMenuType(LobbyPlayer lobbyPlayer, String localizedName, ItemStack item){                      //LocalizedName a l'air de C H E S S _ 0 _ 0
        int subType=Integer.parseInt(localizedName.substring(8,9));
        switch (subType){
            case 0:                                                                 //chessEmptySlot
                return;
            case 1:                                                                 //chessMenuButton
                InventoryManager.giveChessMenuInventory(lobbyPlayer.getPlayer());
                return;
            case 2:                                                                 //chessHeadItem
                LobbyPlayer autreJoueur = getLobbyPlayer(((SkullMeta) item.getItemMeta()).getOwningPlayer().getPlayer());
                if (lobbyPlayer!=null && autreJoueur!=null){
                    lobbyPlayer.checkCreateNewRequest(autreJoueur);
                }else{
                    lobbyPlayer.getPlayer().sendMessage(ChatColor.RED+"Joueur introuvable!");
                }
                return;
            default:
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"ERREUR : OBJET LOCALIZEDNAME 'CHESS' SANS SUBMENUTYPE VALIDE");
                return;
        }
    }


    public void switchAbandonType(LobbyPlayer lobbyPlayer, String localizedName, ItemStack item){

        int subType=Integer.parseInt(localizedName.substring(8,9));
        switch (subType){
            case 0:                                                                 //chessAbandon
                InventoryManager.giveChessResignInventory(lobbyPlayer.getPlayer());
                return;
            case 1:                                                                 //chessAbandonConfirm
                lobbyPlayer.checkResign();
                return;
            case 2:                                                                 //chessAbandonCancel
                lobbyPlayer.getPlayer().closeInventory();
                return;
            default:
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"ERREUR : OBJET LOCALIZEDNAME 'CHESS' SANS SUBABANDONTYPE VALIDE");
                return;
        }
    }
}
