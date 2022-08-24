package me.afatcookie.mittenfishing.fishing;

import me.afatcookie.mittenfishing.MittenFishing;
import me.afatcookie.mittenfishing.fishing.fishesmanger.Fish;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/*
This whole class is a WIP. However, this is what will be used for discovering fish. Waf wanted it so that way players
"discovered Fish", when fishing up unique ones, but the goal of this class at its finish will be having each player own unique
discovered fish. I will probably make a separate object that is both the player and their caught fishes, and then just make a bunch
of those from the database. For the future though.
 */
public class DiscoverableFish {

    private final HashMap<UUID, ArrayList<Fish>> discoveredFish;

    private final ArrayList<Fish> fishes;

    public DiscoverableFish(MittenFishing instance){
        discoveredFish = new HashMap<>();
        fishes = new ArrayList<>();
    }

    public void addFishToPlayer(Player player, Fish fish){
        discoveredFish.get(player.getUniqueId()).add(fish);
    }

    public void addPlayerToHashMap(Player player){
        discoveredFish.putIfAbsent(player.getUniqueId(), new ArrayList<>());
    }

    public HashMap<UUID, ArrayList<Fish>> getDiscoveredFishHash(){
        return discoveredFish;
    }

    public boolean hasDiscoveredFish(Player player, Fish fish){
        return discoveredFish.get(player.getUniqueId()).contains(fish);
    }

    public void removeDiscoveredFish(Player player, Fish fish){
        discoveredFish.get(player.getUniqueId()).remove(fish);
    }

    public void addFishToTotalFish(Fish fish){
        fishes.add(fish);
    }

    public ArrayList<Fish> getPlayersDiscoveredFish(Player player){
        return discoveredFish.get(player.getUniqueId());
    }

    public boolean playerIsInDiscoverHash(Player player){
        return discoveredFish.containsKey(player.getUniqueId());
    }

    public void clearFish(){
        discoveredFish.clear();
    }

    public ArrayList<Fish> getFishes(){
        return fishes;
    }
}
