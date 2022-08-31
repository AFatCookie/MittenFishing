package me.afatcookie.mittenfishing.fishing.fishinglevels;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.customevents.CustomLevelUpEvent;
import me.afatcookie.mittenfishing.files.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

/*
Level Manager Class. This class allows the user to get their current level, to nextlevel, and more!
 */
public class LevelManager {

    HashSet<PlayerLevel> playerLevels;

    private final MittenFishing instance;

    private final ArrayList<UUID> hasLevels;

    private final ConfigManager cm;

    public LevelManager(MittenFishing instance){
        hasLevels = new ArrayList<>();
        playerLevels = new HashSet<>();
        this.instance = instance;
        cm = instance.getConfigManager();
        reload();
    }

    public void reload(){
        getDataBaseLevels();
    }


    public Player getPlayer(UUID uuid){
        for (PlayerLevel level : playerLevels){
            if (uuid.equals(level.getPlayerUUID())){
                return Bukkit.getPlayer(level.getPlayerUUID());
            }
        }
        return null;
    }

    public UUID getPlayerUUID(Player player){
        for (PlayerLevel level : playerLevels){
            if (player.getUniqueId().equals(level.getPlayerUUID())){
                return level.getPlayerUUID();
            }
        }
        return null;
    }

    public PlayerLevel getPlayerLevel(Player player){
        for (PlayerLevel level : playerLevels){
            if (player.getUniqueId().equals(level.getPlayerUUID())){
                return level;
            }
        }
        return new PlayerLevel(player.getUniqueId(), 0, cm.getStartingAmount());
    }

    public PlayerLevel getUUIDLevel(UUID uuid){
        for (PlayerLevel level : playerLevels){
            if (uuid.equals(level.getPlayerUUID())){
                return level;
            }
        }
        return new PlayerLevel(uuid, 0, cm.getStartingAmount());
    }

    /*
    This forces the players level up by one.
     */
    public void force(Player player){
        Bukkit.getServer().getPluginManager().callEvent(new CustomLevelUpEvent(getPlayerLevel(player), player, getPlayerLevel(player).getLevel() + 1));
    }


    /*
    This updates the progress of the level for the specified player, and if they have enough for the next level it'll level them up.
     */
    public void updateProgress(Player player, double amount){
        getPlayerLevel(player).addToProgress(amount);
        if (getPlayerLevel(player).getToNextLevel() == 0){
            getPlayerLevel(player).setToNextLevel(player.getLevel() * 100);
            Bukkit.getServer().getPluginManager().callEvent(new CustomLevelUpEvent(getPlayerLevel(player), player, getPlayerLevel(player).getLevel() + 1));
            return;
        }else if (getPlayerLevel(player).getToNextLevel() < 0){
            double flippedInt = getPlayerLevel(player).getToNextLevel() * -1;
            getPlayerLevel(player).setToNextLevel((player.getLevel() * 100) - flippedInt);
            Bukkit.getServer().getPluginManager().callEvent(new CustomLevelUpEvent(getPlayerLevel(player), player, getPlayerLevel(player).getLevel() + 1));
            return;
        }
        instance.getDataBase().saveToLevelTable(getPlayerLevel(player));
    }

    public void createPlayerLevelObject(Player player){
        if (hasLevels.contains(player.getUniqueId())){
            return;
        }
        playerLevels.add(new PlayerLevel(player.getUniqueId(), 0, cm.getStartingAmount()));
    }



    private void getDataBaseLevels(){
        playerLevels.clear();
        for (PlayerLevel playerLevel : instance.getDataBase().getPlayerDataFromLevelTable()){
            hasLevels.add(playerLevel.getPlayerUUID());
            playerLevels.add(playerLevel);
        }

    }

    public void saveLevels(){
        if (playerLevels.isEmpty()) return;
        for (PlayerLevel playerLevel : playerLevels){
            instance.getDataBase().saveToLevelTable(playerLevel);
        }
    }
}
