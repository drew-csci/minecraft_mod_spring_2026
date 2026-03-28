package com.example.worldsettings.progression;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProgressionManager {

    private final Set<String> trackedAdvancements = new LinkedHashSet<>();

    public ProgressionManager() {
        trackedAdvancements.add("minecraft:story/mine_stone");
        trackedAdvancements.add("minecraft:story/smelt_iron");
        trackedAdvancements.add("minecraft:story/obtain_armor");
        trackedAdvancements.add("minecraft:story/enter_the_nether");
        trackedAdvancements.add("minecraft:story/mine_diamond");
        trackedAdvancements.add("minecraft:story/enchant_item");
        trackedAdvancements.add("minecraft:end/root");
        trackedAdvancements.add("minecraft:end/kill_dragon");
        trackedAdvancements.add("minecraft:nether/summon_wither");
        trackedAdvancements.add("minecraft:adventure/hero_of_the_village");
    }

    public int getCompletedAdvancementCount(Player player) {
        int completed = 0;

        for (String advancementKey : trackedAdvancements) {
            NamespacedKey key = NamespacedKey.fromString(advancementKey);
            if (key == null) continue;

            Advancement advancement = Bukkit.getAdvancement(key);
            if (advancement == null) continue;

            if (player.getAdvancementProgress(advancement).isDone()) {
                completed++;
            }
        }

        return completed;
    }

    public int getTotalTrackedAdvancements() {
        return trackedAdvancements.size();
    }

    public int getProgressionPercent(Player player) {
        int total = getTotalTrackedAdvancements();
        if (total == 0) return 0;

        int completed = getCompletedAdvancementCount(player);
        return (completed * 100) / total;
    }

    // ✅ ONLY colored version
    public String getDifficultyLabel(int percent) {
        if (percent < 10) return "§7Beginner";
        if (percent < 20) return "§aNovice";
        if (percent < 30) return "§2Apprentice";
        if (percent < 40) return "§bSkilled";
        if (percent < 50) return "§9Advanced";
        if (percent < 60) return "§5Expert";
        if (percent < 70) return "§6Elite";
        if (percent < 80) return "§dMaster";
        if (percent < 90) return "§cLegend";
        return "§4Mythic";
    }
}