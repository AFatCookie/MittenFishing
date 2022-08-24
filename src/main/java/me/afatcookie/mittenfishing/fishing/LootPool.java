package me.afatcookie.mittenfishing.fishing;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.customevents.DiscoverFishEvent;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Fish;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Rarity;
import me.afatcookie.mittenfishing.fishing.fishingquests.CatchFishQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * Loot Pool Class. This class handles the LootPool, and handles giving items from the LootPool. This class also loads the
 * LootPool, getting all the fish from config, and any other items that belong in the LootPool, and put them in the LootPool.
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public class LootPool {

    HashSet<LootItem> lootPool;

    HashSet<ItemStack> allIngredients;

    private final DiscoverableFish discoverableFish;

    private final MittenFishing mf;

    private final ConfigManager cm;

    int totalWeight;


    public LootPool(MittenFishing mf) {
        this.mf = mf;
        this.cm = mf.getConfigManager();
        lootPool = new HashSet<>();
        allIngredients = new HashSet<>();
        this.discoverableFish = mf.getFishDiscover();
        reload(cm.getFishConfig().getConfig(), discoverableFish);
    }

    //Reloads config, putting any new fish into lootPool
    public void reload(FileConfiguration configuration, DiscoverableFish discoverableFish) {
        lootPool.clear();
        discoverableFish.clearFish();
        getFishFromConfig("fishes.common", configuration);
        getFishFromConfig("fishes.rare", configuration);
        getFishFromConfig("fishes.epic", configuration);
        getFishFromConfig("fishes.legendary", configuration);
        getFishFromConfig("fishes.mythic", configuration);
        getFishFromConfig("fishes.special", configuration);
        for (Rarity rarity : Rarity.values()) {
            totalWeight += rarity.getWeight();
        }
    }

    /**
     * gets the fish from the desired configuration, and the section of each rarity of fish
     * @param section rarity of fish
     * @param configuration configuration file to get fish from
     */
    private void getFishFromConfig(String section, FileConfiguration configuration){
        if (configuration.getConfigurationSection(section) != null){
            for (String itemAccessor : configuration.getConfigurationSection(section).getKeys(false)){
                String path = section + "." + itemAccessor;
                switch (section){
                    case "fishes.common":
                        createFish(path, Rarity.COMMON, configuration);
                        break;
                    case "fishes.rare":
                        createFish(path, Rarity.RARE, configuration);
                        break;
                    case "fishes.epic":
                        createFish(path, Rarity.EPIC, configuration);
                        break;
                    case "fishes.legendary":
                        createFish(path, Rarity.LEGENDARY, configuration);
                        break;
                    case "fishes.mythic":
                        createFish(path, Rarity.MYTHIC, configuration);
                        break;
                    case "fishes.special":
                        createFish(path, Rarity.SPECIAL, configuration);
                        break;
                }
            }
        }
    }

    public boolean itemStackInLootPool(ItemStack itemStack){
        for (LootItem item : lootPool){
            if (item.getItem().equals(itemStack)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gives the loot directly to the player, rather than dropping and reeling in. This will get a random weighted object,
     * and then get that Lootitem and send its message and sound which are configurable in message config. WEIGHT SYSTEM
     * WIP SO DON"T WORRY, The current one is just temp.
     *
     * @param player the player to give the loot too
     */
    //TODO
    public void giveLootDirectly(Player player) {
        int random = (int) (Math.random() * totalWeight);
        for (LootItem item : lootPool) {
            ItemStack itemFromLoot = item.getItem();
            int weight = item.getWeight();
            if (random > weight) continue;
            item.sendMessage(player);
            item.sendSound(player);
            player.getInventory().addItem(itemFromLoot);
            if (item.getFish() != null) {
                if (!discoverableFish.playerIsInDiscoverHash(player)) {
                    discoverableFish.addPlayerToHashMap(player);
                }
                if (!discoverableFish.hasDiscoveredFish(player, item.getFish())) {
                    Bukkit.getServer().getPluginManager().callEvent(new DiscoverFishEvent(player, item.getFish()));
                }
                if (mf.getQuestManager().hasActiveCatchQuest(player)) {
                    if (item.getRarity() == Rarity.QUEST || item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(mf, "quest"), PersistentDataType.STRING)) {
                        for (PlayerQuest playerQuest : mf.getQuestManager().getPlayerCatchQuest(player)){
                            if (playerQuest.getQuest() instanceof CatchFishQuest){
                                if ((((CatchFishQuest) playerQuest.getQuest()).getFishToBeCaught()).equals(item.getFish())){
                                    playerQuest.updateQuestProgress(1);
                                }
                            }
                        }
                    }
                }
            }
            return;

        }
        Bukkit.getLogger().warning(player.getName() + " wasn't given a reward this is an issue.");
    }

    /**
     * Drops the loot as if it were normal vanilla, and reels it towards the player. Currently, A WIP, but this would be
     * its purpose.
     *
     * @param player player to reel item towards
     * @return The Itemstack that will drop
     */
    //TODO
    public ItemStack dropLoot(Player player) {
        int random = (int) (Math.random() * totalWeight);
        for (LootItem item : lootPool) {
            ItemStack itemFromLoot = item.getItem();
            int weight = item.getWeight();
            if (random > weight) continue;
            item.sendMessage(player);
            item.sendSound(player);
            player.getInventory().addItem(itemFromLoot);
            if (item.getFish() != null) {
                if (!discoverableFish.playerIsInDiscoverHash(player)) {
                    discoverableFish.addPlayerToHashMap(player);
                }
                if (!discoverableFish.hasDiscoveredFish(player, item.getFish())) {
                    Bukkit.getServer().getPluginManager().callEvent(new DiscoverFishEvent(player, item.getFish()));
                }
            }
            return itemFromLoot;
        }
        Bukkit.getLogger().warning(player.getName() + " wasn't given a reward this is an issue.");
        return new ItemStack(Material.TROPICAL_FISH, 1);
    }

    public HashSet<LootItem> getLootPool(){
        return lootPool;
    }

    public HashSet<ItemStack> getAllIngredients() {
        return allIngredients;
    }

    /**
     * Creates a new Fish Object, and puts it into the lootPool, also creating a new LootItem Object. It also adds the fish
     * to the discoverable Fish.
     * @param path pathway to access data from configuration
     * @param rarity rarity of fish to set data for LootItem and lootPool
     */

    private void createFish(String path, Rarity rarity, FileConfiguration configuration){
        String name = cm.getFishName(path);
        double sellValue = cm.getFishSellValue(path);
        double xpValue = cm.getFishXpValue(path);
        Fish fish = new Fish(rarity, name,
                sellValue,xpValue, path, mf);
        lootPool.add(new LootItem(fish.getRarity(), fish.getRarity().getWeight(), fish, mf));
        discoverableFish.addFishToTotalFish(fish);
    }
}
