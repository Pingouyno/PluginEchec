package me.pepelucifer.echecs.custommaps;

import me.pepelucifer.echecs.Echecs;
import me.pepelucifer.echecs.logique.Logique;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import javax.swing.*;
import java.awt.*;

public class TraceurImage extends MapRenderer {
    private Image image;
    private boolean done;
    private boolean white;
    private boolean caseVide;

    public TraceurImage(){
        done=false;
    }

    public TraceurImage(String nomFichier, Boolean isWhite, Boolean isCaseVide){
        load(nomFichier);
        done=false;
        white=isWhite;
        caseVide=isCaseVide;
    }

    public boolean load(String nomFichier){
        Image image=null;
        try{
            image=new ImageIcon(Echecs.class.getClassLoader().getResource(nomFichier+".png")).getImage();
            image = MapPalette.resizeImage(image);
        }catch(Exception e){
            return false;
        }
        this.image=image;
        return true;
    }


    @Override
    public void render(MapView view, MapCanvas canvas, Player p) {
        if (done){
            return;
        }
        canvas.drawImage(0,0,image);
        Byte color;
        if (white){
            color=MapPalette.matchColor(Color.white);
        }else{
            color=MapPalette.matchColor(Color.BLACK);
        }

        for (int y=0;y<128;y++){
            for (int x=0;x<128;x++){
                if (canvas.getPixel(x,y)==0 || caseVide){                                                                           //enlever le OR peut-être
                    canvas.setPixel(x,y,color);
                }
            }
        }

        view.setLocked(true);
        view.setTrackingPosition(false);
        done=true;
    }
}
