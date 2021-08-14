package me.pepelucifer.echecs.events;

import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EventLogique extends Logique {
    public void checkItemStack(ItemStack item, Player player){
        if ((item!=null) && item.equals(ItemManager.porteQuitter)){
            disconnectPlayer(getLobbyPlayer(player));
        }
    }

    public void checkItemStack(ItemStack item, HumanEntity player){
        if ((item!=null) && item.equals(ItemManager.porteQuitter)){
            disconnectPlayer(getLobbyPlayer(player));
        }
    }
}
