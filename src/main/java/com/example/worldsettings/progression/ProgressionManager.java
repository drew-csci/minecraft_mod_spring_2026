package com.example.worldsettings.progression;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProgressionManager {

    public static final double MIN_PROGRESSION_SCALE = 0.50;
    public static final double MAX_PROGRESSION_SCALE = 1.00;

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

    public double getProgressionScale(Player player) {
        if (player == null) {
            return MAX_PROGRESSION_SCALE;
        }
        return getProgressionScale(getProgressionPercent(player));
    }

    public double getAverageProgressionScale(World world) {
        if (world == null || world.getPlayers().isEmpty()) {
            return MAX_PROGRESSION_SCALE;
        }

        double totalScale = 0.0;
        int playerCount = 0;

        for (Player player : world.getPlayers()) {
            totalScale += getProgressionScale(player);
            playerCount++;
        }

        if (playerCount == 0) {
            return MAX_PROGRESSION_SCALE;
        }

        return totalScale / playerCount;
    }

    public static double getProgressionScale(int progressionPercent) {
        int clampedPercent = Math.max(0, Math.min(100, progressionPercent));
        return MIN_PROGRESSION_SCALE
            + ((clampedPercent / 100.0) * (MAX_PROGRESSION_SCALE - MIN_PROGRESSION_SCALE));
    }

    public static int scaleCountByProgression(int originalCount, int progressionPercent) {
        if (originalCount <= 0) {
            return 0;
        }

        double scale = getProgressionScale(progressionPercent);
        return Math.max(1, (int) Math.round(originalCount * scale));
    }

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