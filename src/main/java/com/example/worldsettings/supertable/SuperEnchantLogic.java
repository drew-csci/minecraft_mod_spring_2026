package com.example.worldsettings.supertable;

/**
 * Utility logic for "super enchantment" calculations.
 */
public class SuperEnchantLogic {

	private SuperEnchantLogic() {
		// Utility class
	}

	public static int clampLevel(int level, int minLevel, int maxLevel) {
		if (maxLevel < minLevel) {
			throw new IllegalArgumentException("maxLevel must be >= minLevel");
		}
		return Math.max(minLevel, Math.min(maxLevel, level));
	}

	public static int computeBoostedLevel(int baseLevel, int bonusLevels, int maxAllowedLevel) {
		int safeBase = Math.max(1, baseLevel);
		int safeBonus = Math.max(0, bonusLevels);
		return clampLevel(safeBase + safeBonus, 1, Math.max(1, maxAllowedLevel));
	}

	public static int computeExtraXpCost(int bonusLevels, double costMultiplier) {
		int safeBonus = Math.max(0, bonusLevels);
		if (safeBonus == 0) {
			return 0;
		}
		double safeMultiplier = Math.max(0.0, costMultiplier);
		return Math.max(1, (int) Math.ceil(safeBonus * safeMultiplier));
	}
}
