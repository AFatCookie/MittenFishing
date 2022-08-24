package me.afatcookie.mittenfishing.listeners;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.utils.FishUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * This disables the option for regular players that aren't operators to eat or place the fish. Unless you guys want to
 * refund them of course it can be disabled.
 */
public class DisableFishTampering implements Listener {

     private final String errorMessage;

     private final MittenFishing instance;
     private final ConfigManager cm;

    public DisableFishTampering(MittenFishing mf){
        this.instance = mf;
        this.cm = mf.getConfigManager();
        errorMessage = cm.getTamperingFishMessage();
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (e.getPlayer().isOp()) return;
        if (instance.getLp().itemStackInLootPool(e.getItemInHand()) || instance.getRodManager().isAnIngredient(e.getItemInHand())){
                e.setCancelled(true);
                e.getPlayer().sendMessage(errorMessage);
            }
        }

        @EventHandler
    public void onEatEvent(PlayerItemConsumeEvent e){
        if (e.getPlayer().isOp()) return;
        if (instance.getLp().itemStackInLootPool(e.getItem()) || instance.getLp().itemStackInLootPool(e.getItem())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(errorMessage);
        }
        }
    }
