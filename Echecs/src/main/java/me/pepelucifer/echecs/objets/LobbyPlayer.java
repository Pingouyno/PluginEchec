package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.scoreboard.SB;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class LobbyPlayer{
    private Player player;
    private Session session;
    private boolean playing;
    public boolean isWhite;
    public boolean isRequestingDraw;
    public int timeLeft;
    private ArrayList<Request> outRequests;
    private ArrayList<Request> inRequests;
    private Scoreboard firstScoreBoard;
    private Scoreboard secondScoreBoard;


    public LobbyPlayer(Player player) {
        this.player = (player);
        this.playing=false;
        this.isWhite=false;
        this.timeLeft=6000;
        this.outRequests = new ArrayList<Request>();
        this.inRequests = new ArrayList<Request>();
        this.firstScoreBoard= SB.newPlayerScoreBoard();
        this.secondScoreBoard= SB.newPlayerScoreBoard();
    }

    public void setSession(Session session){this.session=session;}

    public Session getSession(){
        return session;
    }

    public boolean isWhite(){
        return isWhite;
    }

    public int getClockTime(){
        return timeLeft;
    }

    public int getSessionId(){
        return getSession().getSessionId();
    }

    public void setPlaying(boolean isPlaying) {
        playing=isPlaying;
    }

    public boolean isRequestingDraw(){
        return isRequestingDraw;
    }

    public String getName(){
        return player.getName();
    }

    public boolean isPlaying(){
        return playing;
    }

    public Scoreboard getOtherScoreBoard(){
        if (getPlayer().getScoreboard().equals(firstScoreBoard)){
            return secondScoreBoard;
        }else{
            return firstScoreBoard;
        }
    }

    public boolean isInSession() { return (getSession()!=null);}

    public Player getPlayer(){
        return player;
    }

    public ArrayList<Request> getOutRequests(){
        return outRequests;
    }

    public ArrayList<Request> getInRequests(){
        return inRequests;
    }

    public void checkCreateNewRequest(LobbyPlayer receiver){
        if (this.equals(receiver)){
            getPlayer().sendMessage(ChatColor.RED+"Vous ne pouvez pas vous défier vous-même!");
            return;
        }
        if (!receiver.isPlaying()){
            if (!this.isPlaying()){
                for (Request request:getOutRequests()){
                    if (request.getReceiver().equals(receiver) && !request.isExpired()){
                        getPlayer().sendMessage(ChatColor.RED+"Vous avez déjà envoyé une requête à ce joueur. Attendez quelques instants avant d'en envoyer une autre");
                        return;
                    }
                }
                Request request = getRequest(receiver);
                if (request!=null && !request.isExpired()){
                    Logique.lobby.checkStartSession(request.getReceiver(),request.getSender());                                   //vérifie si le receveur a déjà une requête en attente
                }else{
                    createNewRequest(receiver);
                }
            }else{
                getPlayer().sendMessage(ChatColor.RED+"Vous êtes déjà en partie!");
            }
        }else{
            getPlayer().sendMessage(ChatColor.RED+"Ce joueur est déjà en partie!");
        }
    }

    private void createNewRequest(LobbyPlayer receiver){
        getPlayer().closeInventory();
        Request request = new Request(this,receiver);
        getOutRequests().add(request);
        receiver.getInRequests().add(request);
        sendRequestMessage(receiver);
    }

    private void sendRequestMessage(LobbyPlayer receiver){
        String cmd = "/echecsaccept "+getName();
        TextComponent tc = new TextComponent();
        tc.setText("\n"+ChatColor.BLUE+getName()+ChatColor.GOLD+" vous a défié. ");
        TextComponent tc2 = new TextComponent();
        tc2.setText(ChatColor.GREEN+"[Accepter]");
        tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,cmd));
        tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour accepter").create()));
        tc.addExtra(tc2);
        receiver.getPlayer().spigot().sendMessage(ChatMessageType.CHAT,tc);
        getPlayer().sendMessage(ChatColor.GOLD+"Vous avez envoyé un défi à "+ChatColor.BLUE+receiver.getName()+".");
    }

    private void sendDrawRequestMessage(LobbyPlayer receiver){
        String cmdAccept = "/echecsnulle true";
        String cmdRefuser = "/echecsnulle false";

        TextComponent tc = new TextComponent();
        tc.setText(ChatColor.BLUE+getName()+ChatColor.GOLD+" vous propose la nulle.\n     ");

        TextComponent tc2 = new TextComponent();
        tc2.setText(ChatColor.GREEN+"[Accepter]");
        tc2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,cmdAccept));
        tc2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour accepter").create()));
        tc.addExtra(tc2);

        TextComponent tc3 = new TextComponent();
        tc3.setText("  ");
        tc.addExtra(tc3);

        TextComponent tc4 = new TextComponent();
        tc4.setText(ChatColor.RED+"[Refuser]");
        tc4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,cmdRefuser));
        tc4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour refuser").create()));
        tc.addExtra(tc4);

        receiver.getPlayer().spigot().sendMessage(ChatMessageType.CHAT,tc);
    }

    public Request getRequest(LobbyPlayer requestSender){
        for (Request request:getInRequests()){
            if (request.getSender().equals(requestSender)){
                return request;
            }
        }
        return null;
    }


    public void checkOfferDraw(){
        if (isPlaying()&&!getSession().isOver()){
            if (!isRequestingDraw() && getPlayer().getInventory().getItem(7)!=null){
                getPlayer().closeInventory();
                sendDrawOffer();
            }else{
                getPlayer().sendMessage(ChatColor.RED+"Proposition de nulle déjà envoyée!");
            }
        }
    }

    private void sendDrawOffer(){
        isRequestingDraw=true;
        getPlayer().getInventory().remove(ItemManager.chessNulleItem);
        Logique.checkGameEnd(session);
        if (!getSession().isOver()){
            for (LobbyPlayer people:getSession().getPlayers()){
                if (!people.equals(this)){
                    getPlayer().sendMessage(ChatColor.GOLD+"Vous avez offert la nulle à "+ChatColor.BLUE+people.getName()+".");
                    sendDrawRequestMessage(people);
                    break;
                }
            }
        }
    }

    public void checkResign(){
        if (isPlaying()&&!getSession().isOver()){
            getSession().winType=8;
            if (isWhite()){
                getSession().setVainqueur("noirs");
            }else{
                getSession().setVainqueur("blancs");
            }
            Logique.endGame(getSession());
        }
    }
}
