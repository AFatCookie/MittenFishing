package me.afatcookie.mittenfishing.utils;

import com.github.mittenmc.serverutils.Colors;
import me.afatcookie.mittenfishing.MittenFishing;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility Class. This allows for checking if an item is a Custom fish, get the value of the fish from its PDC, and handles click events
 */
@SuppressWarnings("ALL")
public class FishUtils {

    public static boolean isFish(ItemStack itemStack){
        if (itemStack == null || !itemStack.hasItemMeta()) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(MittenFishing.getInstance(), "value"), PersistentDataType.DOUBLE);
    }

    public static double getFishValue(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MittenFishing.getInstance(), "value"), PersistentDataType.DOUBLE);
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
