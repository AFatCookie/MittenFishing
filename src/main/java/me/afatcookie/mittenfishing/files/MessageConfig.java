package me.afatcookie.mittenfishing.files;

import me.afatcookie.mittenfishing.MittenFishing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class MessageConfig {


    private File file;

    private FileConfiguration fileConfiguration;

    private final MittenFishing instance;

    public MessageConfig(MittenFishing instance) {
        this.instance = instance;
        setup();
        getConfig().addDefault("common-display", "&f");
        getConfig().addDefault("rare-display", "&7");
        getConfig().addDefault("epic-display", "&5");
        getConfig().addDefault("legendary-display", "&6");
        getConfig().addDefault("mythic-display", "&d");
        getConfig().addDefault("special-display", "&c");
        getConfig().addDefault("quest-display", "&7");
        getConfig().addDefault("-----------------------", "separator");
        getConfig().addDefault("received-fish-message", "You've just fished up a {fish_name}!");
        getConfig().addDefault("tampering-with-fish-message", "&cYou cannot place/eat this custom fish!");
        getConfig().addDefault("sold-fishes-message", "&eYou just made: &a{total_amount} from this sell!");
        getConfig().addDefault("found-fish-message", "&bYou've just discovered a:&r&6 {fish_name}!");
        getConfig().addDefault("quest-complete-message", "&a{quest_name} has been Completed!");
        getConfig().addDefault("-----------------------", "separator");
        getConfig().addDefault("is-fish-sound", true);
        getConfig().addDefault("custom-fish-caught-sound", "ENTITY_PLAYER_LEVELUP");
        getConfig().addDefault("fish-caught-yaw", 5);
        getConfig().addDefault("fish-caught-pitch", 10);
        getConfig().options().copyDefaults(true);
        save();
    }

    public void setup(){
        file = new File(this.instance.getDataFolder(), "messageconfig.yml");

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Something went wrong in " + MessageConfig.class.getName());
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
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save config to MessageConfig", e);
        }
    }

    public void reload(){
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
