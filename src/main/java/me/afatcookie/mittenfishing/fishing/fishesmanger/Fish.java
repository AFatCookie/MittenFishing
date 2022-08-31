package me.afatcookie.mittenfishing.fishing.fishesmanger;

import dev.dbassett.skullcreator.SkullCreator;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.utils.FishUtils;
import me.afatcookie.mittenfishing.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * Fish class. Used to create new types of fish, and allows to access certain fish variables like rarity, name, settings and more
 */
@SuppressWarnings({"unused", "ConstantConditions"})
public class Fish{


    private ItemStack fish;
    private final String name;
    private final Rarity rarity;
    private Material material;
    private  double sellValue;
    private  double fishingXpValue;
    private boolean isMaterial;
    private boolean isURL = false;
    private boolean is64Bit = false;
    private  boolean shouldGlow = false;
    private boolean shouldDisplayRarity = true;
    private boolean shouldDisplayValue = true;

    private boolean shouldDisplayLvlReq = true;

    private boolean canbeSold = true;
    private boolean shouldBroadcastMessage = false;

    private String broadCastMessage;

    private boolean specialCatch;
    //Gives boost when eaten or holding
    private boolean hasStatBoost;
    private final String pathWay;

    private final MittenFishing instance;

    private int level;

    private final ConfigManager cm;


    public Fish(Rarity rarity, String name, double sellValue, double fishingXpValue, String pathWay, MittenFishing instance){
        this.instance = instance;
        this.name = name;
        this.rarity = rarity;
        this.sellValue = sellValue;
        this.fishingXpValue = fishingXpValue;
        this.pathWay = pathWay;
         cm = instance.getConfigManager();
         getFish(cm.getFishConfig().getConfig());
    }



    /**
     *
     * @return     //Returns a Material.PLAYER_HEAD with the skin of the 64bit part from config
     */
    private ItemStack setItemWithBase64(FileConfiguration configuration){
      return SkullCreator.itemFromBase64(configuration.getString(pathWay + ".base64"));
    }

    /**
     *
     * @return     //Returns a Material.PLAYER_HEAD with the skin of the url from config
     */
    private ItemStack setItemWithURL(FileConfiguration configuration){
      return SkullCreator.itemFromUrl(configuration.getString(pathWay + ".url"));
    }

    private void setLevel(FileConfiguration configuration){
        if (configuration.getInt(pathWay + ".level") == -1 || !configuration.contains(pathWay + ".level") && !cm.rarityBasedLvlValue()){
            level = 0;
            return;
        }
        if (cm.rarityBasedLvlValue()){
            level = cm.getRarityLevelValue(rarity);
            return;
        }
        level = configuration.getInt(pathWay + ".level");
    }

    /**
     * Checks if the fish will have a material value, 64bit value, url value, and glow value. Glow value will only work
     * if the item is a Material other than PLAYER_HEAD. It will then create a fish itemStack with the configs name, lore etc.
     * if adding rarity to lore is enabled, it will add the rarity with its display name, and same goes with sell value. It
     * will always add a PDC double to the item with its sell value, so it's accessible via NBT.
     *
     * @return The ItemStack version of the Fish
     */
    public ItemStack getFish(FileConfiguration configuration){
        checkMaterial(configuration);
        check64Bit(configuration);
        checkURL(configuration);
        shouldGlow(configuration);
        displayRarity(configuration);
        displayValue(configuration);
        isShouldBroadcastMessage(configuration);
        canBeSold(configuration);
        setShouldDisplayLvlReq(configuration);
        setLevel(configuration);
        ItemCreator ic;
        if (isURL){
            ic = new ItemCreator(setItemWithURL(configuration)).setDisplayName(name).setLore(configuration.getStringList(pathWay + ".lore"));
            applyEffects(ic, rarity, configuration);
            fish = ic.getItemStack();
        }else if (is64Bit) {
            ic = new ItemCreator(setItemWithBase64(configuration)).setDisplayName(name).setLore(configuration.getStringList(pathWay + ".lore"));
            applyEffects(ic, rarity, configuration);
            fish = ic.getItemStack();
        } else if (isMaterial) {
            ic = new ItemCreator(Material.valueOf(configuration.getString(pathWay + ".material")), 1).setDisplayName(name).setLore(configuration.getStringList(pathWay + ".lore"));
            applyEffects(ic, rarity, configuration);
            fish = ic.getItemStack();
        }
        return fish;


    }

    public ItemStack getFishItem(){
        return fish;
    }

    /**
     * Applies the effects to the itemCreator instance; If it should glow, display rarity, display sell value, and add Value
     * into it's NBT.
     * @param ic ItemCreator instance to effect
     * @param rarity Rarity to display
     */

    public void applyEffects(ItemCreator ic, Rarity rarity, FileConfiguration configuration){
        if (cm.rarityBasedXp()){
            fishingXpValue = rarity.getXpValue();
        }

        if (cm.rarityBasedSellValue()){
            sellValue = rarity.getSellValue();
        }
        if (shouldDisplayRarity){
            ic.addLoreLine(rarity.getDisplay());
        }
        if (shouldDisplayValue){
            ic.addLoreLine(configuration.getString("sell-value") + sellValue);
        }

        if (shouldDisplayLvlReq){
            ic.addLoreLine("&cLevel needed to catch: " + level);
        }
        if (isMaterial && shouldGlow){
            ic.addGlow(true);
        }
        if (shouldBroadcastMessage){
            broadCastMessage = configuration.getString(pathWay + ".broadcastmessage");
        }
        if (canbeSold){
            ic.setPDCDouble(new NamespacedKey(instance, "value"), sellValue);
        }
        ic.setPDCInteger(new NamespacedKey(instance, "levelreq"), level);

        ic.setPDCDouble(new NamespacedKey(instance, "xp-value"), fishingXpValue);
    }


