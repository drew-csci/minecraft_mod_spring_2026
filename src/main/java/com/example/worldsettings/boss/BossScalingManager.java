package com.example.worldsettings.boss;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * Manages boss tier progression and scaling multipliers.
 * Tracks defeat count per boss key and computes HP/damage scaling.
 */
public class BossScalingManager {

    private static final double MULTIPLIER_PER_TIER = 0.15;    // 15% increase per tier
    private static final double DAMAGE_MULTIPLIER_PER_TIER = 0.10; // 10% damage increase per tier
    private static final double MULTIPLIER_CAP = 5.0;          // Cap at 5x to prevent runaway scaling

    private final Map<String, Integer> bossDefeatCounts; // boss key -> defeat count

    public BossScalingManager() {
        this.bossDefeatCounts = new HashMap<>();
    }

    /**
     * Get or create a boss key from a LivingEntity.
     * Vanilla bosses use fixed type keys.
     * Custom bosses use normalized name + type prefix.
     */
    public String getBossKey(LivingEntity entity) {
        EntityType type = entity.getType();
        
        switch (type) {
            case WITHER:
                return "WITHER";
            case ENDER_DRAGON:
                return "ENDER_DRAGON";
            default:
                // For custom named bosses, use normalized name + type
                String customName = entity.getCustomName();
                if (customName != null && !customName.isEmpty()) {
                    String normalized = normalizeCustomName(customName);
                    return type.name() + "_" + normalized;
                }
                return null; // Not a tracked boss
        }
    }

    /**
     * Normalize custom boss name: lowercase, remove color codes, collapse spaces.
     */
    private String normalizeCustomName(String name) {
        // Remove Minecraft color codes (§x format)
        String cleaned = name.replaceAll("§[0-9a-f]", "");
        // Lowercase and remove extra spaces
        return cleaned.toLowerCase().replaceAll("\\s+", "_");
    }

    /**
     * Get current tier (1-indexed: 0 = not defeated, 1 = defeated once, etc.).
     */
    public int getTier(String bossKey) {
        return bossDefeatCounts.getOrDefault(bossKey, 0);
    }

    /**
     * Increment defeat count and return new tier.
     */
    public int incrementDefeatCount(String bossKey) {
        int newCount = bossDefeatCounts.getOrDefault(bossKey, 0) + 1;
        bossDefeatCounts.put(bossKey, newCount);
        return newCount;
    }

    /**
     * Compute HP multiplier for a given tier.
     * Tier 0: 1.0x, Tier 1: 1.15x, Tier 2: 1.30x, etc. (capped at 5.0x).
     */
    public double getHealthMultiplier(int tier) {
        double multiplier = 1.0 + (tier * MULTIPLIER_PER_TIER);
        return Math.min(multiplier, MULTIPLIER_CAP);
    }

    /**
     * Compute damage multiplier for a given tier (similar logic).
     */
    public double getDamageMultiplier(int tier) {
        double multiplier = 1.0 + (tier * DAMAGE_MULTIPLIER_PER_TIER);
        return Math.min(multiplier, MULTIPLIER_CAP);
    }

    /**
     * Load all boss data from storage (called on plugin enable).
     */
    public void loadFromStorage(Map<String, Integer> data) {
        this.bossDefeatCounts.putAll(data);
    }

    /**
     * Export current data for persistence (called on plugin disable).
     */
    public Map<String, Integer> exportData() {
        return new HashMap<>(bossDefeatCounts);
    }

    /**
     * Reset a specific boss's tier (for testing/admin).
     */
    public void resetBoss(String bossKey) {
        bossDefeatCounts.remove(bossKey);
    }

    /**
     * Clear all boss data (for testing).
     */
    public void clearAll() {
        bossDefeatCounts.clear();
    }
}
