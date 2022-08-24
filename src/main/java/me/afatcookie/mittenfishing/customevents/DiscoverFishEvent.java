package me.afatcookie.mittenfishing.customevents;
import org.bukkit.event.Event;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/*
This event is called whenever a player discovers a fish when giving item in lootPool.
 */
public class DiscoverFishEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player fisher;
    private final Fish fish;

    public DiscoverFishEvent(Player fisher, Fish caughtFish){
        this.fish = caughtFish;
        this.fisher = fisher;
    }

    public Player getFisher() {
        return fisher;
    }

    public Fish getFish() {
        return fish;
    }

    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}
