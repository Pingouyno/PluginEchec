package me.pepelucifer.echecs;

import me.pepelucifer.echecs.commands.Commands;
import me.pepelucifer.echecs.events.Events;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Echecs extends JavaPlugin {

    @Override
    public void onEnable() {
        Logique.init();
        Logique.pluginFolder=this.getDataFolder().getAbsolutePath();
        getServer().getPluginManager().registerEvents(new Events(),this);
        getCommand("echecs").setExecutor(new Commands());
        getCommand("quitter").setExecutor(new Commands());
        getCommand("dev").setExecutor(new Commands());
        getCommand("echecsaccept").setExecutor(new Commands());
        getCommand("echecsnulle").setExecutor(new Commands());
        ItemManager.init();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"Plugin Echecs demarre.");
    }


    @Override
    public void onDisable() {
        int waitingPlayerSize = Logique.lobby.getWaitingPlayers().size();                                                             //Pour une quelconque raison, les boucles FOR buguent lorsqu'on désactive le plugin
        int playingPlayerSize = Logique.lobby.getPlayingPlayers().size();

        for (int i=0;i<waitingPlayerSize;i++){
            Logique.disconnectPlayer(Logique.lobby.getWaitingPlayers().get(0));
        }
        for (int i=0;i<playingPlayerSize;i++){
            Logique.lobby.getPlayingPlayers().get(0).getSession().locked=true;
            Logique.disconnectPlayer(Logique.lobby.getPlayingPlayers().get(0));
        }
        getServer().getConsoleSender().sendMessage(ChatColor.RED+"Plugin Echecs arrete.");
    }
}


