package me.afatcookie.mittenfishing.commands;

import com.github.mittenmc.serverutils.UUIDConverter;
import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.Quest;
import me.afatcookie.mittenfishing.fishing.guimanager.ActiveQuestDisplayGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
            player.openInventory(new ActiveQuestDisplayGUI(instance, player).getInventory());
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
