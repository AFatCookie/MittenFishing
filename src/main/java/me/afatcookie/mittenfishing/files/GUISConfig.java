package me.afatcookie.mittenfishing.files;
import me.afatcookie.mittenfishing.MittenFishing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class GUISConfig {

    private File file;

    private FileConfiguration fileConfiguration;

    private final MittenFishing instance;

    public GUISConfig(MittenFishing instance) {
        this.instance = instance;
        setup();
        getConfig().addDefault("maingui", new HashMap<>());
        getConfig().addDefault("sellgui", new HashMap<>());
        getConfig().addDefault("discoverygui", new HashMap<>());
        getConfig().addDefault("rodcraftgui", new HashMap<>());
        getConfig().addDefault("roddisplaygui", new HashMap<>());
        getConfig().addDefault("-----------------------", "separator");
        getConfig().addDefault("previous-page-button-material", "STONE");
        getConfig().addDefault("next-page-button-material", "STONE");
        getConfig().addDefault("previous-page-button-name", "&6Previous Page");
        getConfig().addDefault("next-page-button-name", "&6Next Page");
        getConfig().options().copyDefaults(true);
        save();
    }

    public void setup(){
        file = new File(this.instance.getDataFolder(), "guiconfig.yml");

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Something went wrong in " + GUISConfig.class.getName());
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
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save config to GUI Config", e);
        }
    }

    public void reload(){
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
