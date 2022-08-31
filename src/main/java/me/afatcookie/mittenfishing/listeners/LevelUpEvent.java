package me.afatcookie.mittenfishing.listeners;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.customevents.CustomLevelUpEvent;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishinglevels.LevelManager;
import me.afatcookie.mittenfishing.fishing.fishinglevels.PlayerLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/*
Event that should be called whenever a player has enough xp to level up.
 */
public class LevelUpEvent implements Listener {


    private final MittenFishing instance;

    private final ConfigManager cm;

    public LevelUpEvent(MittenFishing instance, ConfigManager cm) {
        this.instance = instance;
        this.cm = cm;
        LevelManager levelManager = instance.getLevelManager();
    }


    @EventHandler
    public void levelUpEvent(CustomLevelUpEvent e){
        PlayerLevel playerLevel = e.getPlayerLevel();
        Player player = e.getPlayer();
        int level = e.getLevel();
        playerLevel.setLevel(level);
        player.sendTitle(cm.getLevelMainTitle().replace("{current_level}", String.valueOf(level)), cm.getLevelSubTitle(), cm.getFadeIn(), cm.getStay(), cm.getFadeOut());
        instance.getDataBase().saveToLevelTable(playerLevel);
    }
}
