package me.afatcookie.mittenfishing.fishing.guimanager;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishingrods.Rod;
import me.afatcookie.mittenfishing.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class RodDisplayGUI implements GUI{

    Inventory inv;

    private final MittenFishing instance;

    private final ConfigManager configManager;

    public RodDisplayGUI(MittenFishing instance) {
        this.instance = instance;
        this.configManager = instance.getConfigManager();
    }
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot) {
        if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(instance, "display"), PersistentDataType.STRING)){
            if (!instance.getRodManager().getRodViaName(clickedItem.getItemMeta().getDisplayName()).hasIngredients()){
                player.sendMessage(ChatColor.RED + "This rod has no ingredients!");
                return;
            }
            player.openInventory(new RecipeDisplayGUI(instance.getRodManager().getRodViaName(clickedItem.getItemMeta().getDisplayName()), instance).getInventory());
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
        int start = 0;
        inv = new InventoryBuilder().createGUIFromConfig(instance.getConfigManager().getGuisConfig().getConfig(), "roddisplay.items", this, "roddisplaygui").build();
        for (Rod rod : instance.getRodManager().getFishingRods()){
            inv.setItem(start, new ItemCreator(rod.getRod()).setPDCString(new NamespacedKey(instance, "display"), "displaypurpose").getItemStack());
            start++;
        }
        return inv;
    }
}
