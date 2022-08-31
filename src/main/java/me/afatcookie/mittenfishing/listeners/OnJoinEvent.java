package me.afatcookie.mittenfishing.listeners;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.fishing.DiscoverableFish;
import me.afatcookie.mittenfishing.fishing.fishinglevels.LevelManager;
import me.afatcookie.mittenfishing.fishing.fishingquests.QuestManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinEvent implements Listener {

    private final DiscoverableFish discoverableFish;

    private final QuestManager questManager;

    private final LevelManager levelManager;

    public OnJoinEvent(DiscoverableFish instance, MittenFishing mittenFishing){
        this.discoverableFish = instance;
        this.questManager = mittenFishing.getQuestManager();
        this.levelManager = mittenFishing.getLevelManager();
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e){
        discoverableFish.addPlayerToHashMap(e.getPlayer());
        questManager.onPlayerLoad(e.getPlayer());
        levelManager.createPlayerLevelObject(e.getPlayer());
    }
}
