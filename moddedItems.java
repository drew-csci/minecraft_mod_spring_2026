package com.example.worldsettings;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ModdedItems - Defines custom modded items for the Minecraft plugin.
 * Each item has unique properties including texture (via custom model data),
 * uses (described in lore), and drop chances.
 */
public class ModdedItems {

    // Map to store drop chances for each item (item name -> drop chance percentage)
    private static final Map<String, Double> DROP_CHANCES = new HashMap<>();

    static {
        DROP_CHANCES.put("Enchanted Sword", 5.0);
        DROP_CHANCES.put("Magic Wand", 3.0);
        DROP_CHANCES.put("Healing Potion", 10.0);
        DROP_CHANCES.put("Dragon Scale Armor", 2.0);
        DROP_CHANCES.put("Phoenix Feather", 4.0);
        DROP_CHANCES.put("Crystal Orb", 6.0);
        DROP_CHANCES.put("Shadow Cloak", 3.5);
        DROP_CHANCES.put("Lightning Bolt", 4.5);
        DROP_CHANCES.put("Earth Golem Core", 5.5);
        DROP_CHANCES.put("Fire Essence", 7.0);
        DROP_CHANCES.put("Ice Crystal", 6.5);
        DROP_CHANCES.put("Wind Talisman", 5.0);
        DROP_CHANCES.put("Water Pearl", 8.0);
        DROP_CHANCES.put("Light Amulet", 4.0);
        DROP_CHANCES.put("Dark Ring", 3.0);
    }

    /**
     * Gets the drop chance for a specific item.
     * @param itemName The name of the item
     * @return The drop chance as a percentage (0.0 to 100.0)
     */
    public static double getDropChance(String itemName) {
        return DROP_CHANCES.getOrDefault(itemName, 0.0);
    }

