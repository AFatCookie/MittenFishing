package me.afatcookie.mittenfishing.fishing.fishingrods;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.LootItem;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Map;

public class RodManager {

    private final ArrayList<Rod> fishingRods;

    private final MittenFishing instance;

    private final ConfigManager cm;

    public RodManager(MittenFishing instance, ConfigManager cm) {
        this.instance = instance;
        this.cm = cm;
        fishingRods = new ArrayList<>();
        reload(cm.getRodConfig().getConfig());
    }


    /**
     * Clears the fishing rods from current ArrayList, and refreshes them from config
     * @param configuration configuration file to refresh from
     */
    public void reload(FileConfiguration configuration) {
        fishingRods.clear();
        getRodFromConfigSection(configuration);
    }

    /**
     * Gets A Fishing rod and ingredients from configuration file. It also inserts the ingredients into the lootPool with
     * the desired weight, however this feature is toggable for each item in config.
     * @param configuration configuration to check from
     */
    private void getRodFromConfigSection(FileConfiguration configuration) {
        if (configuration.getConfigurationSection("fishingrods") != null) {
            for (String itemAccessor : configuration.getConfigurationSection("fishingrods").getKeys(false)) {
                String path = "fishingrods" + "." + itemAccessor;
                String name = cm.getRodName(path);
                int timeToFish =  cm.getRodTime(path);
                int levelReq = cm.getLevelReq(path);
                Rod rod = new Rod(name, timeToFish, levelReq, path, instance);
                fishingRods.add(rod);
                for (Map.Entry<Integer, ItemStack> ingredientsWeight : rod.getIngredientWeights().entrySet()){
                    instance.getLootPool().getLootPool().add(new LootItem(ingredientsWeight.getKey(), ingredientsWeight.getValue(), instance));
                }
            }
        }
    }

    /**
     * Get the parameterized fishing rod from the fishing rod array list.
     * @param rod fishing rod to look for.
     * @return null if not found; Otherwise specified Rod.
     */
    public Rod getRod(Rod rod){
        for (Rod rod1 : fishingRods){
            if (rod == rod1){
                return rod1;
            }
        }
        return null;
    }

    /*
    ALlows me to check if the itemStack is from an ingredient of a fishingRod.
     */
    public boolean isAnIngredient(ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(instance, "ingredient"), PersistentDataType.STRING);
    }

    /*
    Allows me to get a fishing rod via its name. this method should only be used in guis and such. Anywhere where the rod
    name will NOT change. etc. of bad place, (Player inventory because of anvils.)
     */
    public Rod getRodViaName(String name){
        for (Rod rod : fishingRods){
            if (rod.getName().equals(name)){
                return rod;
            }
        }
        return fishingRods.get(0);
    }

    /*
    gets fishing rods array list.
     */
    public ArrayList<Rod> getFishingRods() {
        return fishingRods;
    }
}
