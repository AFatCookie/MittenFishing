package me.afatcookie.mittenfishing.fishing;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Fish;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Loot Item Class. This classes purpose is to create a loot item object, which then can be added to the LootPool. It's done
 * like this since the LootPool is not only fish, so by having it this way you can make the pool diverse in items, rather
 * than being stuck to fish.
 */
public class LootItem {


    private final ConfigManager cm;

    private final int weight;
    private  Rarity rarity;
    private ItemStack itemStack;
    private Fish fish;

    public LootItem(Rarity rarity, int weight,  Fish fish, MittenFishing mf) {
        this.cm = mf.getConfigManager();
        this.rarity = rarity;
        this.weight = weight;
        this.fish = fish;
        this.itemStack = fish.getFishItem();
    }

    public LootItem(int weight, ItemStack itemStack, MittenFishing mf){
        this.weight = weight;
        this.itemStack = itemStack;
        this.cm = mf.getConfigManager();
    }

    public LootItem(Rarity rarity, int weight, ItemStack fishItem, MittenFishing mf){
        this.rarity =rarity;
        this.weight = weight;
        this.itemStack = fishItem;
        this.cm = mf.getConfigManager();
    }

    public void sendMessage(Player player){
            player.sendMessage(cm.getRecievedFishMessage().replace("{fish_name}", itemStack.getItemMeta().getDisplayName()));
    }

    public void sendSound(Player player){
        if (!cm.isFishSound()) return;
        player.playSound(player.getLocation(), cm.getFishedSound(), cm.getFishedYaw(), cm.getFishedPitch());
    }

    public void tryBroadCastMessage() {
        if (fish != null) {
            Bukkit.broadcastMessage(fish.getBroadCastMessage());
        }
    }


    public Rarity getRarity() {
        return rarity;
    }

    public int getWeight() {
        return weight;
    }


    public ItemStack getItem() {
        if (fish ==null){
            return itemStack;
        }else{
            return fish.getFish(cm.getFishConfig().getConfig());
        }
    }

    public void setItem(ItemStack item) {
        this.itemStack = item;
    }

    //Returns fish object if not null
    public Fish getFish() {
        if (fish == null) return null;
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }
}
