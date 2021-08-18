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
    public static ItemStack chessPiece;
    public static ItemStack porteQuitter;
    public static ItemStack chessEmptySlot;
    public static ItemStack chessMenuButton;
    public static ItemStack customMapBlanc;
    public static ItemStack customMapNoir;

    public static void init(){
        createChessPiece();
        createPorteQuitter();
        createChessEmptySlotItem();
        createChessMenuButton();
        createCustomMaps();
    }

    private static void createChessPiece(){
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"Pièce d'échec");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addEnchant(Enchantment.LUCK, 1,false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore=new ArrayList(){{
            add("Une ancienne pièce");
            add("d'échec scintillante");
        }};
        meta.setLore(lore);
        item.setItemMeta(meta);
        chessPiece=item;
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
        meta.setLocalizedName("chess_0");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        chessEmptySlot=item;
    }
    private static void createChessMenuButton(){
        ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"Défier quelqu'un");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLocalizedName("chess_1");
        item.setItemMeta(meta);
        chessMenuButton=item;
    }

    public static ItemStack getChessHeadItem(Player player){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        meta.setDisplayName(ChatColor.GOLD+player.getName());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLocalizedName("chess_2");
        item.setItemMeta(meta);
        return item;
    }


    private static void createCustomMaps(){
        customMapBlanc=getCustomMap("test",true,false);
        customMapNoir=getCustomMap("test",false,false);
    }

    public static ItemStack getCustomMap(String nomImage, Boolean isWhite, Boolean isCaseVide){
        MapView view = Bukkit.createMap(Logique.chessWorld);
        view.getRenderers().clear();
        TraceurImage traceurImage = new TraceurImage(nomImage,isWhite,isCaseVide);
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
}
