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
            if (instance.getQuestManager().getPlayersQuest().isEmpty()) System.out.println("EMPTY PLAYER QUEST");
            for (PlayerQuest quest : instance.getQuestManager().getPlayersQuest()){
                if (quest == null) continue;
                if (quest.getPlayer().toString().equals(player.getUniqueId().toString())){
                    player.sendMessage(quest.getQuest().getName());
                    player.sendMessage("Progress" + quest.getProgress());
                }else{
                    System.out.println("not equal LOL");
                }

            }
            for (Quest quest :instance.getQuestManager().getQuests()){
                System.out.println(quest.getName());
            }
        }
    }
}
