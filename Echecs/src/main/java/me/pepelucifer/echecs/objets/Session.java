package me.pepelucifer.echecs.objets;
import com.sun.org.apache.xpath.internal.operations.Bool;
import me.pepelucifer.echecs.chesslib.*;
import me.pepelucifer.echecs.chesslib.move.Move;
import me.pepelucifer.echecs.items.ItemManager;
import me.pepelucifer.echecs.logique.Logique;
import me.pepelucifer.echecs.scoreboard.SB;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapCanvas;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.util.ArrayList;
import java.util.List;
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
    public boolean over;
    World world;
    Board echiquier;
    Boolean auxBlancsAJouer;
    Location premiereCaseBlanc;
    Location premiereCaseNoir;
    String derniereCaseCliquee;
    private String leVainqueur;
    public int winType;

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
        this.over=false;
        this.cadresUUIDsBlancs=new UUID[64];
        this.cadresUUIDsNoirs=new UUID[64];
        this.derniereCaseCliquee=null;
        if (!Logique.isEnDebugging){
            initialiserCadresEtBlocs();
        }
    }

    public boolean isTraitAuxBlancs(){
        return auxBlancsAJouer;
    }

    public void setTraitAuxBlancs(boolean trait){
        auxBlancsAJouer=trait;
    }

    public void setDernierClic(String nomCase){derniereCaseCliquee=nomCase;}

    public void setVainqueur(String vainqueur){leVainqueur=vainqueur;}

    public String getVainqueur(){return leVainqueur;}

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

    public int getWinType(){return winType;}

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

    public boolean isOver(){
        return over;
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

        String promotionPossible = "";                                                                //Gérer la promotion temporairement
        Piece piecePromotionPossible=getEchiquier().getPiece(Square.valueOf(caseDebut));
        if (piecePromotionPossible.getPieceType()!=null&&piecePromotionPossible.getPieceType().equals(PieceType.PAWN)){
            Rank toSquareRank = Square.valueOf(caseFin).getRank();
            if (toSquareRank.equals(Rank.RANK_8)&&getEchiquier().getSideToMove().equals(Side.WHITE)||
                    toSquareRank.equals(Rank.RANK_1)&&getEchiquier().getSideToMove().equals(Side.BLACK)){
                promotionPossible=PieceType.QUEEN.toString();
            }
        }                                                                                            //FIN gérer la promotion

        String coup=caseDebut+caseFin+promotionPossible;
        setDernierClic(null);
        Move move;
        if (isTraitAuxBlancs()){
            move = new Move(coup,Side.WHITE);
        }else{
            move = new Move(coup,Side.BLACK);
        }
        if (isCoupLegal(move)){
            resetDrawOffers(true);
            echiquier.doMove(move);
            drawBoards();
            redessinerCase(caseDebut,true);
            redessinerCase(caseFin,true);
            Logique.checkGameEnd(this);
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
            getPlayer(isTraitAuxBlancs()).getPlayer().sendMessage(ChatColor.RED+"Ce coup est illégal!");
            setDernierClic(null);
            redessinerCase(caseDebut,false);
        }
    }


    public void resetDrawOffers(Boolean enFinDeCoup){
        if (getWhite().isRequestingDraw()){
            getWhite().getPlayer().sendMessage(ChatColor.RED+getBlack().getName()+" a refusé la nulle.");
        }else if (getBlack().isRequestingDraw()){
            getBlack().getPlayer().sendMessage(ChatColor.RED+getWhite().getName()+" a refusé la nulle.");
        }

        for (LobbyPlayer people:getPlayers()){
            people.isRequestingDraw=false;
            if (enFinDeCoup){
                PlayerInventory inv=people.getPlayer().getInventory();
                if (inv.getHeldItemSlot()==7){
                    inv.setHeldItemSlot(6);
                }
                inv.setItem(7,ItemManager.chessNulleItem);
            }
        }
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
                ItemStack map = ItemManager.getChessPiece(indexNom,isPieceBlanc,isCaseBlanc,isCaseVide,false);
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

    public void initialiserCadresEtBlocs(){
        Location caseCouranteBlancs=premiereCaseBlanc.clone().add(0,-7,0);
        Location caseCouranteNoirs=premiereCaseNoir.clone().add(0,0,7);
        String nomCase;
        for (int i=0;i<64;i++){
            nomCase=Square.squareAt(i).toString();

            Block blocBlanc = getWorld().getBlockAt(caseCouranteBlancs.getBlockX()+1,caseCouranteBlancs.getBlockY(),caseCouranteBlancs.getBlockZ());
            Block blocNoir = getWorld().getBlockAt(caseCouranteNoirs.getBlockX()+1,caseCouranteNoirs.getBlockY(),caseCouranteNoirs.getBlockZ());
            blocBlanc.setType(Material.STONE);
            blocNoir.setType(Material.STONE);
            Logique.setValeurCase(blocBlanc,nomCase);
            Logique.setValeurCase(blocNoir,nomCase);

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


    public void redessinerCase(String caseRedessinee,Boolean isOverlayJaune){
        Square cetteCase=Square.valueOf(caseRedessinee);
        Boolean isPieceBlanc;
        Boolean isCaseBlanc;
        Boolean isCaseVide;
        int indexNom=-1;
        Piece piece = getEchiquier().getPiece(cetteCase);
        isPieceBlanc=(piece.getPieceSide()==Side.WHITE);
        isCaseBlanc=cetteCase.isLightSquare();
        isCaseVide=false;
        if (piece.getPieceType()!=null) {
            switch (piece.getPieceType()){
                case PAWN:
                    indexNom=0;
                    break;
                case ROOK:
                    indexNom=1;
                    break;
                case KNIGHT:
                    indexNom=2;
                    break;
                case BISHOP:
                    indexNom=3;
                    break;
                case QUEEN:
                    indexNom=4;
                    break;
                case KING:
                    indexNom=5;
                    break;
                case NONE:
                    indexNom=5;
                    isCaseVide=true;
                    break;
            }
        }else{
            indexNom=5;
            isCaseVide=true;
        }
        int numeroCase=cetteCase.ordinal();
        ItemFrame cadreBlanc = (ItemFrame) Bukkit.getEntity(cadresUUIDsBlancs[numeroCase]);
        ItemFrame cadreNoir = (ItemFrame) Bukkit.getEntity(cadresUUIDsNoirs[numeroCase]);
        ItemStack mapJaune=ItemManager.getChessPiece(indexNom,isPieceBlanc,isCaseBlanc,isCaseVide,isOverlayJaune);
        cadreBlanc.setItem(mapJaune);
        cadreNoir.setItem(mapJaune);
    }


    public void startClockCountDown(Session session){
        new BukkitRunnable() {                                                //boucle pour rendre le reste du code synchrone, oui oui je sais c'est DÉGUEULASSE
            LobbyPlayer white = getWhite();
            LobbyPlayer black = getBlack();
            int cpt=0;
            public void run() {
                if (!isOver()){
                    cpt++;
                    if (isTraitAuxBlancs()){
                        white.timeLeft--;
                    }else{
                        black.timeLeft--;
                    }
                    if (cpt==5){
                        SB.redrawAllScoreBoard(session);
                        cpt=0;
                    }
                    if (white.getClockTime()<10){
                        session.winType=9;
                        setVainqueur("noirs");
                        Logique.endGame(session);
                    }else if (black.getClockTime()<20){
                        winType=9;
                        setVainqueur("blancs");
                        Logique.endGame(session);
                    }
                }else{
                    cancel();
                    return;
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Echecs"), 0L, 1L);
    }
}
