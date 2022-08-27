package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.guimanager.MainGui;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This Command opens the fishing menu GUI. Name: FishingMenu, Description: Opens main fishing menu.
 * Syntax: /mf FishingMenu, execute: opens fishing menu.
 */
public class FishingMenuGUICommand extends CommandBuilder{


    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getDescription() {
        return "opens fishing menu";
    }

    @Override
    public String getSyntax() {
        return "/mf menu";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    void preform(CommandSender commandSender, String[] strings) {
        if (strings.length < 1){
            commandSender.sendMessage(getColoredSyntax());
            return;
        }
        if (!(commandSender instanceof  Player)) return;
        Player player = (Player) commandSender;
        player.openInventory(new MainGui(instance).getInventory());

    }


    @Override
    List<String> getSubCommandArgs(Player player, String[] strings) {
        ArrayList<String> subbies = new ArrayList<>();

        if (strings.length == 1){
            StringUtil.copyPartialMatches(strings[0], subbiesPass, subbies);
        }
        return null;
    }






}
