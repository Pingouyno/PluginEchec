package me.pepelucifer.echecs.events;

import me.pepelucifer.echecs.inventories.InventoryManager;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                    LobbyPlayer lobbyPlayer = getLobbyPlayer(player);
                    lobby.startSession(lobbyPlayer,getOtherPlayer(lobbyPlayer));
                    return true;
                default:
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"ERREUR : OBJET LOCALIZEDNAME 'CHESS' SANS SUBTYPE VALIDE");
                    return true;
            }
        }
        return false;
    }

    private LobbyPlayer getOtherPlayer(LobbyPlayer thisP){
        for (LobbyPlayer people : lobby.getWaitingPlayers()){
            if (!people.equals(thisP)){
                return (people);
            }
        }
        return null;
    }


    public void checkByObject(ItemStack item, Player player){
        if (item.equals(ItemManager.porteQuitter)) {
            disconnectPlayer(getLobbyPlayer(player));
        }
    }
}
