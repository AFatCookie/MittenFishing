package me.afatcookie.mittenfishing.commands;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.fishing.LootPool;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Command builder which is used to be inherited when creating commands.
 */
public abstract class CommandBuilder {

    protected LootPool lp = MittenFishing.getInstance().getLp();
    protected MittenFishing instance = MittenFishing.getInstance();

    protected static List<String> subbiesPass = new ArrayList<>();

    protected static List<String> adminSubbiesPass = new ArrayList<>();

    abstract String getName();
    abstract String getDescription();
    abstract String getSyntax();
    abstract String getColoredSyntax();

    abstract void preform(CommandSender commandSender, String[] strings);

    abstract List<String> getSubCommandArgs(Player player, String[] strings);

}
