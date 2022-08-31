package me.afatcookie.mittenfishing.fishing.guimanager;

import me.afatcookie.mittenfishing.MittenFishing;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Main Gui for fishing menu; Area where you can navigate to selling, obtainable fish, and obtainable rods.
 */
public class MainGui implements GUI{

    Inventory inv;

    private final MittenFishing instance;

    public MainGui(MittenFishing instance){
        this.instance = instance;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot) {
        if (inventory.contains(clickedItem) && clickedItem.getItemMeta().getDisplayName().contains("Sell")){
            player.openInventory(new SellGUI(instance, player).getInventory());
        }
        if (inventory.contains(clickedItem) && clickedItem.getItemMeta().getDisplayName().contains("Rods")){
            player.openInventory(new RodDisplayGUI(instance).getInventory());
        }
    }

    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public void onDrag(Player player, Inventory inventory, ItemStack itemOnCursor) {

    }


    @Override
    public Inventory getInventory() {
        inv = new InventoryBuilder().createGUIFromConfig(instance.getConfigManager().getGuisConfig().getConfig(), "maingui.items", this, "maingui").build();
        return inv;
    }
}
