package me.afatcookie.mittenfishing.files;

import me.afatcookie.mittenfishing.MittenFishing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class FishConfig {

    private File file;

    private FileConfiguration fileConfiguration;

    private final MittenFishing instance;

    public FishConfig(MittenFishing instance) {
        this.instance = instance;
        setup();
        getConfig().addDefault("rarity-based-sell-value", false);
        getConfig().addDefault("rarity-based-xp-value", true);
        getConfig().addDefault("rarity-based-lvl-value", true);
        getConfig().addDefault("level.maintitle", "&c&lLevel &f{current_level} &cReached!");
        getConfig().addDefault("level.subtitle", "&6Congrats!");
        getConfig().addDefault("level.fadein", 3);
        getConfig().addDefault("level.stay", 1);
        getConfig().addDefault("level.fadeout", 3);
        getConfig().addDefault("-----------------------", "separator");
        getConfig().addDefault("common.xp", 10);
        getConfig().addDefault("common.sell", 100);
        getConfig().addDefault("common.weight", 5000);
        getConfig().addDefault("common.lvl", 0);
        getConfig().addDefault("rare.xp", 20);
        getConfig().addDefault("rare.sell", 200);
        getConfig().addDefault("rare.weight", 4300);
        getConfig().addDefault("rare.lvl", 5);
        getConfig().addDefault("epic.xp", 30);
        getConfig().addDefault("epic.sell", 300);
        getConfig().addDefault("epic.weight", 1425);
        getConfig().addDefault("epic.lvl", 10);
        getConfig().addDefault("legendary.xp", 40);
        getConfig().addDefault("legendary.sell", 400);
        getConfig().addDefault("legendary.weight", 475);
        getConfig().addDefault("legendary.lvl", 5);
        getConfig().addDefault("mythic.xp", 50);
        getConfig().addDefault("mythic.sell", 500);
        getConfig().addDefault("mythic.weight", 100);
        getConfig().addDefault("mythic.lvl", 20);
        getConfig().addDefault("special.xp", 60);
        getConfig().addDefault("special.sell", 600);
        getConfig().addDefault("special.weight", 1);
        getConfig().addDefault("special.lvl", 25);
        getConfig().addDefault("quest.xp", 70);
        getConfig().addDefault("quest.sell", 700);
        getConfig().addDefault("quest.weight", 8600);
        getConfig().addDefault("quest.lvl", 0);
        getConfig().addDefault("-----------------------", "separator");
        getConfig().addDefault("fishes.common", new HashMap<>());
        getConfig().addDefault("fishes.rare", new HashMap<>());
        getConfig().addDefault("fishes.epic", new HashMap<>());
        getConfig().addDefault("fishes.legendary", new HashMap<>());
        getConfig().addDefault("fishes.mythic", new HashMap<>());
        getConfig().addDefault("fishes.special", new HashMap<>());
        getConfig().addDefault("chance-normal-fishes", 0.99);
        getConfig().addDefault("sell-value", "&6Sell Value: ");
        getConfig().options().copyDefaults(true);
        save();
    }

    public void setup(){
        file = new File(this.instance.getDataFolder(), "fishconfig.yml");

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Something went wrong in " + FishConfig.class.getName());
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
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save config to Fish Config", e);
        }
    }

    public void reload(){
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

}
