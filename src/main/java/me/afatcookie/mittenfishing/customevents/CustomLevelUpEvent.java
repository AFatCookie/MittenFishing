package me.afatcookie.mittenfishing.customevents;

import me.afatcookie.mittenfishing.fishing.fishinglevels.PlayerLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomLevelUpEvent extends Event {

    private static final HandlerList handlers = new HandlerList();


    private final Player player;

    private final PlayerLevel playerLevel;

    private final int level;

    public CustomLevelUpEvent(PlayerLevel playerlevel, Player player, int level){
        this.player = player;
        this.playerLevel = playerlevel;
        this.level = level;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerLevel getPlayerLevel() {
        return playerLevel;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
