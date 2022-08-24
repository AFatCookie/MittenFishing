package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.LootItem;
import me.afatcookie.mittenfishing.fishing.fishingrods.Rod;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        return "/MF GiveAll";
    }

    @Override
    void execute(String[] args, Player player) {
        if (player.hasPermission("mittenfishing.admin") || player.isOp()) {
            if (args.length > 0) {
                for (LootItem lootItem : lp.getLootPool()) {
                    player.getInventory().addItem(lootItem.getItem());
                }
                for (ItemStack ingredient : lp.getAllIngredients()){
                    player.getInventory().addItem(ingredient);
                }

/*
            for (Fish fish : instance.getFishDiscover().getFishes()){
                player.getInventory().addItem(fish.getFishItem());
            }

 */
                if (instance.getRodManager().getFishingRods().isEmpty()) return;
                for (Rod rod : instance.getRodManager().getFishingRods()) {
                    player.getInventory().addItem(rod.getRod());
                }
            }

        }
    }
}
