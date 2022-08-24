package me.afatcookie.mittenfishing.fishing.fishingquests;

import java.util.List;

public class Quest {

    private String name;

    private List<String> description;

    private double rewardValue;

    private double xpValue;

    public Quest(String name, List<String> description, double rewardValue, double xpValue) {
        this.name = name;
        this.description = description;
        this.rewardValue = rewardValue;
        this.xpValue = xpValue;
    }

    public Quest(Quest ob){
        this.name = ob.name;
        this.description = ob.description;
        this.rewardValue = ob.rewardValue;
        this.xpValue = ob.xpValue;
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
}
