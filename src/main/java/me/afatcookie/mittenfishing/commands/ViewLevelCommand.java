package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.fishinglevels.PlayerLevel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewLevelCommand extends CommandBuilder{
    @Override
    String getName() {
        return "viewLevel";
    }

    @Override
    String getDescription() {
        return "Shows the level to commandSender";
    }

    @Override
    String getSyntax() {
        return "/mf viewLevel";
    }

    @Override
    String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    void preform(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof Player)){
            return;
        }
        Player player = (Player) commandSender;
        if (strings.length > 0) {
            player.sendMessage(ChatColor.GOLD + "Current Level: " + instance.getLevelManager().getPlayerLevel(player).getLevel());
            player.sendMessage(ChatColor.GOLD + "Till next level: " + instance.getLevelManager().getPlayerLevel(player).getToNextLevel());
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
