package me.afatcookie.mittenfishing.commands;
import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.fishing.LootPool;
import org.bukkit.entity.Player;

/**
 * Command builder which is used to be inherited when creating commands.
 */
public abstract class CommandBuilder {

    protected LootPool lp = MittenFishing.getInstance().getLp();
    protected MittenFishing instance = MittenFishing.getInstance();

    abstract String getName();
    abstract String getDescription();
    abstract String getSyntax();
    abstract void execute(String[] args, Player player);
}
