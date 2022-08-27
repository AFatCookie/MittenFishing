package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.guimanager.FishingRodCraftingGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class OpenRodCraftCommand extends CommandBuilder{
    @Override
    String getName() {
        return "rodcraft";
    }

    @Override
    String getDescription() {
        return "Opens rod crafting menu";
    }

    @Override
    String getSyntax() {
        return "/mf rodcraft";
    }

    @Override
    String getColoredSyntax() {
        return ChatColor.GOLD + "Usage: " + getSyntax();
    }

    @Override
    void preform(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof  Player)) return;
        Player player = (Player) commandSender;
            player.openInventory(new FishingRodCraftingGUI(instance).getInventory());
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
