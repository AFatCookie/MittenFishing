package me.afatcookie.mittenfishing.commands;

import me.afatcookie.mittenfishing.fishing.guimanager.FishingRodCraftingGUI;
import org.bukkit.entity.Player;

public class OpenRodCraftCommand extends CommandBuilder{
    @Override
    String getName() {
        return "craftrod";
    }

    @Override
    String getDescription() {
        return "Opens rod crafting menu";
    }

    @Override
    String getSyntax() {
        return "/mf craftrod";
    }

    @Override
    void execute(String[] args, Player player) {
        if (args.length > 0){
            player.openInventory(new FishingRodCraftingGUI(instance).getInventory());
        }
    }
}