    /**
     * Creates an Enchanted Sword item.
     * Texture: Custom model data 1
     * Uses: Deals extra damage to undead mobs
     * Drop Chance: 5.0%
     */
    public static ItemStack createEnchantedSword() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Enchanted Sword");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A sword imbued with ancient magic.",
            ChatColor.BLUE + "Deals extra damage to undead creatures.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Enchanted Sword") + "%"
        ));
        meta.setCustomModelData(1);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a Magic Wand item.
     * Texture: Custom model data 2
     * Uses: Casts basic spells
     * Drop Chance: 3.0%
     */
    public static ItemStack createMagicWand() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Magic Wand");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A wand infused with magical energy.",
            ChatColor.BLUE + "Right-click to cast basic spells.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Magic Wand") + "%"
        ));
        meta.setCustomModelData(2);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a Healing Potion item.
     * Texture: Custom model data 3
     * Uses: Restores health instantly
     * Drop Chance: 10.0%
     */
    public static ItemStack createHealingPotion() {
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Healing Potion");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A potion that heals wounds instantly.",
            ChatColor.GREEN + "Restores full health when consumed.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Healing Potion") + "%"
        ));
        meta.setCustomModelData(3);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Dragon Scale Armor item.
     * Texture: Custom model data 4
     * Uses: Provides fire resistance and high defense
     * Drop Chance: 2.0%
     */
    public static ItemStack createDragonScaleArmor() {
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Dragon Scale Armor");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Armor crafted from dragon scales.",
            ChatColor.RED + "Grants fire resistance and high defense.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Dragon Scale Armor") + "%"
        ));
        meta.setCustomModelData(4);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Phoenix Feather item.
     * Texture: Custom model data 5
     * Uses: Allows temporary flight and fire immunity
     * Drop Chance: 4.0%
     */
    public static ItemStack createPhoenixFeather() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Phoenix Feather");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A feather from the legendary phoenix.",
            ChatColor.YELLOW + "Grants temporary flight and fire immunity.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Phoenix Feather") + "%"
        ));
        meta.setCustomModelData(5);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Crystal Orb item.
     * Texture: Custom model data 6
     * Uses: Reveals hidden structures and treasures
     * Drop Chance: 6.0%
     */
    public static ItemStack createCrystalOrb() {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Crystal Orb");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A mystical orb that glows with inner light.",
            ChatColor.BLUE + "Reveals hidden structures and treasures nearby.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Crystal Orb") + "%"
        ));
        meta.setCustomModelData(6);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Shadow Cloak item.
     * Texture: Custom model data 7
     * Uses: Grants invisibility and stealth
     * Drop Chance: 3.5%
     */
    public static ItemStack createShadowCloak() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Shadow Cloak");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A cloak woven from shadows.",
            ChatColor.DARK_PURPLE + "Grants invisibility and enhanced stealth.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Shadow Cloak") + "%"
        ));
        meta.setCustomModelData(7);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Lightning Bolt item.
     * Texture: Custom model data 8
     * Uses: Summons lightning strikes
     * Drop Chance: 4.5%
     */
    public static ItemStack createLightningBolt() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Lightning Bolt");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A rod charged with electrical energy.",
            ChatColor.YELLOW + "Summons lightning strikes on targets.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Lightning Bolt") + "%"
        ));
        meta.setCustomModelData(8);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Earth Golem Core item.
     * Texture: Custom model data 9
     * Uses: Summons earth golems for protection
     * Drop Chance: 5.5%
     */
    public static ItemStack createEarthGolemCore() {
        ItemStack item = new ItemStack(Material.IRON_BLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Earth Golem Core");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "The heart of an earth golem.",
            ChatColor.DARK_GREEN + "Summons earth golems to protect you.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Earth Golem Core") + "%"
        ));
        meta.setCustomModelData(9);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Fire Essence item.
     * Texture: Custom model data 10
     * Uses: Ignites enemies and creates fire barriers
     * Drop Chance: 7.0%
     */
    public static ItemStack createFireEssence() {
        ItemStack item = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Fire Essence");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Pure essence of fire.",
            ChatColor.RED + "Ignites enemies and creates fire barriers.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Fire Essence") + "%"
        ));
        meta.setCustomModelData(10);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Ice Crystal item.
     * Texture: Custom model data 11
     * Uses: Freezes enemies and creates ice platforms
     * Drop Chance: 6.5%
     */
    public static ItemStack createIceCrystal() {
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Ice Crystal");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A crystal formed from eternal ice.",
            ChatColor.BLUE + "Freezes enemies and creates ice platforms.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Ice Crystal") + "%"
        ));
        meta.setCustomModelData(11);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Wind Talisman item.
     * Texture: Custom model data 12
     * Uses: Grants speed boost and wind manipulation
     * Drop Chance: 5.0%
     */
    public static ItemStack createWindTalisman() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Wind Talisman");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A talisman infused with wind spirits.",
            ChatColor.GRAY + "Grants speed boost and wind manipulation abilities.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Wind Talisman") + "%"
        ));
        meta.setCustomModelData(12);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Water Pearl item.
     * Texture: Custom model data 13
     * Uses: Allows underwater breathing and water manipulation
     * Drop Chance: 8.0%
     */
    public static ItemStack createWaterPearl() {
        ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Water Pearl");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A pearl born from ocean depths.",
            ChatColor.BLUE + "Grants underwater breathing and water control.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Water Pearl") + "%"
        ));
        meta.setCustomModelData(13);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Light Amulet item.
     * Texture: Custom model data 14
     * Uses: Illuminates dark areas and repels darkness
     * Drop Chance: 4.0%
     */
    public static ItemStack createLightAmulet() {
        ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Light Amulet");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "An amulet that radiates pure light.",
            ChatColor.YELLOW + "Illuminates darkness and repels evil.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Light Amulet") + "%"
        ));
        meta.setCustomModelData(14);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Dark Ring item.
     * Texture: Custom model data 15
     * Uses: Enhances night vision and shadow powers
     * Drop Chance: 3.0%
     */
    public static ItemStack createDarkRing() {
        ItemStack item = new ItemStack(Material.COAL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Dark Ring");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A ring forged in darkness.",
            ChatColor.DARK_PURPLE + "Enhances night vision and shadow manipulation.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Dark Ring") + "%"
        ));
        meta.setCustomModelData(15);
        item.setItemMeta(meta);
        return item;
    }
}
