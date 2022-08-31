package me.afatcookie.mittenfishing.fishing.guimanager;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishingquests.CatchFishQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.SellQuest;
import me.afatcookie.mittenfishing.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*
Gui that displays all active quests for that player
 */
public class ActiveQuestDisplayGUI implements GUI{

    Inventory inv;

    private final MittenFishing instance;

    private final ConfigManager configManager;

    private final Player player;

    public ActiveQuestDisplayGUI(MittenFishing mittenFishing, Player player){
        this.instance = mittenFishing;
        this.configManager = instance.getConfigManager();
        this.player = player;
    }
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot) {

    }

    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public void onDrag(Player player, Inventory inventory, ItemStack itemOnCursor) {

    }

    @Override
    public Inventory getInventory() {
        inv = new InventoryBuilder().createGUIFromConfig(configManager.getGuisConfig().getConfig(), "questdisplaygui.items", this, "questdisplaygui").build();
        int firstLoop = 12;
        int secondLoop = 12;

        for (int i = 0; i < instance.getQuestManager().getActiveQuests().size(); i++){
            inv.setItem(firstLoop, new ItemCreator(Material.RED_STAINED_GLASS_PANE, 1).setDisplayName("&cQuest completed, come back at 8PM EST to view the new quest!").getItemStack());
            firstLoop++;
        }
        for (PlayerQuest quest : instance.getQuestManager().getAllOfPlayerQuest(player)){
            if (quest.getQuest() instanceof SellQuest) {
                inv.setItem(secondLoop, new ItemCreator(quest.getQuest().getQuestItem()).setDisplayName(quest.getQuest().getName()).setLore(quest.getQuest().getDescription()).addLoreLine(
                                "&f&lProgress: &r" + "&6" + quest.getProgress() + "/" + ((SellQuest) quest.getQuest()).getAmountToMake())
                        .addGlow(true).getItemStack());
            }else{
                inv.setItem(secondLoop, new ItemCreator(quest.getQuest().getQuestItem()).setDisplayName(quest.getQuest().getName()).setLore(quest.getQuest().getDescription()).addLoreLine(
                                "&f&lProgress: &r" + "&6" + quest.getProgress() + "/" + ((CatchFishQuest) quest.getQuest()).getAmountToBeCaught())
                        .addGlow(true).getItemStack());
            }
            secondLoop++;
        }
        return inv;
    }
}
