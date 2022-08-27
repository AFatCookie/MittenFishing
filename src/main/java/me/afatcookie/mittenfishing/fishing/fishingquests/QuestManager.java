package me.afatcookie.mittenfishing.fishing.fishingquests;

import com.github.mittenmc.serverutils.RepeatingTask;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.LootItem;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Fish;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class QuestManager {

    private final ArrayList<Quest> quests;

    private final ArrayList<Quest> activeQuests;

    private final MittenFishing instance;

    private final ConfigManager configManager;

    private final ArrayList<UUID> hadQuests;

    private final HashSet<PlayerQuest> playersQuest;

    private  long day;

    /**
     * This clears initializes all the data holding for the quests, reloads the quests from config, sets new daily quests,
     * assigns them to the current players online, and starts the timer which will reset the quests at 8pm every day.
     *
     * @param instance instance of plugin.
     */
    public QuestManager(MittenFishing instance) {
        this.instance = instance;
        this.configManager = instance.getConfigManager();
        this.quests = new ArrayList<>();
        activeQuests = new ArrayList<>();
        this.playersQuest = new HashSet<>();
        this.hadQuests = new ArrayList<>();
        this.day = instance.getConfigManager().getDay();
        restoreDailyQuestData();
        startNewDayChecker();
    }


    /**
     * This clears all the quests, and then adds them all from quest configuration, allowing them to be picked when getting
     * new daily quests.
     *
     * @param configuration configuration to get quests from
     * @param section       section of configuration to look in
     */
    public void reloadQuests(FileConfiguration configuration, String section) {
        quests.clear();
        if (configuration.getConfigurationSection(section) != null) {
            for (String questAccessor : configuration.getConfigurationSection(section).getKeys(false)) {
                String path = section + "." + questAccessor;
                createQuest(configuration, path);
            }
        }
    }


    /**
     * This will create a quest with some standard values, and nonstandard values.
     * VALUES THAT APPLY TO EVERY QUEST
     * Name - Quest name
     * Description - description of what to do for quest
     * moneyValue - value of quest when completed.
     * xpValue - xp given when completed quest
     * When creating catching fish quests, you must create the fish within the config that needs to be caught. If you want
     * to use fish already made, you have to copy it from the fish config and put it into here.
     * You must also provide a sell and xp value to it. If you want it to be the same for every quest fish, the option is there,
     * however all you have to do is set it for each fish the value of quest fish.
     * <p>
     * When creating sell quests, you must provide the amount the player must make.
     *
     * @param configuration configuration to look into for quests,
     * @param path          pathway to follow
     */
    public void createQuest(FileConfiguration configuration, String path) {
        Quest quest;
        String name = configManager.getQuestName(path);
        List<String> description = configManager.getQuestDescription(path);
        double moneyValue = configManager.getQuestRewardValue(path);
        double xpValue = configManager.getQuestXpValue(path);
        String questType = configManager.getQuestType(path);
        int questId = configManager.getQuestID(path);
        int amount = configManager.getQuestAmountToBeCaught(path);
        switch (questType) {
            case "catchfish":
                if (configuration.getConfigurationSection(path + ".questfish") != null) {
                    Fish fish = null;
                    for (String itemAccessor : configuration.getConfigurationSection(path + ".questfish").getKeys(false)) {
                        Rarity rarity;
                        String path2 = path + ".questfish" + "." + itemAccessor;
                        String fishName = configuration.getString(path2 + ".name");
                        double fishSellValue = configuration.getDouble(path2 + ".sellvalue");
                        double fishXpValue = configuration.getDouble(path2 + ".xpvalue");
                        if (!configuration.contains(path2 + ".rarity") || configuration.getString(path2 + ".rarity") == null) {
                            fish = new Fish(Rarity.QUEST, fishName, fishSellValue, fishXpValue, path2, instance);
                        } else {
                            rarity = configManager.getRarity(configuration.getString(path2 + ".rarity"));
                            fish = new Fish(rarity, fishName, fishSellValue, fishXpValue, path2, instance);
                        }
                        fish.getFish(configuration).getItemMeta().getPersistentDataContainer().set(new NamespacedKey(instance, "quest"), PersistentDataType.STRING, "TRUE");
                        fish.setFishingXpValue(fishXpValue, configuration);
                        fish.setSellValue(fishSellValue, configuration);
                        instance.getLp().getLootPool().add(new LootItem(fish.getRarity(), fish.getRarity().getWeight(), fish.getFishItem(), instance));
                    }
                    quest = new CatchFishQuest(name, description, moneyValue, xpValue, fish, questId, amount);
                    quests.add(quest);
                    System.out.println("Created Quest: " + quest.getName());
                }
                break;
            case "sell":
                double amountToMake = configManager.getAmountToMake(path);
                quest = new SellQuest(name, description, moneyValue, xpValue, questId, amountToMake);
                quests.add(quest);
                System.out.println("Created Quest: " + quest.getName());
                break;

        }
    }

    /**
     * returns all quests
     *
     * @return all quests created
     */
    public ArrayList<Quest> getQuests() {
        return quests;
    }

    /**
     * This method will clear the active quests, the players who've already had quests/finished quests, and all current player
     * quests/ removing progress. It will then reload the lootpool, just to make sure it removes the quests fish from the lootPool.
     * It will then loop through and get random quests to set active for the day. if any quests are catching fish quests, it will
     * get the fish from that quest, and add it to the lootpool with its rarity being used for weight. If the amount of quests
     * is smaller than three, it will just add any available.
     *
     * @param quests all the quests to choose from
     */
    public void getNewDailies(ArrayList<Quest> quests) {
        activeQuests.clear();
        hadQuests.clear();
        playersQuest.clear();
        instance.getLp().reload(instance.getFc().getConfig(), instance.getFishDiscover());
        if (quests.isEmpty()) return;
        if (quests.size() >= 3) {
            while (activeQuests.size() < 3) {
                Random random = new Random();
                int randomNumber = random.nextInt(quests.size());
                if (!activeQuests.contains(quests.get(randomNumber))) {
                    activeQuests.add(quests.get(randomNumber));
                    instance.getDb().createQuestTable(quests.get(randomNumber));
                    if (quests.get(randomNumber) instanceof CatchFishQuest) {
                        Fish fishToBeCaught = ((CatchFishQuest) quests.get(randomNumber)).getFishToBeCaught();
                        instance.getLp().getLootPool().add(new LootItem(fishToBeCaught.getRarity(), fishToBeCaught.getRarity().getWeight(), fishToBeCaught.getFishItem(), instance));
                    }
                }
            }
        } else {
            activeQuests.addAll(quests);
        }
    }
    private void startNewDayChecker() {
        new RepeatingTask(instance, 20, 20) {
            @Override
            public void run() {
                if (day != System.currentTimeMillis() / 86400000) {
                    day = System.currentTimeMillis() / 86400000;
                    getNewDailies(quests);
                    assignQuestToOnline();
                }
            }
        };
    }

    /**
     * To be called when the player joins to determine if their daily quests should reset.
     * If so, their daily quests will be reset.
     * @param loadedPlayer The player who joined
     */
    public void onPlayerLoad(Player loadedPlayer) {
        //If the last day the player joined was not today
        if (loadedPlayer.getLastPlayed() / 86400000 != System.currentTimeMillis() / 86400000) {
            assignPlayerQuests(loadedPlayer.getUniqueId());
        }
    }

    private void addToDailies(Quest quest){
        if (!activeQuests.contains(quest)){
            activeQuests.add(quest);
        }
    }

    /**
     * gets the player quests set. PlayerQuest is what allows it to be "per-player".
     *
     * @return Set of playersQuest
     */
    public HashSet<PlayerQuest> getPlayersQuest() {
        return playersQuest;
    }

    /**
     * This method gets the player quest if it is an instance of SellQuest, and the playerquest is occupied by the player's
     * UUID. Although not needed, you should check if the player has any Active Sell Quest to ensure it gets them.
     *
     * @param player Player to check for Sell Quests
     * @return ArrayList of sell quests the player has active.
     */
    public ArrayList<PlayerQuest> getPlayerSellQuest(Player player) {
        ArrayList<PlayerQuest> playersQuests = new ArrayList<>();
        for (PlayerQuest playerQuest : playersQuest) {
            if (playerQuest != null) {
                if (playerQuest.getQuest() instanceof SellQuest && playerQuest.getPlayer().toString().equals(player.getUniqueId().toString())) {
                    playersQuests.add(playerQuest);
                }
            }
        }
        return playersQuests;
    }

    /**
     * Gets the players active catch fish quests from the playersQuest set. The set is automatically updated whenever a play
     * joins by giving them quests.
     *
     * @param player Player to check quests of
     * @return the Arraylist of all catch quest related to player
     */
    public ArrayList<PlayerQuest> getPlayerCatchQuest(Player player) {
        ArrayList<PlayerQuest> playersQuests = new ArrayList<>();
        for (PlayerQuest playerQuest : playersQuest) {
            if (playerQuest != null) {
                if (playerQuest.getQuest() instanceof CatchFishQuest && playerQuest.getPlayer().toString().equals(player.getUniqueId().toString())) {
                    playersQuests.add(playerQuest);
                }
            }
        }
        return playersQuests;
    }

    /**
     * Checks to see if player has any active Catch fish quests.
     *
     * @param player Player to check
     * @return true if player does, false if not.
     */
    public boolean hasActiveCatchQuest(Player player) {
        for (PlayerQuest playerQuest : playersQuest) {
            if (playerQuest != null) {
                if (playerQuest.getQuest() instanceof CatchFishQuest && playerQuest.getPlayer().toString().equals(player.getUniqueId().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if player has any active sell quests.
     *
     * @param player player to check
     * @return true if player does, false if not.
     */
    public boolean hasActiveSellQuest(Player player) {
        for (PlayerQuest playerQuest : playersQuest) {
            if (playerQuest != null) {
                if (playerQuest.getQuest() instanceof SellQuest && playerQuest.getPlayer().toString().equals(player.getUniqueId().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * These methods' purpose is only to be used when players join, so when the quests reset and if the player wasn't online
     * it'll assign them their quests.
     *
     * @param player Player to check if needed to be assigned quests.
     */
    public void assignPlayerQuests(UUID player) {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        if (hadQuests.contains(player)) return;
        for (PlayerQuest playerQuest : playersQuest){
            if (playerQuest.getPlayer() == player) return;
        }
        for (Quest quest : activeQuests) {
            playersQuest.add(new PlayerQuest(player, quest));
        }
        hadQuests.add(player);
    }

    /**
     * This method is used inside the timer when the quests reset. This will assign all active quests to the players
     * online, so that way as soon as it resets, they will automatically get it.
     */
    public void assignQuestToOnline() {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Quest quest : activeQuests) {
                playersQuest.add(new PlayerQuest(player.getUniqueId(), quest));
            }
            hadQuests.add(player.getUniqueId());
        }
    }


    /**
     * Removes the players Quest from their active quests.
     *
     * @param playerQuest Player quest to remove.
     */
    public void removePlayerQuest(PlayerQuest playerQuest) {
        playersQuest.remove(playerQuest);
        if (!instance.getDb().findPlayer(playerQuest.getQuest(), playerQuest.getPlayer()))  return;
        instance.getDb().removePlayer(playerQuest.getQuest(), playerQuest.getPlayer());
    }

    public ArrayList<Quest> getActiveQuests() {
        return activeQuests;
    }

    /*
    Currently Working on, but the goal of this is whenever the server restarts, it saves the names of the active quests to
    the data config, which will allow me to reinstitute back into the activequests on unreload, and then check for those
    tables in the database. Current issue: what happens if server crashes? working on fix for that by either making a table with
    the names of quest, or something else.
     */
    public void saveDailiesOnServerClose() {
        if (activeQuests.isEmpty()) return;
        for (Quest quest : activeQuests){
            instance.getDataConfig().getConfig().set(quest.getName(), quest.getQuestID());
        }
        if (playersQuest.isEmpty()) return;
        for (PlayerQuest playerQuest : playersQuest){
            if (playerQuest == null) continue;
            instance.getDb().savePlayerDataToTable(playerQuest);
        }


    }

    /*
    This method will add the quests back into dailies, assuming that the quest matches with the quest name.
     */
    private void restoreDailyQuestData() {
        reloadQuests(configManager.getQuestConfig().getConfig(), "quests");
        ArrayList<String> savedQuests = new ArrayList<>();
        for (Quest quest : quests){
            if (instance.getDataConfig().getConfig().getString(quest.getName()) == null || !instance.getDataConfig().getConfig().contains(quest.getName())) continue;
            savedQuests.add(quest.getName());
        }
        ArrayList<PlayerQuest> savedData = new ArrayList<>();
        if (savedQuests.isEmpty()){
            System.out.println("creating new quests");
            getNewDailies(quests);
            return;
        }
        for (String string : savedQuests){
            for (Quest quest : quests){
                if (quest.getName().toLowerCase().replace(" ", "_").replace(".", "_").equalsIgnoreCase(string)){
                    activeQuests.add(quest);
                    System.out.println("added to active quests, attempting to add to saved data");
                    if (instance.getDb().loadDataAfterRestart(quest) == null){
                        Bukkit.getLogger().warning("Failed to grab data for a player from sqlLite");
                    }
                   savedData.add(instance.getDb().loadDataAfterRestart(quest));
                }
            }
        }
        playersQuest.addAll(savedData);
        System.out.println("saved data");
    }

    public ArrayList<UUID> getHadQuests() {
        return hadQuests;
    }
}