    /**
     * checks if fish uses an url for texture when making in config.
     */
    private void checkURL(FileConfiguration configuration){
        isURL = configuration.getString(pathWay + ".url") != null && configuration.contains(pathWay + ".url");
    }

    /**
     * checks if fish uses a 64bit for texture when making in config
     */
    private void check64Bit(FileConfiguration configuration){
        if (configuration.getString(pathWay + ".base64") == null || !configuration.contains(pathWay + ".base64")){
            is64Bit = false;
            return;
        }
        is64Bit = true;
    }

    /**
     * checks if fish uses a material rather than a custom head when making in config.
     */
    private void checkMaterial(FileConfiguration configuration){
        isMaterial = configuration.getString(pathWay + ".material") != null && configuration.contains(pathWay + ".material");
    }

    /**
     * checks if fish will glow, only working when item is a material, when making in config.
     */
    private void shouldGlow(FileConfiguration configuration){
        if (!configuration.getBoolean(pathWay + ".glow") || !configuration.contains(pathWay + ".glow")){
            shouldGlow = false;
        }
        shouldGlow = configuration.getBoolean(pathWay + ".glow");
    }

    private void setShouldDisplayLvlReq(FileConfiguration configuration){
        if (!configuration.getBoolean(pathWay + ".displaylevelreq") && configuration.contains(pathWay + ".displaylevelreq")) {
            shouldDisplayLvlReq = false;
        }
    }

    /**
     * checks if item should display rarity in its lore when making in config.
     */
    private void displayRarity(FileConfiguration configuration){
        if (!configuration.getBoolean(pathWay + ".displayrarity") || !configuration.contains(pathWay + ".displayrarity")){
            shouldDisplayRarity = true;
        }else{
            shouldDisplayRarity = configuration.getBoolean(pathWay + ".displayrarity");
        }
    }

    private void  canBeSold(FileConfiguration configuration){
        canbeSold = configuration.getBoolean(pathWay + ".canbesold") || !configuration.contains(pathWay + ".canbesold");
    }

    /**
     * Checks if item should display sell value in its lore when making in config.
     */
    private void displayValue(FileConfiguration configuration){
        if (!configuration.getBoolean(pathWay + ".displayvalue") || !configuration.contains(pathWay + ".displayvalue")){
            shouldDisplayValue = true;
        }else{
            shouldDisplayValue = configuration.getBoolean(pathWay + ".displayvalue");
        }
    }

    private void isShouldBroadcastMessage(FileConfiguration configuration){
        if (!configuration.getBoolean(pathWay + ".shouldbroadcast") || !configuration.contains(pathWay +  ".shouldbroadcast")){
            shouldBroadcastMessage = false;
        }else{
            shouldBroadcastMessage = configuration.getBoolean(pathWay + ".shouldbroadcast");
        }
    }


    //GETTERS AND SETTERS
    public String getName() {
        return name;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Material getMaterial() {
        return material;
    }

    public int getLevel() {
        return level;
    }

    public double getSellValue() {
        return sellValue;
    }

    public boolean isSpecialCatch() {
        return specialCatch;
    }

    public boolean isHasStatBoost() {
        return hasStatBoost;
    }

    public double getFishingXpValue(){
        return fishingXpValue;
    }

    public void setFish(ItemStack fish) {
        this.fish = fish;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setSellValue(double sellValue, FileConfiguration configuration) {
        getFish(configuration).getItemMeta().getPersistentDataContainer().set(new NamespacedKey(instance, "value"), PersistentDataType.DOUBLE, sellValue);
        this.sellValue= sellValue;
    }

    public void setFishingXpValue(double fishingXpValue, FileConfiguration configuration) {
        getFish(configuration).getItemMeta().getPersistentDataContainer().set(new NamespacedKey(instance, "xpvalue"), PersistentDataType.DOUBLE, fishingXpValue);
        this.fishingXpValue = fishingXpValue;
    }

    public void setMaterial(boolean material) {
        isMaterial = material;
    }

    public void setURL(boolean URL) {
        isURL = URL;
    }

    public void setIs64Bit(boolean is64Bit) {
        this.is64Bit = is64Bit;
    }

    public void setShouldGlow(boolean shouldGlow) {
        this.shouldGlow = shouldGlow;
    }

    public void setShouldDisplayRarity(boolean shouldDisplayRarity) {
        this.shouldDisplayRarity = shouldDisplayRarity;
    }

    public void setShouldDisplayValue(boolean shouldDisplayValue) {
        this.shouldDisplayValue = shouldDisplayValue;
    }

    public void setCanbeSold(boolean canbeSold) {
        this.canbeSold = canbeSold;
    }

    public void setShouldBroadcastMessage(boolean shouldBroadcastMessage) {
        this.shouldBroadcastMessage = shouldBroadcastMessage;
    }

    public void setBroadCastMessage(String broadCastMessage) {
        this.broadCastMessage = broadCastMessage;
    }

    public void setSpecialCatch(boolean specialCatch) {
        this.specialCatch = specialCatch;
    }

    public void setHasStatBoost(boolean hasStatBoost) {
        this.hasStatBoost = hasStatBoost;
    }

    public boolean isShouldDisplayRarity() {
        return shouldDisplayRarity;
    }

    public boolean isShouldDisplayValue() {
        return shouldDisplayValue;
    }



    public String getBroadCastMessage(){
        return FishUtils.colorizeMessage(broadCastMessage);
    }
}
