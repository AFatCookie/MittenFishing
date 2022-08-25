package me.afatcookie.mittenfishing.utils;

import com.github.mittenmc.serverutils.Colors;
import me.afatcookie.mittenfishing.MittenFishing;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility Class. This allows for checking if an item is a Custom fish, get the value of the fish from its PDC, and handles click events
 */
@SuppressWarnings("ConstantConditions")
public class FishUtils {

    public static boolean isFish(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(MittenFishing.getInstance(), "value"), PersistentDataType.DOUBLE);
    }

    public static double getFishValue(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MittenFishing.getInstance(), "value"), PersistentDataType.DOUBLE);
    }


    public static int handleSellingFish(Inventory inventory, ItemStack clickedItem, Player player, int slot, int totalValue) {
        if (isFish(clickedItem) && !inventory.contains(clickedItem)) {
            if (inventory.firstEmpty() > -1) {
                inventory.setItem(inventory.firstEmpty(), clickedItem);
                if (clickedItem.getAmount() > 1) {
                    //Checks if the ItemStack is stacked together, and multiplies the value of one * total amount of ItemStack.
                    totalValue += getFishValue(clickedItem) * clickedItem.getAmount();
                } else {
                    totalValue += getFishValue(clickedItem);
                }
                player.getInventory().setItem(slot, new ItemStack(Material.AIR, 1));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Sell the current inventory to be able to add more fish!"));
            }
            return totalValue;
        } else {
            if (inventory.contains(clickedItem) && isFish(clickedItem)) {
                player.getInventory().addItem(clickedItem);
                inventory.setItem(slot, new ItemStack(Material.AIR, 1));
                if (clickedItem.getAmount() > 1) {
                    //Subtracts the items value * the amount of the ItemStack
                    totalValue -= getFishValue(clickedItem) * clickedItem.getAmount();
                } else {
                    totalValue -= getFishValue(clickedItem);
                }
                return totalValue;
            }
        }
        return totalValue;
    }

    public static int validateIntInConfig(FileConfiguration configuration, String string){
        if (!configuration.contains(string) || configuration.getInt(string) <= -1){
            return 1;
        }
        return  configuration.getInt(string);
    }


    public static String validateStringInConfig(FileConfiguration configuration, String string){
        if (!configuration.contains(string) || configuration.getString(string) == null){
            return "Invalid";
        }
        return  configuration.getString(string);
    }

    public static boolean validateBooleanInConfig(FileConfiguration configuration, String string){
        return configuration.contains(string);
    }

    public static List<String> validateStringListInConfig(FileConfiguration configuration, String string){
        if (!configuration.getStringList(string).isEmpty() || configuration.contains(string)){
            return configuration.getStringList(string);
        }
        return new ArrayList<>();
    }

    public static String colorizeMessage(String message){
        return Colors.conv(message);
    }

}
