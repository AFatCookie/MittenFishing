package me.afatcookie.mittenfishing.fishing.guimanager;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishingrods.Rod;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FishingRodCraftingGUI implements GUI {

    Inventory inv;

    private final MittenFishing instance;

    private final Map<Integer, ItemStack> potentialCraft;

    private final ConfigManager configManager;


    public FishingRodCraftingGUI(MittenFishing instance) {
        this.instance = instance;
        this.configManager = instance.getConfigManager();
        potentialCraft = new HashMap<>();
    }

    /**
     * This is very overkill For custom crafting, as it checks the menu multiple times for the same things, however everytime
     * you take out a piece, something else breaks, so it works like this, so I left it like this.
     * @param player Player in inventory.
     * @param inventory inventory of player
     * @param clickedItem item that player clicked
     * @param clickType type of click player has done
     * @param slot slot of clicked item or clicked part of menu.
     */
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot) {
        if (slot == 25 && isNotGlass(inventory, slot)){
            if (clickedItem.getType() == Material.FISHING_ROD){
                Rod rod = instance.getRodManager().getRodViaName(clickedItem.getItemMeta().getDisplayName());
                for (Map.Entry<Integer, ItemStack> rodRecipe : rod.getRecipe().entrySet()){
                    inventory.getItem(rodRecipe.getKey()).setAmount(inventory.getItem(rodRecipe.getKey()).getAmount() - rodRecipe.getValue().getAmount());
                }
            }
            player.getInventory().addItem(clickedItem);
            player.closeInventory();
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            if (isNotGlass(inventory, i) && inventory.getItem(i).getType() != Material.EMERALD_BLOCK) {
                potentialCraft.put(i, inventory.getItem(i));
            }
        }
        if (slot == 23 && clickedItem.getType() == Material.EMERALD_BLOCK){
            boolean isValid = false;
        for (Rod rod : instance.getRodManager().getFishingRods()){
            for (Map.Entry<Integer, ItemStack> loopRecipe : rod.getRecipe().entrySet()) {
                for (Map.Entry<Integer, ItemStack> loopRecipe2 : potentialCraft.entrySet()) {
                    isValid = loopRecipe2.getKey().equals(loopRecipe.getKey());
                }
            }
            if (isValid){
                if (potentialCraft.keySet().equals(rod.getRecipe().keySet())) {
                    if (potentialCraft.entrySet().stream().allMatch(e -> e.getValue().getAmount() >= rod.getRecipe().get(e.getKey()).getAmount())) {
                        if (potentialCraft.entrySet().stream().allMatch(e -> e.getValue().getType() == rod.getRecipe().get(e.getKey()).getType())) {
                            inv.setItem(25, rod.getRod());
                            return;
                        }
                    }
                    }
                }
                }
            }else {
            if (isNotGlass(inventory, slot)) {
                inv.setItem(25, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
                potentialCraft.clear();
            }
        }
        }

    @Override
    public void onClose(Player player, Inventory inventory) {
        if (inventory.isEmpty()){
            potentialCraft.clear();
            return;
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            for (int j = 0; j < instance.getRodManager().getFishingRods().size(); j++) {
                if (isNotGlass(inventory, i) && inventory.getItem(i) != instance.getRodManager().getFishingRods().get(j).getRod() && inventory.getItem(i).getType() != Material.EMERALD_BLOCK) {
                    player.getInventory().addItem(inventory.getItem(i));
                }
            }
        }
        potentialCraft.clear();
    }

    @Override
    public void onDrag(Player player, Inventory inventory, ItemStack itemOnCursor) {
        if (potentialCraft.containsValue(itemOnCursor)){
            potentialCraft.clear();
        }
    }


    @Override
    public Inventory getInventory() {
        inv = new InventoryBuilder().createGUIFromConfig(instance.getConfigManager().getGuisConfig().getConfig(), "rodcraftgui.items", this, "rodcraftgui").build();
        inv.setItem(10, new ItemStack(Material.AIR, 1));
        inv.setItem(11, new ItemStack(Material.AIR, 1));
        inv.setItem(12, new ItemStack(Material.AIR, 1));
        inv.setItem(19, new ItemStack(Material.AIR, 1));
        inv.setItem(20, new ItemStack(Material.AIR, 1));
        inv.setItem(21, new ItemStack(Material.AIR, 1));
        inv.setItem(23, new ItemStack(Material.EMERALD_BLOCK, 1));
        inv.setItem(25, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
        inv.setItem(28, new ItemStack(Material.AIR, 1));
        inv.setItem(29, new ItemStack(Material.AIR, 1));
        inv.setItem(30, new ItemStack(Material.AIR, 1));
        return inv;
    }

    private boolean isNotGlass(Inventory inventory, int slot){
      return  inventory.getItem(slot) != null && inventory.getItem(slot).getType() != Material.AIR && !inventory.getItem(slot).getType().toString().contains("PANE");
    }

    private boolean compareMaps(Map<Integer, ItemStack> map1, Map<Integer, ItemStack> map2){
        if (map1 == null || map2 == null){
            return false;
        }
        for (int i : map1.keySet()){
            if (!map1.get(i).equals(map2.get(i))){
                return false;
            }
            for (int j : map2.keySet()){
                if (!map2.get(j).equals(map1.get(j))){
                    return false;
                }

            }
        }
        return true;
    }
}
