package me.afatcookie.mittenfishing.listeners;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.customevents.QuestCompleteEvent;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishingquests.Quest;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuestCompleteListener implements Listener {

    private final ConfigManager cm;

    private final Economy economy = MittenFishing.getEconomy();
    private final MittenFishing instance;

    public QuestCompleteListener(MittenFishing instance) {
        this.cm = instance.getConfigManager();
        this.instance = instance;
    }


    @EventHandler
    public void onQuestComplete(QuestCompleteEvent e){
       final Player player = Bukkit.getPlayer(e.getPlayer());
       final Quest quest = e.getQuest().getQuest();
       player.sendMessage(cm.getQuestCompleteMessage().replace("{quest_name}", quest.getName()));
       economy.depositPlayer(player, quest.getRewardValue());

       instance.getQuestManager().removePlayerQuest(e.getQuest());

    }
}
