package me.afatcookie.mittenfishing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Command Listener Class. Registers all commands in the constructor, and whenever a player tries to execute a command,
 * it checks if the arguments match any of the subcommands names. If so, it will execute that commands execute method.
 */
public class CommandListener implements CommandExecutor {

    //ArrayList of all commands on server
    private static  final ArrayList<CommandBuilder> commands = new ArrayList<>();

    public CommandListener() {
        commands.add(new MfGiveAllCommand());
        commands.add(new FishingMenuGUICommand());
        commands.add(new ReloadConfigsCommand());
        commands.add(new OpenRodCraftCommand());
        commands.add(new ViewQuestsCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length > 0) {
            for (int i = 0; i < getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                    getSubCommands().get(i).execute(args, player);
                }
            }
        }
        return false;
    }

    private ArrayList<CommandBuilder> getSubCommands(){
        return commands;
    }
}
