package me.afatcookie.mittenfishing.fishing.guimanager;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.fishingquests.PlayerQuest;
import me.afatcookie.mittenfishing.fishing.fishingquests.QuestManager;
import me.afatcookie.mittenfishing.fishing.fishingquests.SellQuest;
import me.afatcookie.mittenfishing.utils.FishUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Sell Gui. Few Notes: Make sure that the material to click in order to sell, matches with the one here. If they don't match
 * it will not work. It's done like this to prevent any issues with getting the wrong item from digging into the Sell Gui configSection.
 */
public class SellGUI implements GUI{
    private int totalValue = 0;

    private final ConfigManager cm;
    private final Economy economy = MittenFishing.getEconomy();
    private final Player player;

    private final QuestManager questManager;
    Inventory inv;

    public SellGUI(MittenFishing mf, Player player){
        this.cm = mf.getConfigManager();
        this.questManager = mf.getQuestManager();
        this.player = player;
    }
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot) {
         int counter = 0;
        //This method will handle selling fish, Regardless of where its being done. This will hopefully prevent any
        //duping with guis, and gets an accurate amount for fishes.
       // if (!isNotGlass(this.player.getInventory(), slot)) return;
        if (inventory.contains(clickedItem) && clickedItem.getType() == Material.HOPPER && clickType.isLeftClick()) {
            for (int i = 0; i < this.player.getInventory().getSize(); i++) {
                if (isNotGlass(this.player.getInventory(), i) && !this.player.getInventory().getItem(i).getType().toString().contains("HOPPER")) {
                    if (FishUtils.isFish(this.player.getInventory().getItem(i))) {
                        this.player.getInventory().setItem(i, new ItemStack(Material.AIR, 1));
                    }
                }
            }
                economy.depositPlayer(this.player, totalValue);
                this.player.sendMessage(cm.getSoldFishMessage().replace("{total_amount}", String.valueOf(totalValue)));
                inventory.clear();
                player.closeInventory();
                if (!questManager.hasActiveSellQuest(this.player)) return;
                for (PlayerQuest playerQuest : questManager.getPlayerSellQuest(this.player)) {
                    if (playerQuest == null) continue;
                    if (playerQuest.getQuest() instanceof SellQuest && counter <= 0) {
                        playerQuest.updateQuestProgress(totalValue);
                    }else{
                        counter++;
                    }
                }
            }else{
        }
        }



    @Override
    public void onClose(Player player, Inventory inventory) {
    }

    @Override
    public void onDrag(Player player, Inventory inventory, ItemStack itemOnCursor) {

    }

    @Override
    public Inventory getInventory() {
        inv = new InventoryBuilder().createGUIFromConfig(cm.getGuisConfig().getConfig(), "sellgui.items", this, "sellgui").build();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (FishUtils.isFish(player.getInventory().getItem(i)) && player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getAmount() <= 1){
                totalValue += FishUtils.getFishValue(player.getInventory().getItem(i));
            }else if (FishUtils.isFish(player.getInventory().getItem(i)) && player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getAmount() > 1){
                totalValue += (FishUtils.getFishValue(player.getInventory().getItem(i)) * player.getInventory().getItem(i).getAmount());
            }
        }
        return inv;
    }

    private boolean isNotGlass(Inventory inventory, int slot){
        return  inventory.getItem(slot) != null && inventory.getItem(slot).getType() != Material.AIR && !inventory.getItem(slot).getType().toString().contains("PANE");
    }

}
