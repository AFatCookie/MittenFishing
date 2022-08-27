package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.Quest;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

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
    String getColoredSyntax() {
        return ChatColor.GOLD + "Usage: " + getSyntax();
    }

    @Override
    void preform(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof  Player)) return;
        Player player = (Player) commandSender;
        if (strings.length > 0){
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

    @Override
    List<String> getSubCommandArgs(Player player, String[] strings) {
        ArrayList<String> subbies = new ArrayList<>();

        if (strings.length == 1) {
            StringUtil.copyPartialMatches(strings[0], subbiesPass, subbies);
        }

        return null;
    }


}
