package me.afatcookie.mittenfishing.files;

import me.afatcookie.mittenfishing.MittenFishing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class QuestConfig {

    private File file;

    private FileConfiguration fileConfiguration;

    private final MittenFishing instance;

    public QuestConfig(MittenFishing instance) {
        this.instance = instance;
        setup();
        getConfig().addDefault("day", System.currentTimeMillis());
        getConfig().addDefault("quests", new HashMap<>());
        getConfig().addDefault("sell-value", "&6Sell Value: ");
        getConfig().options().copyDefaults(true);
        save();
    }

    public void setup(){
        file = new File(this.instance.getDataFolder(), "questconfig.yml");

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Something went wrong in " + QuestConfig.class.getName());
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig(){
        return fileConfiguration;
    }

    public void save(){
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save config to Quest Config", e);
        }
    }

    public void reload(){
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
