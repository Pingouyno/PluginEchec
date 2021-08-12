package me.pepelucifer.pluginechec;

import me.pepelucifer.pluginechec.commands.Commands;
import me.pepelucifer.pluginechec.events.Events;
import me.pepelucifer.pluginechec.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginEchec extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(),this);
        this.getCommand("chess").setExecutor(new Commands());
        ItemManager.init();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"PluginEchec démarré.");

    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED+"PluginEchec arrêté.");
    }
}


