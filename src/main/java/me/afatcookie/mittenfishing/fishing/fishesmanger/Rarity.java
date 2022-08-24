package me.afatcookie.mittenfishing.fishing.fishesmanger;

import me.afatcookie.mittenfishing.MittenFishing;

/**
 * Rarity class. Each rarity has a weight, and a display which are both customizable via configuration. Creating new
 * rarities cannot be one via config, so if that time comes, you can create one right here and hook it into config easily.
 */
public enum Rarity {



    COMMON(MittenFishing.getInstance().getConfigManager().getWeight("Common"), MittenFishing.getInstance().getConfigManager().getDisplay("Common"),  MittenFishing.getInstance().getConfigManager().getRarityXpValue("common"), MittenFishing.getInstance().getConfigManager().getRaritySellValue("common")),
    RARE(MittenFishing.getInstance().getConfigManager().getWeight("Rare"), MittenFishing.getInstance().getConfigManager().getDisplay("Rare"),  MittenFishing.getInstance().getConfigManager().getRarityXpValue("rare"), MittenFishing.getInstance().getConfigManager().getRaritySellValue("rare")),
    EPIC(MittenFishing.getInstance().getConfigManager().getWeight("Epic"),MittenFishing.getInstance().getConfigManager().getDisplay("Epic"),  MittenFishing.getInstance().getConfigManager().getRarityXpValue("epic"), MittenFishing.getInstance().getConfigManager().getRaritySellValue("epic")),
    LEGENDARY(MittenFishing.getInstance().getConfigManager().getWeight("Legendary"), MittenFishing.getInstance().getConfigManager().getDisplay("Legendary"),  MittenFishing.getInstance().getConfigManager().getRarityXpValue("legendary"), MittenFishing.getInstance().getConfigManager().getRaritySellValue("legendary")),
    MYTHIC(MittenFishing.getInstance().getConfigManager().getWeight("Mythic"), MittenFishing.getInstance().getConfigManager().getDisplay("Mythic"),  MittenFishing.getInstance().getConfigManager().getRarityXpValue("mythic"), MittenFishing.getInstance().getConfigManager().getRaritySellValue("mythic")),
    SPECIAL(MittenFishing.getInstance().getConfigManager().getWeight("Special"), MittenFishing.getInstance().getConfigManager().getDisplay("Special"), MittenFishing.getInstance().getConfigManager().getRarityXpValue("special"), MittenFishing.getInstance().getConfigManager().getRaritySellValue("special")),

    QUEST(MittenFishing.getInstance().getConfigManager().getWeight("Quest"), MittenFishing.getInstance().getConfigManager().getDisplay("Quest"), MittenFishing.getInstance().getConfigManager().getRarityXpValue("Quest"), MittenFishing.getInstance().getConfigManager().getRaritySellValue("Quest"));


    private final int weight;
    private final String display;

    private final double xpValue;

    private final double sellValue;


    Rarity(int weight, String display, double xp, double sellValue){
     this.weight = weight;
     this.display = display;
     this.xpValue = xp;
     this.sellValue = sellValue;
    }


    public String getDisplay() {
        return display;
    }

    public int getWeight() {
        return weight;
    }

    public double getXpValue(){
        return xpValue;
    }

    public double getSellValue(){
        return sellValue;
    }


}
