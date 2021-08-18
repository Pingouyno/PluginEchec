package me.pepelucifer.echecs.objets;
import me.pepelucifer.echecs.chesslib.Piece;
import me.pepelucifer.echecs.chesslib.Side;
import me.pepelucifer.echecs.chesslib.Square;
import me.pepelucifer.echecs.chesslib.move.Move;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.chesslib.Board;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Session{
    Lobby lobby;
    int id;
    UUID[] cadresUUIDsBlancs;
    UUID[] cadresUUIDsNoirs;
    ArrayList<LobbyPlayer> players;
    boolean started;
    public boolean locked;
    World world;
    Board echiquier;
    Boolean auxBlancsAJouer;
    Location premiereCaseBlanc;
    Location premiereCaseNoir;
    String derniereCaseCliquee;

    public Session(int id, LobbyPlayer p1, LobbyPlayer p2) {
        this.id = id;
        this.players = new ArrayList<LobbyPlayer>();
        players.add(p1);
        players.add(p2);
        this.started = true;
        this.lobby=Logique.lobby;
        this.echiquier = new Board();
        this.auxBlancsAJouer = true;
        this.premiereCaseBlanc = Logique.getLocationDecalee(id,Logique.premiereCaseUniverselleBlanc);
        this.premiereCaseNoir = Logique.getLocationDecalee(id,Logique.premiereCaseUniverselleNoir);
        this.world=Logique.chessWorld;
        this.locked=false;
        this.cadresUUIDsBlancs=new UUID[64];
        this.cadresUUIDsNoirs=new UUID[64];
        this.derniereCaseCliquee=null;
        initialiserCadres();
    }

    public boolean isTraitAuxBlancs(){
        return auxBlancsAJouer;
    }

    public void setTraitAuxBlancs(boolean trait){
        auxBlancsAJouer=trait;
    }

    public void setDernierClic(String nomCase){derniereCaseCliquee=nomCase;}

    public String getDernierClic(){return derniereCaseCliquee;}

    public int getSessionId(){
        return id;
    }

    public World getWorld() {
        return world;
    }

    public Board getEchiquier(){
        return this.echiquier;
    }

    public int getPlayerCount(){
        return players.size();
    }

    public Lobby getLobby(){
        return lobby;
    }

    public boolean isLocked(){return locked;}

    public void inverserTrait(){
        setTraitAuxBlancs(!isTraitAuxBlancs());
    }

    public boolean isStarted(){
        return started;
    }

    public ArrayList<LobbyPlayer> getPlayers(){
        return players;
    }

    public boolean isCoupLegal(Move move){
        return (getEchiquier().legalMoves().contains(move));
    }

    public void messageJoueurs(String msg){
        for (LobbyPlayer people:getPlayers()){
            people.getPlayer().sendMessage(msg);
        }
    }

    public LobbyPlayer getPlayer(boolean getWhite){
        if (getWhite){
            return getWhite();
        }else{
            return getBlack();
        }
    }

    public LobbyPlayer getWhite(){
        if (getPlayers().get(0).isWhite()){
            return getPlayers().get(0);
        }else{
            return getPlayers().get(1);
        }
    }

    public LobbyPlayer getBlack(){
        if (!getPlayers().get(0).isWhite()){
            return getPlayers().get(0);
        }else{
            return getPlayers().get(1);
        }
    }

    public void jouer(String caseDebut,String caseFin){
        String coup=caseDebut+caseFin;
        setDernierClic(null);
        Move move;
        if (isTraitAuxBlancs()){
            move = new Move(coup,Side.WHITE);
        }else{
            move = new Move(coup,Side.BLACK);
        }
        if (isCoupLegal(move)){
            echiquier.doMove(move);
            inverserTrait();

            /*
            if (isTraitAuxBlancs()){
                Bukkit.broadcastMessage("Les blancs jouent :");
            }else{
                Bukkit.broadcastMessage("Les noirs jouent :");
            }
            Bukkit.broadcastMessage("Depart du coup : " + move.getFrom());
            Bukkit.broadcastMessage("Arrivee du coup : " + move.getTo());
            */

        }else{
            getPlayer(isTraitAuxBlancs()).getPlayer().sendMessage(ChatColor.RED+"Coup invalide!");
        }
        drawBoards();
        Logique.checkGameEnd(this);
    }


    public void drawBoards(){
        drawBoardFromPerspective(true);
        drawBoardFromPerspective(false);
    }

    public void decalerCase(Location caseCourante, int cpt){
        if (cpt%8==0){
            caseCourante.add(0,-1,-7);
        }else{
            caseCourante.add(0,0,1);
        }
    }

    public void decalerCadre(Location caseCourante, int cpt, Boolean isEchiqBlanc){
        if (isEchiqBlanc){                                                                                                //Square.getvalues va de a1,b1,c1, etc
            if (cpt%8==0){
                caseCourante.add(0,+1,-7);
            }else{
                caseCourante.add(0,0,1);
            }
        }else{
            if (cpt%8==0){
                caseCourante.add(0,-1,7);
            }else{
                caseCourante.add(0,0,-1);
            }
        }
    }

    public void drawBoardFromPerspective(boolean asWhite){
        Location caseCourante;
        String[] echiq;
        int coord_x;
        int coord_y;
        if (asWhite){
            caseCourante=premiereCaseBlanc.clone();
            echiq=echiquier.toStringFromWhiteViewPoint().split("\n");
            coord_x=0;
            coord_y=7;
        }else{
            caseCourante=premiereCaseNoir.clone();
            echiq=echiquier.toStringFromBlackViewPoint().split("\n");
            coord_x=7;
            coord_y=0;
        }
        int cpt=0;
        int i=0;
        char c;
        boolean isCaseBlanc=false;
        for (String str:echiq){
            while (cpt<8){
                isCaseBlanc=!isCaseBlanc;
                c=str.charAt(cpt);
                Boolean isPieceBlanc=(Character.isUpperCase(c));
                Boolean isCaseVide=false;
                int indexNom=-1;

                switch (Character.toLowerCase(c)) {
                    case 'p':
                        indexNom=0;
                        break;
                    case 'r':
                        indexNom=1;
                        break;
                    case 'n':
                        indexNom=2;
                        break;
                    case 'b':
                        indexNom=3;
                        break;
                    case 'q':
                        indexNom=4;
                        break;
                    case 'k':
                        indexNom=5;
                        break;
                    case '.':
                        indexNom=6;
                        isCaseVide=true;
                        break;
                }
                Entity entity;
                if (asWhite){
                    entity = Bukkit.getEntity(cadresUUIDsBlancs[coordonnesadaptees(coord_x,coord_y,asWhite)]);
                    coord_x++;
                    if (coord_x==8){
                        coord_x=0;
                        coord_y--;
                    }
                }else{
                    entity = Bukkit.getEntity(cadresUUIDsNoirs[coordonnesadaptees(coord_x,coord_y,asWhite)]);
                    coord_x--;
                    if (coord_x==-1){
                        coord_x=7;
                        coord_y++;
                    }
                }
                ItemStack map = ItemManager.getChessPiece(indexNom,isPieceBlanc,isCaseBlanc,isCaseVide);
                ItemFrame cadre = (ItemFrame) entity;
                cadre.setItem(map);
                cpt++;
                i++;
                decalerCase(caseCourante,cpt);
            }
            isCaseBlanc=!isCaseBlanc;
            cpt=0;
        }
    }


    private int coordonnesadaptees(int x,int y, Boolean asWhite){                                                      //Fonction Board.toString() avance de case en case d'une manière différente que Square.values()
        return (y*8)+x;
    }

    public void initialiserCadres(){
        Location caseCouranteBlancs=premiereCaseBlanc.clone().add(0,-7,0);
        Location caseCouranteNoirs=premiereCaseNoir.clone().add(0,0,7);
        String nomCase;
        for (int i=0;i<64;i++){
            nomCase=Square.squareAt(i).toString();
            Entity cadreBlanc=getWorld().spawnEntity(caseCouranteBlancs, EntityType.ITEM_FRAME);
            Entity cadreNoir=getWorld().spawnEntity(caseCouranteNoirs, EntityType.ITEM_FRAME);
            cadresUUIDsBlancs[i] = cadreBlanc.getUniqueId();
            cadresUUIDsNoirs[i] = cadreNoir.getUniqueId();
            cadreBlanc.setCustomName(nomCase);
            cadreNoir.setCustomName(nomCase);
            decalerCadre(caseCouranteBlancs, i+1,true);
            decalerCadre(caseCouranteNoirs, i+1,false);
            ((ItemFrame) cadreBlanc).setVisible(false);
            ((ItemFrame) cadreNoir).setVisible(false);
        }
    }

    public void retirerCadres(){
        for (UUID uuid:cadresUUIDsBlancs){
            Bukkit.getEntity(uuid).remove();
        }
        for (UUID uuid:cadresUUIDsNoirs){
            Bukkit.getEntity(uuid).remove();
        }
    }

    public void testResetPanneauEchiquiers(Boolean setAir){
        World world=getWorld();
        Location caseCouranteBlancs=premiereCaseBlanc.clone().add(+1,0,0);
        Location caseCouranteNoirs=premiereCaseNoir.clone().add(+1,0,0);
        Material material;
        if (setAir){
            material=Material.AIR;
        }else{
            material=Material.STONE;
        }
        for (int i=0;i<64;i++){
            world.getBlockAt(caseCouranteBlancs).setType(material);
            world.getBlockAt(caseCouranteNoirs).setType(material);
            decalerCase(caseCouranteBlancs, i+1);
            decalerCase(caseCouranteNoirs, i+1);
        }
    }
}
