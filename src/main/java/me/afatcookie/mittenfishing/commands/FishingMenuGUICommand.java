package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.guimanager.MainGui;
import org.bukkit.entity.Player;

/**
 * This Command opens the fishing menu GUI. Name: FishingMenu, Description: Opens main fishing menu.
 * Syntax: /mf FishingMenu, execute: opens fishing menu.
 */
public class FishingMenuGUICommand extends CommandBuilder{
    @Override
    String getName() {
        return "FishingMenu";
    }

    @Override
    String getDescription() {
        return "Opens main fishing menu.";
    }

    @Override
    String getSyntax() {
        return "/mf FishingMenu";
    }

    @Override
    void execute(String[] args, Player player) {
        if (args.length > 0){
            player.openInventory(new MainGui(instance).getInventory());
        }

    }
}
