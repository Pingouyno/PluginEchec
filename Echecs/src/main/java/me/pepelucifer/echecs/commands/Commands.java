package me.pepelucifer.echecs.commands;


import me.pepelucifer.echecs.Echecs;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

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
            LobbyPlayer lobbyPlayer=Echecs.lobby.getLobbyPlayer(player);
            if (lobbyPlayer==null){
                Echecs.lobby.connectPlayer(player);
            }else{
                player.sendMessage(ChatColor.RED+"Vous êtes déjà dans un salon d'échecs!");
            }
        }else if (cmd.equals("quit")){
            LobbyPlayer lobbyPlayer=Echecs.lobby.getLobbyPlayer(player);
            if (lobbyPlayer!=null){
                Echecs.lobby.disconnectPlayer(lobbyPlayer);
            }else{
                player.sendMessage(ChatColor.RED+"Vous n'êtes pas dans un salon d'échecs!");
            }
        }
    }
}
