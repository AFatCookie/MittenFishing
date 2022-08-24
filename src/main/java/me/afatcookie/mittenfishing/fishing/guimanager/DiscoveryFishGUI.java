package me.afatcookie.mittenfishing.fishing.guimanager;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.files.ConfigManager;
import me.afatcookie.mittenfishing.fishing.DiscoverableFish;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Fish;
import me.afatcookie.mittenfishing.utils.ItemCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*
This class will be for when Discoverable Fish is up and running.
 */
public class DiscoveryFishGUI implements GUI{

    private final DiscoverableFish discoverableFish;

    private final ConfigManager cm;

    private final MittenFishing instance;

    private final  Player player;
    private  int _currentPage = 0;
    Inventory inv;

    public DiscoveryFishGUI(MittenFishing mf, Player player){
        this.discoverableFish = mf.getFishDiscover();
        this.instance = mf;
        this.cm = mf.getConfigManager();
        this.player = player;
    }

    public DiscoveryFishGUI setPage(int page){
        if (page < 0){
            _currentPage = 0;
        }else _currentPage = Math.min(page, 1);
        return this;
    }
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot) {
        if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(cm.getDiscoveryGUIPreviousButtonName()) &&
        _currentPage == 0) return;
        if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(cm.getDiscoveryGUINextButtonName()) &&
        _currentPage ==0 && inventory.firstEmpty() <= -1){
            player.openInventory(this.setPage(this.getPage() + 1).getInventory());
            return;
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
        inv = new InventoryBuilder().createGUIFromConfig(instance.getConfig(), cm.getDiscoveryGUIItemSection(), this, cm.getDiscoveryGUISection()).build();
        inv.setItem(48, new ItemCreator(cm.getDiscoveryGUIPreviousButtonMaterial()).
                setDisplayName(cm.getDiscoveryGUIPreviousButtonName()).getItemStack());
        inv.setItem(50, new ItemCreator(cm.getDiscoveryGUINextButtonMaterial(), 1)
                .setDisplayName(cm.getDiscoveryGUINextButtonName()).getItemStack());
        for (Fish fish : discoverableFish.getPlayersDiscoveredFish(player)){
        }
        return inv;
    }

    public int getPage(){
        return  _currentPage;
    }
}
