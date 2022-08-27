package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.LootItem;
import me.afatcookie.mittenfishing.fishing.fishingrods.Rod;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This gives all Mitten Fishing related items to the command sender. Name: GiveItems, Description: gives all the items in the game,
 * Syntax: /mf GiveItems, Execute: gives all items from this plugin to the command sender.
 */
public class MfGiveAllCommand extends CommandBuilder{
    @Override
    String getName() {
        return "GiveAll";
    }

    @Override
    String getDescription() {
        return "gives all items in the game";
    }

    @Override
    String getSyntax() {
        return "/MFAdmin GiveAll";
    }

    @Override
    String getColoredSyntax() {
        return null;
    }

    @Override
    void preform(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof  Player)) return;
        Player player = (Player) commandSender;
            if (strings.length > 0) {
                for (LootItem lootItem : lp.getLootPool()) {
                    player.getInventory().addItem(lootItem.getItem());
                }
                for (ItemStack ingredient : lp.getAllIngredients()){
                    player.getInventory().addItem(ingredient);
                }

                if (instance.getRodManager().getFishingRods().isEmpty()) return;
                for (Rod rod : instance.getRodManager().getFishingRods()) {
                    player.getInventory().addItem(rod.getRod());
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
