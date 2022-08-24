package me.afatcookie.mittenfishing.fishing.fishingrods;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Rod {

    private ItemStack rod;
    private final String name;

    private final int timeToFish;

    private final int levelRequirement;

    private final String pathWay;

    private  int weight;

    private  boolean shouldGlow = true;

    private final HashMap<Integer, ItemStack> ingredientWeights;

    private final HashMap<Integer, ItemStack> rodRecipe;

    private final MittenFishing instance;

    public Rod(String name, int timeToFish, int levelRequirement, String pathWay, MittenFishing mf) {
        this.instance = mf;
        this.name = name;
        this.timeToFish = timeToFish;
        this.levelRequirement = levelRequirement;
        this.pathWay = pathWay;
        ingredientWeights = new HashMap<>();
        rodRecipe = new HashMap<>();
        establishRodBuild(mf.getConfigManager().getRodConfig().getConfig());
    }

    /**
     * This massive method creates a fishing rod from the configuration file, and then gets its ingredients and puts them into
     * this instance's ingredients and ingredients + weight hashmap. It allows for toggable insertion into pools, custom items,
     * glows, unbreakability, weight, amounts, and plenty of configurable options to allow the configure to make the ingredients
     * however they like. NOTE: The order that the ingredients are in matter, each time it adds 1 to beginning, which allows me to set
     * the crafting recipe in the custom crafting to number 1-9.
     * @param configuration configuration file to get data from
     * @return returns the fishing rod, adds ingredients to this instance's ingredients,  and if allowed, adds items to ingredients weight map which
     * go into the loot Pool.
     */
    public ItemStack establishRodBuild(FileConfiguration configuration) {
        ItemCreator ic;
        ItemStack ingredient;
        shouldGlow(configuration);
        ic = new ItemCreator(Material.FISHING_ROD, 1).setDisplayName(name).setLore(configuration.getStringList(pathWay + ".lore"));
        applyEffects(ic);
        rod = ic.getItemStack();
        if (configuration.getConfigurationSection(pathWay + ".ingredients") != null){
            for (String itemAccessor : configuration.getConfigurationSection(pathWay + ".ingredients").getKeys(false)) {
                String ingredientsPath = pathWay + ".ingredients" + "." + itemAccessor;
                if (configuration.getString(ingredientsPath + ".name") == null) {
                    ingredient = new ItemCreator(configuration.getString(ingredientsPath + ".material"), configuration.getInt(ingredientsPath + ".amount"))
                            .setUnbreakable(configuration.getBoolean(ingredientsPath + ".unbreakable")).addGlow(configuration.getBoolean(ingredientsPath + ".glowing"))
                            .setLore(configuration.getStringList(ingredientsPath + ".lore")).getItemStack();
                } else {
                    ingredient = new ItemCreator(configuration.getString(ingredientsPath + ".material"), configuration.getInt(ingredientsPath + ".amount"))
                            .setDisplayName(configuration.getString(ingredientsPath + ".name"))
                            .setUnbreakable(configuration.getBoolean(ingredientsPath + ".unbreakable"))
                            .addGlow(configuration.getBoolean(ingredientsPath + ".glowing")).setLore(configuration.getStringList(ingredientsPath + ".lore")).getItemStack();
                }
                instance.getLp().getAllIngredients().add(ingredient);
                weight = configuration.getInt(ingredientsPath + ".weight");
                if (weight <= -1) weight = 1;
                if (!configuration.getIntegerList(ingredientsPath + ".slots").isEmpty()) {
                    for (int slot : configuration.getIntegerList(ingredientsPath + ".slots")) {
                        rodRecipe.put(slot, ingredient);
                    }
                }
                if (configuration.getBoolean(ingredientsPath + ".addtopool") || !configuration.contains(ingredientsPath + ".addtopool")) {
                    ingredientWeights.put(weight, ingredient);
                }
            }
        }else{
            Bukkit.getLogger().log(Level.SEVERE, "Failed to add rods ingredient");
            return rod;
        }
        return rod;
    }

    public ItemStack getRod(){
        return rod;
    }

    /**
     * applies effects to the material. this allows for allowing or disallowing custom effects like glow, unbreakable etc
     * @param ic
     */
    public void applyEffects(ItemCreator ic){
        if (shouldGlow){
            ic.addGlow(true);
        }
    }

    public String getName() {
        return name;
    }

    public int getTimeToFish() {
        return timeToFish;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public Map<Integer, ItemStack> getRecipe(){
        return rodRecipe;
    }

    private void shouldGlow(FileConfiguration configuration){
        if (!configuration.getBoolean(pathWay + ".glowing") || !configuration.contains(pathWay + ".glowing")){
            shouldGlow = false;
        }
        shouldGlow = configuration.getBoolean(pathWay + ".glowing");
    }

    public int getWeight(){
        return weight;
    }

    public HashMap<Integer, ItemStack> getIngredientWeights() {
        return ingredientWeights;
    }

    public boolean hasIngredients(){
        return !rodRecipe.isEmpty();
    }

}
