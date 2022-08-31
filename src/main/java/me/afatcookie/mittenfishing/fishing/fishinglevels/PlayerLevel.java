package me.afatcookie.mittenfishing.fishing.fishinglevels;


import java.util.UUID;

public class PlayerLevel {



    private final UUID playerUUID;

    private  int level;

    private double toNextLevel;

    public PlayerLevel( UUID playerUUID, int level, double toNextLevel) {
        this.playerUUID = playerUUID;
        this.level = level;
        this.toNextLevel = toNextLevel;
    }

    public PlayerLevel(PlayerLevel ob){
        this.playerUUID = ob.playerUUID;
        this.level = ob.level;
        this.toNextLevel = ob.toNextLevel;
    }


    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getLevel() {
        return level;
    }

    public double getToNextLevel() {
        return toNextLevel;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addToProgress(double amount){
        toNextLevel -= amount;
    }

    public void setToNextLevel(double toNextLevel) {
        this.toNextLevel = toNextLevel;
    }
}
