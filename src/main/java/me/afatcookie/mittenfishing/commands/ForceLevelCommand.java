package me.afatcookie.mittenfishing.commands;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ForceLevelCommand extends CommandBuilder{
    @Override
    String getName() {
        return "ForceLevel";
    }

    @Override
    String getDescription() {
        return "Forces the desired Player's level by 1";
    }

    @Override
    String getSyntax() {
        return "/mfadmin forceLevel <Player>";
    }

    @Override
    String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    void preform(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof  Player)) return;
        Player player = (Player) commandSender;
        if (strings.length > 1){
            Player target  = Bukkit.getPlayerExact(strings[1]);
            if (target != null){
                instance.getLevelManager().force(target);
                player.sendMessage(ChatColor.YELLOW + "You've just forced " + target.getName() + "level by 1!");
            }
        }
        player.sendMessage("Not enough arguments, please you the command like this: " + getSyntax());

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
