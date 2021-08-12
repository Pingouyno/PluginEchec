package me.pepelucifer.pluginechec.commands;

import me.pepelucifer.pluginechec.PluginEchec;
import me.pepelucifer.pluginechec.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String cmd= command.getName().toLowerCase();
            executeCommand(cmd,player);
            return true;
        }else{
            sender.sendMessage(ChatColor.RED+"Seuls les joueurs ont accès a cette commande!");
            return false;
        }
    }

    public void executeCommand(String cmd, Player player){
        if (cmd.equals("chess")){
            player.teleport(PluginEchec.lobby.getSpawnLocation());
            player.sendMessage("Vous avez rejoint un lobby d'échecs.");
            player.getInventory().addItem(ItemManager.chessPiece);
        }else if (cmd.equals("quit")){
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            player.sendMessage("Vous avez quitté un lobby d'échecs.");
            player.getInventory().removeItem(ItemManager.chessPiece);
        }
    }
}
