package me.afatcookie.mittenfishing.files;

import dev.dbassett.skullcreator.SkullCreator;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Rarity;
import me.afatcookie.mittenfishing.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConfigManager {

    private final FishConfig fishConfig;

    private final GUISConfig guisConfig;

    private final RodConfig rodConfig;
    private final MessageConfig messageConfig;

    private final QuestConfig questConfig;

    public ConfigManager(MittenFishing instance) {
        this.fishConfig = instance.getFc();
        this.guisConfig = instance.getGc();
        this.messageConfig = instance.getMc();
        this.rodConfig = instance.getRodConfig();
        this.questConfig = instance.getQc();
    }

    //FISH CONFIG
    //////////////////////////////////////////////////////
    public int getWeight(String rarity){
        switch (rarity.toLowerCase()){
            case "common":
                return fishConfig.getConfig().getInt("common-weight");
            case "rare":
                return fishConfig.getConfig().getInt("rare-weight");
            case "epic":
                return fishConfig.getConfig().getInt("epic-weight");
            case "legendary":
                return fishConfig.getConfig().getInt("legendary-weight");
            case "mythic":
                return fishConfig.getConfig().getInt("mythic-weight");
            case "special":
                return fishConfig.getConfig().getInt("special-weight");
            case "quest":
                return fishConfig.getConfig().getInt("quest-weight");
        }
        return fishConfig.getConfig().getInt("common-weight") ;
    }

    public double getRarityXpValue(String rarity){
        switch (rarity.toLowerCase()){
            case "common":
                return fishConfig.getConfig().getInt("common-xp");
            case "rare":
                return fishConfig.getConfig().getInt("rare-xp");
            case "epic":
                return fishConfig.getConfig().getInt("epic-xp");
            case "legendary":
                return fishConfig.getConfig().getInt("legendary-xp");
            case "mythic":
                return fishConfig.getConfig().getInt("mythic-xp");
            case "special":
                return fishConfig.getConfig().getInt("special-xp");
            case "quest":
                return fishConfig.getConfig().getInt("quest-xp");
        }
        return fishConfig.getConfig().getInt("common-xp") ;
    }

    public double getRaritySellValue(String rarity){
        switch (rarity.toLowerCase()){
            case "common":
                return fishConfig.getConfig().getInt("common-sell");
            case "rare":
                return fishConfig.getConfig().getInt("rare-sell");
            case "epic":
                return fishConfig.getConfig().getInt("epic-sell");
            case "legendary":
                return fishConfig.getConfig().getInt("legendary-sell");
            case "mythic":
                return fishConfig.getConfig().getInt("mythic-sell");
            case "special":
                return fishConfig.getConfig().getInt("special-sell");
            case "quest":
                return fishConfig.getConfig().getInt("quest-sell");
        }
        return fishConfig.getConfig().getInt("common-sell") ;
    }

    public String getFishName(String path){
        return validateStringInConfig(fishConfig.getConfig(), path + ".name");
    }

    public double getFishSellValue(String path){
        return validateIntInConfig(fishConfig.getConfig(), path + ".sellvalue");
    }

    public double getFishXpValue(String path){
        return validateIntInConfig(fishConfig.getConfig(), path + ".xpvalue");
    }

    public boolean rarityBasedXp(){
        return  validateBooleanInConfig(fishConfig.getConfig(), "rarity-based-xp-value");
    }

    public boolean rarityBasedSellValue(){
        return validateBooleanInConfig(fishConfig.getConfig(), "rarity-based-sell-value");
    }


    public double getRegularDrop(){
        return fishConfig.getConfig().getDouble("chance-normal-fishes");
    }

    public FishConfig getFishConfig() {
        return fishConfig;
    }
    /////////////////////////////////////////////////////////


    //MESSAGE CONFIG
    /////////////////////////////////////////////////////////
    public int getFishedYaw(){
        return  validateIntInConfig(messageConfig.getConfig(), "fish-caught-yaw");
    }

    public int getFishedPitch(){
        return validateIntInConfig(messageConfig.getConfig(), "fish-caught-pitch");
    }


    public Sound getFishedSound(){
        return   Sound.valueOf(messageConfig.getConfig().getString("custom-fish-caught-sound"));
    }

    public boolean isFishSound(){
        return messageConfig.getConfig().getBoolean("is-fish-sound");
    }


    public String getDisplay(String rarity){
        switch (rarity.toLowerCase()){
            case "common":
                return colorizeMessage(messageConfig.getConfig().getString("common-display") + "&lCommon");
            case "rare":
                return colorizeMessage(messageConfig.getConfig().getString("rare-display") + "&lRare");

            case "epic":
                return colorizeMessage(messageConfig.getConfig().getString("epic-display") + "&lEpic");
            case "legendary":
                return colorizeMessage(messageConfig.getConfig().getString("legendary-display") + "&lLegendary");
            case "mythic":
                return colorizeMessage(messageConfig.getConfig().getString("mythic-display") + "&lMythic");
            case "special":
                return colorizeMessage(messageConfig.getConfig().getString("special-display") + "&lSpecial");
            case "quest":
                return colorizeMessage(messageConfig.getConfig().getString("quest-display") + "&lQuest");
        }
        return " ";
    }

    public Rarity getRarity(String rarity){
        switch (rarity.toLowerCase()){
            case "common":
                return Rarity.COMMON;
            case "rare":
                return Rarity.RARE;
            case "epic":
                return Rarity.EPIC;
            case "legendary":
                return Rarity.LEGENDARY;
            case "mythic":
                return Rarity.MYTHIC;
            case "special":
                return Rarity.SPECIAL;
            case "quest":
                return Rarity.QUEST;
        }
        return Rarity.COMMON;
    }

    public String getFoundFishMessage(){
        return colorizeMessage(validateStringInConfig(messageConfig.getConfig(), "found-fish-message"));
    }

    public String getRecievedFishMessage(){
        return colorizeMessage(validateStringInConfig(messageConfig.getConfig(), "received-fish-message"));
    }

    public String getSoldFishMessage(){
        return colorizeMessage(messageConfig.getConfig().getString("sold-fishes-message"));
    }

    public String getTamperingFishMessage(){
        return colorizeMessage(messageConfig.getConfig().getString("tampering-with-fish-message"));
    }

    public String getQuestCompleteMessage(){
        return colorizeMessage(messageConfig.getConfig().getString("quest-complete-message"));
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }
    /////////////////////////////////////////////////////////////


    //GUI CONFIG
    /////////////////////////////////////////////////////////////
    public String getDiscoveryGUISection(){
        return "discoverygui";
    }

    public String getDiscoveryGUIItemSection(){
        return getDiscoveryGUISection() + ".items";
    }

    public Material getDiscoveryGUIPreviousButtonMaterial(){
        if (validateStringInConfig(guisConfig.getConfig(), getDiscoveryGUISection() +".previous-page-button-material") == null ||
                Material.getMaterial(guisConfig.getConfig().getString(getDiscoveryGUISection() +".previous-page-button-material")) == null){
            return Material.DIRT;
        }
        return Material.getMaterial(guisConfig.getConfig().getString(getDiscoveryGUISection() +".previous-page-button-material"));
    }

    public Material getDiscoveryGUINextButtonMaterial(){
        if (validateStringInConfig(guisConfig.getConfig(), getDiscoveryGUISection() +".next-page-button-material") == null ||
                Material.getMaterial(guisConfig.getConfig().getString(getDiscoveryGUISection() +".next-page-button-material")) == null){
            return Material.DIRT;
        }
        return Material.getMaterial(guisConfig.getConfig().getString(getDiscoveryGUISection() +".next-page-button-material"));
    }

    public String getDiscoveryGUIPreviousButtonName(){
        return validateStringInConfig(guisConfig.getConfig(),getDiscoveryGUISection() + ".previous-page-button-name");
    }

    public String getDiscoveryGUINextButtonName(){
        return validateStringInConfig(guisConfig.getConfig(),getDiscoveryGUISection() + ".next-page-button-name");
    }

    public ItemStack getDiscoveryGUIMysteryBlock(){
        if (validateStringInConfig(guisConfig.getConfig(), getDiscoveryGUISection() + ".mysteryblockurl") == null){
            return SkullCreator.itemFromUrl("https://textures.minecraft.net/texture/46ba63344f49dd1c4f5488e926bf3d9e2b29916a6c50d610bb40a5273dc8c82");
        }
        return SkullCreator.itemFromUrl(guisConfig.getConfig().getString(getDiscoveryGUISection() + ".mysteryblockurl"));
    }

    public ItemStack getStandardBackButton(){
        String backbutton = "backbutton.";
        ItemStack ic;
        if (guisConfig.getConfig().getString(backbutton + "material") == null || !guisConfig.getConfig().contains(backbutton + "material")) {
             ic = new ItemCreator(SkullCreator.itemFromUrl(guisConfig.getConfig().getString(backbutton + ".url"))).setDisplayName(guisConfig.getConfig().getString(backbutton + "name")).getItemStack();
        }else{
            ic = new ItemCreator(guisConfig.getConfig().getString(backbutton + "material"), 1)
                    .setDisplayName(guisConfig.getConfig().getString(backbutton + "name")).getItemStack();
        }
        return ic;

    }
    public GUISConfig getGuisConfig() {
        return guisConfig;
    }
    /////////////////////////////////////////////////////////////

    //ROD CONFIG
    ////////////////////////////////////////////////////////////
    public String getRodName(String path){
        return validateStringInConfig(rodConfig.getConfig(), path + ".name");
    }

    public int getRodTime(String path){
        return validateIntInConfig(rodConfig.getConfig(), path +".timetofish");
    }

    public int getLevelReq(String path){
        return validateIntInConfig(rodConfig.getConfig(), path + ".levelreq");
    }

    public RodConfig getRodConfig(){
        return rodConfig;
    }


    //QUEST CONFIG
    //////////////////////////////////////////////////////////////

    public long getDay(){
        return questConfig.getConfig().getLong("day");
    }
    public String getQuestName(String path){
        return validateStringInConfig(questConfig.getConfig(), path + ".name");
    }

    public List<String> getQuestDescription(String path){
        return validateStringListInConfig(questConfig.getConfig(), path + ".lore");
    }

    public int getQuestID(String path){
        return validateIntInConfig(questConfig.getConfig(), path + ".id");
    }

    public double getQuestRewardValue(String path){
        return validateDoubleInConfig(questConfig.getConfig(), path + ".moneyvalue");
    }

    public double getQuestXpValue(String path){
        return validateDoubleInConfig(questConfig.getConfig(), path + ".xpvalue");
    }

    public int getQuestAmountToBeCaught(String path){
        return validateIntInConfig(questConfig.getConfig(), path + ".amount");
    }

    public double getAmountToMake(String path){
        return validateDoubleInConfig(questConfig.getConfig(), path + ".amounttomake");
    }

    public String getQuestType(String path){
        String questType = "catchfish";
        switch (validateStringInConfig(questConfig.getConfig(), path + ".type").toLowerCase()){
            case "catchfish":
                questType = "catchfish";
                break;
            case "sell":
                questType = "sell";
                break;
        }
        return questType;
    }

    private String colorizeMessage(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String validateStringInConfig(FileConfiguration configuration, String string){
        if (!configuration.contains(string) || configuration.getString(string) == null){
            return "Invalid";
        }
        return  configuration.getString(string);
    }

    private boolean validateBooleanInConfig(FileConfiguration configuration, String string){
        return configuration.contains(string);
    }


    private int validateIntInConfig(FileConfiguration configuration, String string){
        if (!configuration.contains(string) || configuration.getInt(string) <= -1){
            return 0;
        }
        return  configuration.getInt(string);
    }

    private double validateDoubleInConfig(FileConfiguration configuration, String string){
        if (!configuration.contains(string) || configuration.getDouble(string) <= -1.0){
            return 0.0;
        }
        return configuration.getDouble(string);
    }

    private  List<String> validateStringListInConfig(FileConfiguration configuration, String string){
        if (!configuration.getStringList(string).isEmpty() || configuration.contains(string)){
            return configuration.getStringList(string);
        }
        return new ArrayList<>();
    }

    public QuestConfig getQuestConfig() {
        return questConfig;
    }
}
