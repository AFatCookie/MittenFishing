package me.afatcookie.mittenfishing.fishing.fishingquests;


import java.util.List;

public class SellQuest extends Quest{


    private final double amountToMake;

    public SellQuest(String name, List<String> description, double rewardValue, double xpValue, int questID, double amountToMake) {
        super(name, description, rewardValue, xpValue, questID);
        this.amountToMake = amountToMake;
    }

    public SellQuest(Quest quest, int amountToMake){
        super(quest);
        this.amountToMake = amountToMake;
    }

    public double getAmountToMake() {
        return amountToMake;
    }
}
