package me.afatcookie.mittenfishing;
import me.afatcookie.mittenfishing.DB.Database;
import me.afatcookie.mittenfishing.DB.SQLite;
import me.afatcookie.mittenfishing.commands.AdminCommandListener;
import me.afatcookie.mittenfishing.commands.PlayerCommandListener;
import me.afatcookie.mittenfishing.files.*;
import me.afatcookie.mittenfishing.fishing.DiscoverableFish;
import me.afatcookie.mittenfishing.fishing.FishingEvent;
import me.afatcookie.mittenfishing.fishing.LootPool;
import me.afatcookie.mittenfishing.fishing.fishinglevels.LevelManager;
import me.afatcookie.mittenfishing.fishing.fishingquests.QuestManager;
import me.afatcookie.mittenfishing.fishing.fishingrods.RodManager;
import me.afatcookie.mittenfishing.fishing.guimanager.GUIClickListener;
import me.afatcookie.mittenfishing.listeners.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MittenFishing extends JavaPlugin {


    private static MittenFishing instance;

    private static Economy econ = null;

    private static LootPool lootPool;
    private static MessageConfig messageConfig;
    private static FishConfig fishConfig;
    private static GUISConfig guisConfig;

    private static RodConfig rodConfig;

    private static QuestConfig questConfig;


    private static DataConfig dataConfig;

    private static LevelConfig levelConfig;
    private static ConfigManager configManager;

    private static RodManager rodManager;
    private Database database;


    private static QuestManager questManager;

    private static LevelManager levelManager;
    private static DiscoverableFish fishDiscover;
    public static MittenFishing getInstance(){
        return instance;
    }
    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            System.out.println((String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName())));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    instance = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        fishConfig = new FishConfig(instance);
        guisConfig = new GUISConfig(instance);
        messageConfig = new MessageConfig(instance);
        rodConfig = new RodConfig(instance);
        dataConfig = new DataConfig(instance);
        questConfig = new QuestConfig(instance);
        levelConfig = new LevelConfig(instance);
        configManager = new ConfigManager(instance);
    fishDiscover = new DiscoverableFish(instance);
        lootPool = new LootPool(instance);
        rodManager = new RodManager(instance, getConfigManager());
        database = new SQLite(instance);
        database.createQuestTable();
        database.createLevelTable();
        questManager = new QuestManager(instance);
        levelManager = new LevelManager(instance);


    registerCommands();
    registerListeners();
    }

    @Override
    public void onDisable() {
        questManager.saveDailiesOnServerClose();
        levelManager.saveLevels();
       saveDefaultConfig();
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
    public LootPool getLootPool(){
        return lootPool;
    }


    private void registerCommands(){
        getCommand("mf").setExecutor(new PlayerCommandListener());
        getCommand("mfadmin").setExecutor(new AdminCommandListener());
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new DisableFishTampering(instance), this);
        getServer().getPluginManager().registerEvents(new FishingEvent(instance), this);
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        getServer().getPluginManager().registerEvents(new OnJoinEvent(getFishDiscover(), instance), this);
        getServer().getPluginManager().registerEvents(new DiscoverFishListener(configManager, instance.getFishDiscover()), this);
        getServer().getPluginManager().registerEvents(new QuestCompleteListener(instance), this);
        getServer().getPluginManager().registerEvents(new LevelUpEvent(instance, configManager), this);
    }

    public  MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public  FishConfig getFishConfig() {
        return fishConfig;
    }

    public  GUISConfig getGUIConfig() {
        return guisConfig;
    }

    public  DiscoverableFish getFishDiscover(){
        return fishDiscover;
    }

    public ConfigManager getConfigManager(){
        return configManager;
    }

    public RodConfig getRodConfig(){
        return rodConfig;
    }

    public RodManager getRodManager(){
        return rodManager;
    }

    public  QuestConfig getQuestConfig() {
        return questConfig;
    }

    public  QuestManager getQuestManager() {
        return questManager;
    }

    public  DataConfig getDataConfig() {
        return dataConfig;
    }

    public LevelConfig getLevelConfig(){
        return levelConfig;
    }

    public Database getDataBase() {
        return database;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }

}
