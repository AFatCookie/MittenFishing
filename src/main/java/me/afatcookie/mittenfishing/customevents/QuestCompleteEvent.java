package me.afatcookie.mittenfishing.customevents;

import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/*
This event calls whenever a player completes a quest.
 */
public class QuestCompleteEvent extends Event {


    private static final HandlerList handlers = new HandlerList();

    private final UUID player;

    private final PlayerQuest quest;

    public QuestCompleteEvent(UUID player, PlayerQuest quest){
        this.player = player;
        this.quest = quest;
    }

    public UUID getPlayer() {
        return player;
    }

    public PlayerQuest getQuest() {
        return quest;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
