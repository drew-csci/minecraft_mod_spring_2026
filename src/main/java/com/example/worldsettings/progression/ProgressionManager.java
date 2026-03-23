package com.example.worldsettings.progression;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProgressionManager {

    private final Map<String, Integer> advancementScores = new LinkedHashMap<>();

    public ProgressionManager() {
        advancementScores.put("minecraft:story/mine_stone",1);
        advancementScores.put("minecraft:story/smelt_iron", 2);
        advancementScores.put("minecraft:story/obtain_armor", 2);
        advancementScores.put("minecraft:story/enter_the_nether", 4);
        advancementScores.put("minecraft:story/mine_diamond", 5);
        advancementScores.put("minecraft:story/enchant_item", 5);
        advancementScores.put("minecraft:end/root", 6);
        advancementScores.put("minecraft:end/kill_dragon", 10);
        advancementScores.put("minecraft:nether/summon_wither", 10);
        advancementScores.put("minecraft:adventure/hero_of_the_village", 4);
    }

    public int calculateProgressionScore(Player player) {
        return getAdvancementScore(player) + getGearTierScore(player);
    }

    private int getAdvancementScore(Player player) {
        int score = 0;

        for (Map.Entry<String, Integer> entry : advancementScores.entrySet()) {
            NamespacedKey key = NamespacedKey.fromString(entry.getKey());
            if (key == null) continue;

            Advancement advancement = Bukkit.getAdvancement(key);
            if (advancement == null) continue;

            if (player.getAdvancementProgress(advancement).isDone()) {
                score += entry.getValue();
            }
        }

        return score;
    }

    private int getGearTierScore(Player player) {
        PlayerInventory inv = player.getInventory();
        int bestTier = 0;

        bestTier = Math.max(bestTier, getItemTier(inv.getHelmet()));
        bestTier = Math.max(bestTier, getItemTier(inv.getChestplate()));
        bestTier = Math.max(bestTier, getItemTier(inv.getLeggings()));
        bestTier = Math.max(bestTier, getItemTier(inv.getBoots()));
        bestTier = Math.max(bestTier, getItemTier(inv.getItemInMainHand()));

        return switch (bestTier) {
            case 1 -> 4;
            case 2 -> 8;
            case 3 -> 12;
            default -> 0;
        };
    }

    private int getItemTier(ItemStack item) {
        if (item == null) return 0;

        String name = item.getType().name();

        if (name.startsWith("NETHERITE_")) return 3;
        if (name.startsWith("DIAMOND_")) return 2;
        if (name.startsWith("IRON_")) return 1;

        return 0;
    }
}

