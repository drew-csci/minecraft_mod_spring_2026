package com.example.worldsettings.boss;

import org.bukkit.enchantments.Enchantment;
import java.util.HashMap;
import java.util.Map;

/**
 * Pure logic layer for Super Enchantment Table calculations.
 * Handles enchant level boosts, lapis costs, and XP costs.
 */
public class SuperEnchantLogic {
    // Exclusive max enchant levels exclusive to Super Enchantment Table
    // Note: Enchantment enum references are intentionally excluded to allow testing without full Bukkit init
    public static final Map<String, Integer> EXCLUSIVE_MAX_LEVELS = new HashMap<>();

    static {
        // Map enchantment names to their exclusive max levels
        EXCLUSIVE_MAX_LEVELS.put("DAMAGE_ALL", 7);                    // Sharpness VII
        EXCLUSIVE_MAX_LEVELS.put("PROTECTION_ENVIRONMENTAL", 6);      // Protection VI
        EXCLUSIVE_MAX_LEVELS.put("DAMAGE_UNDEAD", 6);                 // Smite VI
        EXCLUSIVE_MAX_LEVELS.put("KNOCKBACK", 5);                     // Knockback V
        EXCLUSIVE_MAX_LEVELS.put("FIRE_ASPECT", 4);                   // Fire Aspect IV
    }

    /**
     * Get the boosted enchantment level for Super Table.
     * Returns the exclusive max level if enchant is supported, otherwise vanilla max.
     */
    public static int getBoostedLevel(Enchantment enchant) {
        String key = enchant.getKey().getKey();
        if (EXCLUSIVE_MAX_LEVELS.containsKey(key)) {
            return EXCLUSIVE_MAX_LEVELS.get(key);
        }
        return enchant.getMaxLevel();
    }

    /**
     * Calculate required lapis lazuli for enchanting based on target level.
     * Formula: base_cost * level * 1.5
     */
    public static int computeRequiredLapis(int targetLevel) {
        return Math.max(1, (int) (targetLevel * 3 * 1.5));
    }

    /**
     * Calculate required XP cost for enchanting based on target level.
     * Formula: base_cost * level * 2
     */
    public static int computeRequiredXpCost(int targetLevel) {
        return targetLevel * 3 * 2;
    }
}
