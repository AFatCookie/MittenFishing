package me.afatcookie.mittenfishing.fishing;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.customevents.DiscoverFishEvent;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Fish;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Rarity;
import me.afatcookie.mittenfishing.fishing.fishingquests.CatchFishQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import me.afatcookie.mittenfishing.utils.RandomSelector;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.Random;

/**
 * Loot Pool Class. This class handles the LootPool, and handles giving items from the LootPool. This class also loads the
 * LootPool, getting all the fish from config, and any other items that belong in the LootPool, and put them in the LootPool.
 */
@SuppressWarnings({"ConstantConditions", "unused"})
public class LootPool {

    HashSet<LootItem> lootPool;

    HashSet<ItemStack> allIngredients;

    private final DiscoverableFish discoverableFish;

    private RandomSelector<LootItem> reward;

    private final HashSet<Fish> commonFish;

    private final HashSet<Fish> rareFish;

    private final HashSet<Fish> epicFish;

    private final HashSet<Fish> legendaryFish;

    private final HashSet<Fish> mythicFish;

    private final HashSet<Fish> specialFish;

    private final HashSet<Fish> questFish;

    private final MittenFishing mf;

    private final ConfigManager cm;

    int totalWeight;


    public LootPool(MittenFishing mf) {
        this.mf = mf;
        this.cm = mf.getConfigManager();
        lootPool = new HashSet<>();
        allIngredients = new HashSet<>();
        commonFish = new HashSet<>();
        rareFish = new HashSet<>();
        epicFish = new HashSet<>();
        legendaryFish = new HashSet<>();
        mythicFish = new HashSet<>();
        specialFish = new HashSet<>();
        questFish = new HashSet<>();
        this.discoverableFish = mf.getFishDiscover();
        reload(cm.getFishConfig().getConfig(), discoverableFish);
    }

    //Reloads config, putting any new fish into lootPool
    public void reload(FileConfiguration configuration, DiscoverableFish discoverableFish) {
        lootPool.clear();
        commonFish.clear();
        rareFish.clear();
        epicFish.clear();
        legendaryFish.clear();
        mythicFish.clear();
        specialFish.clear();
        questFish.clear();
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
        sortFishes();
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
        int counter = 0;
            LootItem item = getLoot(getPlayerLootPool(player));
            if (item != null) {
                item.sendMessage(player);
                item.sendSound(player);
                player.getInventory().addItem(item.getItem());
                if (item.getFish() != null) {
                    if (!discoverableFish.playerIsInDiscoverHash(player)) {
                        discoverableFish.addPlayerToHashMap(player);
                    }
                    if (!discoverableFish.hasDiscoveredFish(player, item.getFish())) {
                        Bukkit.getServer().getPluginManager().callEvent(new DiscoverFishEvent(player, item.getFish()));
                    }
                    mf.getLevelManager().updateProgress(player, item.getFish().getFishingXpValue());
                }
                if (mf.getQuestManager().hasActiveCatchQuest(player)) {
                    if (item.getRarity() == Rarity.QUEST || item.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(mf, "quest"), PersistentDataType.STRING)) {
                        for (PlayerQuest playerQuest : mf.getQuestManager().getPlayerCatchQuest(player)) {
                            if (playerQuest.getQuest() instanceof CatchFishQuest && counter <= 0) {
                                if ((((CatchFishQuest) playerQuest.getQuest()).getFishToBeCaught().getFishItem()).equals(item.getItem())) {
                                    playerQuest.updateQuestProgress(1);
                                }
                            } else {
                                counter++;
                            }
                        }
                    }
                }
            }
        Bukkit.getLogger().warning(player.getName() + " wasn't given a reward this is an issue.");

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

    private HashSet<LootItem> getPlayerLootPool(Player player){
        HashSet<LootItem> availableLoot = new HashSet<>();
        for (LootItem lootItem : lootPool) {
            if (lootItem.getFish() != null && lootItem.getFish().getLevel() <= mf.getLevelManager().getPlayerLevel(player).getLevel()){
                availableLoot.add(lootItem);
            }
            if (lootItem.getFish() == null){
                availableLoot.add(lootItem);
            }
        }
        return availableLoot;
    }

    private void sortFishes(){
        for (LootItem item : lootPool){
            if (item.getFish() == null) continue;
            switch (item.getRarity()){
                case COMMON:
                    commonFish.add(item.getFish());
                    return;
                case RARE:
                    rareFish.add(item.getFish());
                    return;
                case EPIC:
                    epicFish.add(item.getFish());
                    return;
                case LEGENDARY:
                    legendaryFish.add(item.getFish());
                    return;
                case MYTHIC:
                    mythicFish.add(item.getFish());
                    return;
                case SPECIAL:
                    specialFish.add(item.getFish());
                    return;
                case QUEST:
                    questFish.add(item.getFish());
                    return;
            }
        }
    }

    public HashSet<Fish> getCommonFish() {
        return commonFish;
    }

    public HashSet<Fish> getRareFish() {
        return rareFish;
    }

    public HashSet<Fish> getEpicFish() {
        return epicFish;
    }

    public HashSet<Fish> getLegendaryFish() {
        return legendaryFish;
    }

    public HashSet<Fish> getMythicFish() {
        return mythicFish;
    }

    public HashSet<Fish> getSpecialFish() {
        return specialFish;
    }

    public HashSet<Fish> getQuestFish() {
        return questFish;
    }

    private void establishRewardLoot(HashSet<LootItem> lootItems){
        try {
            reward = RandomSelector.weighted(lootItems , LootItem::getWeight);
            }catch (Exception e){
            mf.getLogger().warning("Failed to get rewards when fishing!!!");
            e.printStackTrace();
            reward = null;
        }

    }

    private LootItem getLoot(HashSet<LootItem> lootItems){
        establishRewardLoot(lootItems);
        if (reward == null) return null;
        Random random = new Random();
        return reward.next(random);
    }
}
