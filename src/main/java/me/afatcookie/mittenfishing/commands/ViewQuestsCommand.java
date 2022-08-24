package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.Quest;
import org.bukkit.entity.Player;

public class ViewQuestsCommand extends CommandBuilder{
    @Override
    String getName() {
        return "viewQuests";
    }

    @Override
    String getDescription() {
        return "Views all active quests for the day";
    }

    @Override
    String getSyntax() {
        return "/mf viewQuests";
    }

    @Override
    void execute(String[] args, Player player) {
        if (args.length > 0){
            for (PlayerQuest quest : instance.getQuestManager().getPlayersQuest()){
                if (quest.getPlayer() == player.getUniqueId()){
                    player.sendMessage(quest.getQuest().getName());
                    player.sendMessage("Progress" + quest.getProgress());
                }
            }
            for (Quest quest :instance.getQuestManager().getQuests()){
                System.out.println(quest.getName());
            }
        }
    }
}
