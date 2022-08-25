package me.afatcookie.mittenfishing.fishing.guimanager;
import me.afatcookie.mittenfishing.utils.FishUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

/**
 * Handles whenever a player clicks while in a GUI that is created and implements GUI interface. Yes I know that
 * inventory holder was not implemented for custom guis, however I've been creating guis like this from the beginning,
 * and if you'd like me to change to hashmap way I will do so.
 */
public class GUIClickListener implements Listener {

    @EventHandler
    public static void onClick(InventoryClickEvent e){
        if (!(e.getInventory().getHolder() instanceof GUI)){
            return;
        }
        if (e.getCurrentItem() == null){
            return;
        }
        final GUI getGUI = (GUI) e.getInventory().getHolder();
        if (e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem() == null || e.getCurrentItem().getType().toString().contains("PANE")) {
            e.setCancelled(true);
        }

        if (getGUI instanceof MainGui){
            e.setCancelled(true);
        }
        if (getGUI instanceof SellGUI){
            if (!FishUtils.isFish(e.getCurrentItem())){
                e.setCancelled(true);
            }
        }
        if (getGUI instanceof FishingRodCraftingGUI){
            if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK){
                e.setCancelled(true);
            }
        }

        if (getGUI instanceof RodDisplayGUI){
            e.setCancelled(true);
        }

        if (getGUI instanceof RecipeDisplayGUI){
            e.setCancelled(true);
        }
        getGUI.onClick((Player) e.getWhoClicked(), e.getInventory(), e.getCurrentItem(), e.getClick(), e.getSlot());
    }

    @EventHandler
    public static void onDrag(InventoryDragEvent e){
        if (!(e.getInventory().getHolder() instanceof GUI)){
            return;
        }

        final GUI getGUI = (GUI) e.getInventory().getHolder();
        getGUI.onDrag((Player) e.getWhoClicked(), e.getInventory(), e.getCursor());
    }

    @EventHandler
    public static  void onClose(InventoryCloseEvent e){
        try {
            if (!(e.getInventory().getHolder() instanceof GUI)) return;
            final GUI getGUI = (GUI) e.getInventory().getHolder();
            getGUI.onClose((Player) e.getPlayer(), e.getInventory());
        }catch (NoClassDefFoundError exception){
            return;
        }
    }
}
