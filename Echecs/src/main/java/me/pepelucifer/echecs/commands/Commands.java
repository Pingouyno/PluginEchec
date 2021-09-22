package me.pepelucifer.echecs.commands;


import me.pepelucifer.echecs.Echecs;
import me.pepelucifer.echecs.custommaps.TraceurImage;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.objets.LobbyPlayer;
import me.pepelucifer.echecs.objets.Request;
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
            executeCommand(cmd,player,args);
            return true;
        }else{
            sender.sendMessage(ChatColor.RED+"Seuls les joueurs ont accès a cette commande!");
            return false;
        }
    }

    public void executeCommand(String cmd, Player player, String[] args){
        if (cmd.equals("echecs")){
            checkValidConnect(player);
        }else if (cmd.equals("quitter")){
            checkValidDisconnect(player);
        }else if (cmd.equals("dev")){
            if (player.isOp()&&isInLobby(player)){
                isEnDebugging=!isEnDebugging;
                LobbyPlayer lobbyPlayer=getLobbyPlayer(player);
                if (lobbyPlayer!=null && lobbyPlayer.isPlaying()){
                    lobbyPlayer.getSession().testResetPanneauEchiquiers(isEnDebugging);
                }
            }
            if (isEnDebugging){
                Logique.lobby.despawnDummyBoards();
                Bukkit.broadcastMessage("§2Mode débug activé.");
            }else{
                Logique.lobby.spawnDummyBoards();
                Bukkit.broadcastMessage("§4Mode débug désactivé.");
            }
        }else if (cmd.equals("echecsaccept")){
            if (args.length==0){
                player.sendMessage(ChatColor.AQUA+"USAGE : /echecsaccepter [nom joueur]");
            }
            if (args.length==1){
                if (isInLobby(player)){
                    LobbyPlayer receiver = getLobbyPlayer(player);
                    LobbyPlayer sender = getLobbyPlayer(args[0]);
                    if (sender!=null && receiver!=null){
                        Request request=receiver.getRequest(sender);
                        if (request!=null && !request.isExpired()){
                            Logique.lobby.checkStartSession(receiver,sender);
                        }else{
                            player.sendMessage(ChatColor.RED+"Cette requête est expirée");
                        }
                    }else{
                        player.sendMessage(ChatColor.RED+"Joueur introuvable!");
                    }
                }
            }
        }else if (cmd.equals("echecsnulle")){
            if (args.length==0){
                player.sendMessage(ChatColor.AQUA+"USAGE : /echecsnulle [true/false]");
            }
            if (args.length==1){
                if (isInLobby(player)){
                    LobbyPlayer receiver = getLobbyPlayer(player);
                    if (receiver!=null && receiver.isPlaying() && !receiver.getSession().isOver()){
                        if (args[0].equals("true")){
                            receiver.checkOfferDraw();
                        }else if (args[0].equals("false")){
                            receiver.getSession().resetDrawOffers(false);
                        }
                    }
                }
            }
        }
    }
}
