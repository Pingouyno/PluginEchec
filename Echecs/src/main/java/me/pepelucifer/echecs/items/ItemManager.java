package me.pepelucifer.echecs.items;
import me.pepelucifer.echecs.Echecs;
import me.pepelucifer.echecs.custommaps.TraceurImage;
import me.pepelucifer.echecs.logique.Logique;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static ItemStack porteQuitter;
    public static ItemStack chessEmptySlot;
    public static ItemStack chessMenuButton;
    public static ItemStack[] chessAbandonItems = new ItemStack[3];
    public static ItemStack chessNulleItem;

    public static ItemStack[] whiteChessPiecesWhiteSquare = new ItemStack[6];
    public static ItemStack[] whiteChessPiecesBlackSquare = new ItemStack[6];
    public static ItemStack[] blackChessPiecesWhiteSquare = new ItemStack[6];
    public static ItemStack[] blackChessPiecesBlackSquare = new ItemStack[6];
    public static ItemStack[] whiteChessPiecesWhiteSquareYellow = new ItemStack[6];
    public static ItemStack[] whiteChessPiecesBlackSquareYellow = new ItemStack[6];
    public static ItemStack[] blackChessPiecesWhiteSquareYellow = new ItemStack[6];
    public static ItemStack[] blackChessPiecesBlackSquareYellow = new ItemStack[6];
    public static ItemStack[] emptySquares = new ItemStack[2];
    public static ItemStack[] emptySquaresYellow = new ItemStack[2];

    public static void init(){
        createPorteQuitter();
        createChessEmptySlotItem();
        createChessMenuButton();
        createChessPieces();
        createChessAbandonItems();
        createChessNulleItem();
    }


    private static void createPorteQuitter(){
        ItemStack item = new ItemStack(Material.OAK_DOOR,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"Quitter");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        porteQuitter=item;
    }

    private static void createChessEmptySlotItem(){
        Material mats = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
        String description = ChatColor.GOLD+"";
        ItemStack item = new ItemStack(mats, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(description);
        meta.setLocalizedName("chess_0_0");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        chessEmptySlot=item;
    }
    private static void createChessMenuButton(){
        ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"Défier quelqu'un");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLocalizedName("chess_0_1");
        item.setItemMeta(meta);
        chessMenuButton=item;
    }

    public static ItemStack getChessHeadItem(Player player){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        meta.setDisplayName(ChatColor.GOLD+player.getName());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLocalizedName("chess_0_2");
        item.setItemMeta(meta);
        return item;
    }

    public static void createChessAbandonItems(){
        Material[] mats = {Material.MOJANG_BANNER_PATTERN,Material.SCUTE,Material.BARRIER};
        String[] descript = {ChatColor.GOLD+"Abandonner",ChatColor.GREEN+"Confirmer",ChatColor.RED+"Annuler"};
        for (int i=0;i<3;i++){
            ItemStack item = new ItemStack(mats[i], 1);
            ItemMeta meta=item.getItemMeta();
            meta.setDisplayName(descript[i]);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.setLocalizedName("chess_1_"+i);
            item.setItemMeta(meta);
            chessAbandonItems[i]=item;
        }
    }

    public static void createChessNulleItem(){
        ItemStack item = new ItemStack(Material.POPPY, 1);
        ItemMeta meta=item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE+"Proposer la nulle");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLocalizedName("chess_2");
        item.setItemMeta(meta);
        chessNulleItem=item;
    }



    private static ItemStack getCustomMap(String nomImage, Boolean isCaseBlanc, Boolean isCaseVide, Boolean overlayJaune){                                     //On n'utilise pas celle-là durant la partie car elle prend trop de temps à dessiner
        MapView view = Bukkit.createMap(Logique.chessWorld);
        view.getRenderers().clear();
        TraceurImage traceurImage = new TraceurImage(nomImage,isCaseBlanc,isCaseVide,overlayJaune);
        if (!traceurImage.load(nomImage)){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Erreur dans le chargement de l'image ["+nomImage+"]");
            return null;
        }
        view.addRenderer(traceurImage);
        ItemStack map = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        mapMeta.setMapView(view);
        map.setItemMeta(mapMeta);
        return map;
    }

    private static void createChessPieces(){
        String[] nomsPossibles={"pion","tour","cavalier","fou","dame","roi"};
        int i=0;

        for (String nom:nomsPossibles){
            whiteChessPiecesWhiteSquare[i]=getCustomMap(nom+"_blanc",true,false,false);
            whiteChessPiecesBlackSquare[i]=getCustomMap(nom+"_blanc",false,false,false);
            blackChessPiecesWhiteSquare[i]=getCustomMap(nom+"_noir",true,false,false);
            blackChessPiecesBlackSquare[i]=getCustomMap(nom+"_noir",false,false,false);

            whiteChessPiecesWhiteSquareYellow[i]=getCustomMap(nom+"_blanc",true,false,true);
            whiteChessPiecesBlackSquareYellow[i]=getCustomMap(nom+"_blanc",false,false,true);
            blackChessPiecesWhiteSquareYellow[i]=getCustomMap(nom+"_noir",true,false,true);
            blackChessPiecesBlackSquareYellow[i]=getCustomMap(nom+"_noir",false,false,true);
            i++;
        }
        emptySquares[0]=getCustomMap("roi_blanc",true,true,false);
        emptySquares[1]=getCustomMap("roi_blanc",false,true,false);
        emptySquaresYellow[0]=getCustomMap("roi_blanc",true,true,true);
        emptySquaresYellow[1]=getCustomMap("roi_blanc",false,true,true);


    }

    public static ItemStack getChessPiece(int indexNom,Boolean isPieceBlanc, Boolean isCaseBlanc, Boolean isCaseVide, Boolean overlayJaune){
        if (overlayJaune){
            if (isCaseVide){
                if (isCaseBlanc){
                    return emptySquaresYellow[0];
                }else{
                    return emptySquaresYellow[1];
                }
            }
            if (isPieceBlanc){
                if (isCaseBlanc){
                    return whiteChessPiecesWhiteSquareYellow[indexNom];
                }else{
                    return whiteChessPiecesBlackSquareYellow[indexNom];
                }
            }else{
                if (isCaseBlanc){
                    return blackChessPiecesWhiteSquareYellow[indexNom];
                }else{
                    return blackChessPiecesBlackSquareYellow[indexNom];
                }
            }
        }else{
            if (isCaseVide){
                if (isCaseBlanc){
                    return emptySquares[0];
                }else{
                    return emptySquares[1];
                }
            }
            if (isPieceBlanc){
                if (isCaseBlanc){
                    return whiteChessPiecesWhiteSquare[indexNom];
                }else{
                    return whiteChessPiecesBlackSquare[indexNom];
                }
            }else{
                if (isCaseBlanc){
                    return blackChessPiecesWhiteSquare[indexNom];
                }else{
                    return blackChessPiecesBlackSquare[indexNom];
                }
            }
        }
    }
}
