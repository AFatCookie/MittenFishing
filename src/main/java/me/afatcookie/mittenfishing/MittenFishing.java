package me.afatcookie.mittenfishing;
import me.afatcookie.mittenfishing.DB.Database;
import me.afatcookie.mittenfishing.DB.SQLite;
import me.afatcookie.mittenfishing.commands.CommandListener;
import me.afatcookie.mittenfishing.files.*;
import me.afatcookie.mittenfishing.fishing.DiscoverableFish;
import me.afatcookie.mittenfishing.fishing.FishingEvent;
import me.afatcookie.mittenfishing.fishing.LootPool;
import me.afatcookie.mittenfishing.fishing.fishingquests.QuestManager;
import me.afatcookie.mittenfishing.fishing.fishingrods.RodManager;
import me.afatcookie.mittenfishing.fishing.guimanager.GUIClickListener;
import me.afatcookie.mittenfishing.listeners.DisableFishTampering;
import me.afatcookie.mittenfishing.listeners.DiscoverFishListener;
import me.afatcookie.mittenfishing.listeners.OnJoinEvent;
import me.afatcookie.mittenfishing.listeners.QuestCompleteListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MittenFishing extends JavaPlugin {


    private static MittenFishing instance;

    private static Economy econ = null;

    private static LootPool lp;
    private static MessageConfig mc;
    private static FishConfig fc;
    private static GUISConfig gc;

    private static RodConfig rc;

    private static QuestConfig qc;

    private static DataConfig dataConfig;
    private static ConfigManager configManager;

    private static RodManager rodManager;
    private Database db;

    private static QuestManager questManager;
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
        fc = new FishConfig(instance);
        gc = new GUISConfig(instance);
        mc = new MessageConfig(instance);
        rc = new RodConfig(instance);
        dataConfig = new DataConfig(instance);
        qc = new QuestConfig(instance);
        configManager = new ConfigManager(instance);
    fishDiscover = new DiscoverableFish(instance);
        lp = new LootPool(instance);
        rodManager = new RodManager(instance, getConfigManager());
        questManager = new QuestManager(instance);

    registerCommands();
    registerListeners();
    }

    @Override
    public void onDisable() {
        questManager.saveDailiesOnServerClose();
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
    public LootPool getLp(){
        return lp;
    }


    private void registerCommands(){
        getCommand("mf").setExecutor(new CommandListener());
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new DisableFishTampering(instance), this);
        getServer().getPluginManager().registerEvents(new FishingEvent(instance), this);
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        getServer().getPluginManager().registerEvents(new OnJoinEvent(getFishDiscover(), instance), this);
        getServer().getPluginManager().registerEvents(new DiscoverFishListener(configManager, instance.getFishDiscover()), this);
        getServer().getPluginManager().registerEvents(new QuestCompleteListener(instance), this);
    }

    public  MessageConfig getMc() {
        return mc;
    }

    public  FishConfig getFc() {
        return fc;
    }

    public  GUISConfig getGc() {
        return gc;
    }

    public  DiscoverableFish getFishDiscover(){
        return fishDiscover;
    }

    public ConfigManager getConfigManager(){
        return configManager;
    }

    public RodConfig getRodConfig(){
        return rc;
    }

    public RodManager getRodManager(){
        return rodManager;
    }

    public  QuestConfig getQc() {
        return qc;
    }

    public  QuestManager getQuestManager() {
        return questManager;
    }

    public  DataConfig getDataConfig() {
        return dataConfig;
    }

    public Database getDb() {
        return db;
    }
}
