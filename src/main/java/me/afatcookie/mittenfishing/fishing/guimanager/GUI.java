package me.afatcookie.mittenfishing.fishing.guimanager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Simple GUI interface that extends InventoryHolder, allowing for one registration of an event listener which works for all guis created implementing this interface.
 */
public interface GUI extends InventoryHolder {

    void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot);
    void onClose(Player player, Inventory inventory);

    void onDrag(Player player, Inventory inventory, ItemStack itemOnCursor);
}
