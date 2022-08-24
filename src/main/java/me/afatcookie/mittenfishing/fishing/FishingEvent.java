package me.afatcookie.mittenfishing.fishing;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Fishing Event Class. This class handles fishing events; Therefore, it'll distribute LootPool Items, handle hook events
 * and really anything related to the act of fishing
 */
public class FishingEvent implements Listener {

    private final MittenFishing instance;

    private final ConfigManager cm;

    public FishingEvent(MittenFishing instance){
        this.instance = instance;
        this.cm = instance.getConfigManager();
    }

    @EventHandler
    public void onFishEvent(PlayerFishEvent e){
        final Player player = e.getPlayer();
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (Math.random() > cm.getRegularDrop()) {
                e.setCancelled(false);
                e.setExpToDrop(0);
                return;
            }
            e.setCancelled(true);
            e.setExpToDrop(0);
            e.getHook().remove();
            instance.getLp().giveLootDirectly(player);
            }
        }
    }
