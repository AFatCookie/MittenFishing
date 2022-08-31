package me.afatcookie.mittenfishing.fishing.fishingquests;

import me.afatcookie.mittenfishing.fishing.fishesmanger.Fish;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CatchFishQuest extends Quest{

    private final Fish fishToBeCaught;

    private final int amountToBeCaught;

    public CatchFishQuest(String name, List<String> description, double rewardValue, double xpValue, Fish fish, int questID, int amountToBeCaught, ItemStack questItem) {
        super(name, description, rewardValue, xpValue, questID, questItem);
        this.fishToBeCaught = fish;
        this.amountToBeCaught = amountToBeCaught;
    }

    public CatchFishQuest(Quest ob, Fish fish, int amountToBeCaught) {
        super(ob);
        this.fishToBeCaught = fish;
        this.amountToBeCaught =amountToBeCaught;
    }


    public int getAmountToBeCaught() {
        return amountToBeCaught;
    }

    public Fish getFishToBeCaught() {
        return fishToBeCaught;
    }
}
