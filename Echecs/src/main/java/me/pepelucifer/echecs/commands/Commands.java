package me.pepelucifer.echecs.commands;


import me.pepelucifer.echecs.Echecs;
import me.pepelucifer.echecs.custommaps.TraceurImage;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import sun.rmi.runtime.Log;

public class Commands extends Logique implements CommandExecutor {

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
        if (cmd.equals("echecs")){
            checkValidConnect(player);
        }else if (cmd.equals("quitter")){
            checkValidDisconnect(player);
        }else if (cmd.equals("dev")){
            Logique.isEnModeDeveloppement=!isEnModeDeveloppement;
            if (isInLobby(player)){
                LobbyPlayer lobbyPlayer=getLobbyPlayer(player);
                if (lobbyPlayer.isPlaying()){
                    lobbyPlayer.getSession().testResetPanneauEchiquiers(isEnModeDeveloppement);
                }
            }
            if (isEnModeDeveloppement){
                Bukkit.broadcastMessage("§2Mode développement activé.");
            }else{
                Bukkit.broadcastMessage("§4Mode développement désactivé.");
            }
        }
    }
}
