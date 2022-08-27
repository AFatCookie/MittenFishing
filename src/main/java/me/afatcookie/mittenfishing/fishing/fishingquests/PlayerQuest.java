package me.afatcookie.mittenfishing.fishing.fishingquests;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.customevents.QuestCompleteEvent;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Think of this as a "per-Player" quest. this allows me to access each person individually with the same quest. Because
 * I use the UUID, I can get a Player quest through the UUID, and it can be any quest. So for the future, If decided to make more
 * quests, you can access each players' quests through here, because each object has either a different quest or different uuid,
 * however they link together.
 */
public class PlayerQuest {

    private final UUID player;

    private int progress;

    private MittenFishing instance;

    private final Quest quest;


    public PlayerQuest(UUID player, Quest quest) {
        this.player = player;
        this.quest = quest;
    }

    public PlayerQuest(UUID player, Quest quest, int progress, MittenFishing instance){
        this.player = player;
        this.quest = quest;
        this.progress = progress;
        this.instance = instance;
    }

    public UUID getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void updateQuestProgress(int progress) {
        this.progress +=  progress;
        if (hasCompletedQuest(this.progress)){
            Bukkit.getServer().getPluginManager().callEvent(new QuestCompleteEvent(player, this));
        }else{
            instance.getDb().savePlayerDataToTable(this);
        }
    }


    public boolean hasCompletedQuest(int progress){
        if (quest instanceof SellQuest){
            return ((SellQuest) quest).getAmountToMake() <= progress;
        }
        if (quest instanceof CatchFishQuest){
            return ((CatchFishQuest) quest).getAmountToBeCaught() <= progress;
        }
        return false;
    }

}
