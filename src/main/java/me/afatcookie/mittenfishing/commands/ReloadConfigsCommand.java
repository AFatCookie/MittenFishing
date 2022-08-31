package me.afatcookie.mittenfishing.commands;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ReloadConfigsCommand extends CommandBuilder{
    @Override
    String getName() {
        return "reload";
    }

    @Override
    String getDescription() {
        return "Reloads everything possible";
    }

    @Override
    String getSyntax() {
        return "/mfadmin reload";
    }

    @Override
    String getColoredSyntax() {
        return ChatColor.GOLD + "Usage: " + getSyntax();
    }

    @Override
    void preform(CommandSender commandSender, String[] strings) {
        if (strings.length > 0) {
                try {

                    instance.getFishConfig().reload();
                    instance.getGUIConfig().reload();
                    instance.getMessageConfig().reload();
                    instance.getRodConfig().reload();
                    instance.getQuestConfig().reload();
                    instance.getLootPool().reload(instance.getFishConfig().getConfig(), instance.getFishDiscover());
                    instance.getRodManager().reload(instance.getRodConfig().getConfig());
                    instance.getQuestManager().reloadQuests(instance.getQuestConfig().getConfig(), "quests");
                    instance.getLevelManager().reload();
                    commandSender.sendMessage(ChatColor.AQUA + "Successfully reloaded Mitten Fishing!");
                } catch (IllegalArgumentException | NullPointerException exception) {
                    commandSender.sendMessage("Failed to reload Mitten Fishing!");
                    commandSender.sendMessage("Exception is in console.");
                    exception.printStackTrace();
                }
            }
        }

    @Override
    List<String> getSubCommandArgs(Player player, String[] strings) {
        ArrayList<String> subbies = new ArrayList<>();

        if (strings.length == 1) {
            StringUtil.copyPartialMatches(strings[0], adminSubbiesPass, subbies);
        }

        return null;
    }


}
