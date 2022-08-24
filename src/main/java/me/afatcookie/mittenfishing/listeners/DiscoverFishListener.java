package me.afatcookie.mittenfishing.listeners;
import me.afatcookie.mittenfishing.customevents.DiscoverFishEvent;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.DiscoverableFish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DiscoverFishListener implements Listener {

    private final ConfigManager cm;

    private final DiscoverableFish discoverableFish;

    public DiscoverFishListener(ConfigManager cm, DiscoverableFish df){
        this.cm = cm;
        this.discoverableFish = df;
    }

    @EventHandler
    public void onDiscoverFish(DiscoverFishEvent e){
        e.getFisher().sendMessage(cm.getFoundFishMessage().replace("{fish_name}", e.getFish().getName()));
        discoverableFish.addFishToPlayer(e.getFisher(), e.getFish());
    }
}
