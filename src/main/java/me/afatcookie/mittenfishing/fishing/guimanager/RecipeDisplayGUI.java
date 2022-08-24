package me.afatcookie.mittenfishing.fishing.guimanager;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.fishing.fishingrods.Rod;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RecipeDisplayGUI implements GUI{

    Inventory inv;

    Rod rod;

    MittenFishing instance;

    public RecipeDisplayGUI(Rod rod, MittenFishing instance){
        this.rod = rod;
        this.instance = instance;
    }
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot) {
        if (clickedItem.getItemMeta().getDisplayName().equals(instance.getConfigManager().getStandardBackButton().getItemMeta().getDisplayName())){
            player.openInventory(new RodDisplayGUI(instance).getInventory());
        }
        return;
    }

    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public void onDrag(Player player, Inventory inventory, ItemStack itemOnCursor) {
        return;
    }

    @Override
    public Inventory getInventory() {
        this.inv = new InventoryBuilder().createGUIFromConfig(instance.getConfigManager().getGuisConfig().getConfig(), "recipedisplaygui.items", this, "recipedisplaygui").build();
        inv.setItem(10, new ItemStack(Material.AIR, 1));
        inv.setItem(11, new ItemStack(Material.AIR, 1));
        inv.setItem(12, new ItemStack(Material.AIR, 1));
        inv.setItem(19, new ItemStack(Material.AIR, 1));
        inv.setItem(20, new ItemStack(Material.AIR, 1));
        inv.setItem(21, new ItemStack(Material.AIR, 1));
        inv.setItem(25, rod.getRod());
        inv.setItem(28, new ItemStack(Material.AIR, 1));
        inv.setItem(29, new ItemStack(Material.AIR, 1));
        inv.setItem(30, new ItemStack(Material.AIR, 1));
        inv.setItem(36, instance.getConfigManager().getStandardBackButton());
        for (Map.Entry<Integer, ItemStack> recipeMap : rod.getRecipe().entrySet()){
            this.inv.setItem(recipeMap.getKey(), recipeMap.getValue());
        }

        return inv;
    }
}
