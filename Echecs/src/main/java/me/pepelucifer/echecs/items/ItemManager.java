package me.pepelucifer.pluginechec.items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static ItemStack chessPiece;

    public static void init(){
        createChessPiece();
    }

    public static void createChessPiece(){
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
}
