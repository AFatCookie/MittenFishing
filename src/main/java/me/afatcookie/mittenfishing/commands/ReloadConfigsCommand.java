package me.afatcookie.mittenfishing.commands;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadConfigsCommand extends CommandBuilder{
    @Override
    String getName() {
        return "reload";
    }

    @Override
    String getDescription() {
        return "Reloads everything possible";
    }

    @Override
    String getSyntax() {
        return "/mf reload";
    }

    @Override
    void execute(String[] args, Player player) {
        if (args.length > 0) {
            if (player.hasPermission("mittenfishing.admin") || player.isOp()) {
                try {

                    instance.getFc().reload();
                    instance.getGc().reload();
                    instance.getMc().reload();
                    instance.getRodConfig().reload();
                    instance.getQc().reload();
                    instance.getLp().reload(instance.getFc().getConfig(), instance.getFishDiscover());
                    instance.getRodManager().reload(instance.getRodConfig().getConfig());
                    instance.getQuestManager().reloadQuests(instance.getQc().getConfig(), "quests");
                    player.sendMessage(ChatColor.AQUA + "Successfully reloaded Mitten Fishing!");
                } catch (IllegalArgumentException | NullPointerException exception) {
                    player.sendMessage("Failed to reload Mitten Fishing!");
                    player.sendMessage("Exception is in console.");
                    exception.printStackTrace();
                }
            }else{
                player.sendMessage(ChatColor.RED  + "You don't have access to this command!, if this is an error, please contact an administrator");
            }
        }
    }
}
