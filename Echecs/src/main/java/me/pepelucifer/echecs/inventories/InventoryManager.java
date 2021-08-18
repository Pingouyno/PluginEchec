package me.pepelucifer.echecs.inventories;

import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryManager {
    public static void giveChessMenuInventory(Player player){
        Inventory inventory = Bukkit.createInventory(null, 54, "Choisir un joueur à défier");
        int cpt=0;
        for (LobbyPlayer people:Logique.lobby.getWaitingPlayers()){
            if (cpt==54){
                player.sendMessage(ChatColor.RED+"Le nombre de joueurs connectés dépasse la capacité du menu Échecs.\nVeuillez avertir un administrateur.");
                break;
            }
            if (!player.getName().equals(people.getName())){
                inventory.setItem(cpt, ItemManager.getChessHeadItem(people.getPlayer()));
                cpt++;
            }
        }
        for (int i=cpt;i<inventory.getSize();i++){
            inventory.setItem(i,ItemManager.chessEmptySlot);
        }
        player.openInventory(inventory);
    }
}
