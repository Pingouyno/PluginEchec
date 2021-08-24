package me.pepelucifer.echecs.objets;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Request {
    private LobbyPlayer sender;
    private LobbyPlayer receiver;
    private int timer;
    private boolean expired;

    public Request(LobbyPlayer sender, LobbyPlayer receiver){
        this.sender=sender;
        this.receiver=receiver;
        this.timer=30;
        this.expired=false;
        startExpirationCountDown();
    }

    public boolean isExpired(){
        return expired;
    }

    public void setExpired(Boolean isExpired){
        expired=isExpired;
    }

    public int getTimeLeft(){
        return timer;
    }

    public LobbyPlayer getSender(){
        return sender;
    }

    public LobbyPlayer getReceiver(){
        return receiver;
    }

    public Request getRequest(){ return this; }

    private void startExpirationCountDown(){
        new BukkitRunnable() {
            public void run() {
                if (timer == 0) {
                    setExpired(true);
                    getReceiver().getInRequests().remove(getRequest());
                    getSender().getOutRequests().remove(getRequest());
                    cancel();
                    return;
                }
                timer--;
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Echecs"), 0L, 20L);
    }
}
