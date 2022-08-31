package me.afatcookie.mittenfishing.fishing.fishingquests;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Quest {

    private String name;

    private List<String> description;

    private double rewardValue;

    private double xpValue;

    private int questID;

    private ItemStack questItem;

    public Quest(String name, List<String> description, double rewardValue, double xpValue, int questID, ItemStack questItem) {
        this.name = name;
        this.description = description;
        this.rewardValue = rewardValue;
        this.xpValue = xpValue;
        this.questID = questID;
        this.questItem = questItem;
    }

    public Quest(Quest ob){
        this.name = ob.name;
        this.description = ob.description;
        this.rewardValue = ob.rewardValue;
        this.xpValue = ob.xpValue;
        this.questID = ob.questID;
        this.questItem = ob.getQuestItem();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public double getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(double rewardValue) {
        this.rewardValue = rewardValue;
    }

    public double getXpValue() {
        return xpValue;
    }

    public void setXpValue(double xpValue) {
        this.xpValue = xpValue;
    }

    public void setQuestID(int questID) {
        this.questID = questID;
    }

    public int getQuestID() {
        return questID;
    }

    public ItemStack getQuestItem() {
        return questItem;
    }

    public void setQuestItem(ItemStack questItem) {
        this.questItem = questItem;
    }
}
