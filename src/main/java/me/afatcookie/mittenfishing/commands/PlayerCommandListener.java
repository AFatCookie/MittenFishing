package me.afatcookie.mittenfishing.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Command Listener Class. Registers all commands in the constructor, and whenever a player tries to execute a command,
 * it checks if the arguments match any of the subcommands names. If so, it will execute that commands execute method.
 */
public class PlayerCommandListener implements TabExecutor {

    //ArrayList of all commands on server
    private static  final ArrayList<CommandBuilder> commands = new ArrayList<>();

    private static  final ArrayList<String> commandsName = new ArrayList<>();

    public PlayerCommandListener() {
        commands.add(new FishingMenuGUICommand());
        commands.add(new OpenRodCraftCommand());
        commands.add(new ViewQuestsCommand());
        commands.add(new ViewLevelCommand());

        for (CommandBuilder commandBuilder : commands){
            commandsName.add(commandBuilder.getName());
            CommandBuilder.subbiesPass.add(commandBuilder.getName());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length > 0) {
            for (int i = 0; i < getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                    getSubCommands().get(i).preform(sender, args);
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "Invalid Command!");
        sender.sendMessage(ChatColor.YELLOW + "Please use /mf help to see a list of valid commands!");
        return false;
    }

    private ArrayList<CommandBuilder> getSubCommands(){
        return commands;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0], commandsName, subcommandsArguments);

            return subcommandsArguments;
        }
        else if (args.length >= 2) {
            for (CommandBuilder subcommand : commands) {
                if (args[0].equalsIgnoreCase(subcommand.getName())) {
                    return subcommand.getSubCommandArgs((Player) sender, args);
                }
            }
        }

        return null;
    }
    }
