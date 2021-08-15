package me.pepelucifer.echecs;

import me.pepelucifer.echecs.commands.Commands;
import me.pepelucifer.echecs.events.Events;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Echecs extends JavaPlugin {

    @Override
    public void onEnable() {
        Logique.init();
        getServer().getPluginManager().registerEvents(new Events(),this);
        getCommand("echecs").setExecutor(new Commands());
        getCommand("quitter").setExecutor(new Commands());
        ItemManager.init();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Plugin Echecs démarré.");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED+"echecs arrêté.");
    }
}


