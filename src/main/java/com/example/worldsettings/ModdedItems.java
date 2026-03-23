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
        DROP_CHANCES.put("Zombie Arm", 5.0);
        DROP_CHANCES.put("Reinforced Bones", 3.0);
        DROP_CHANCES.put("Spider Legs", 5.0);
        DROP_CHANCES.put("Blood Shard", 2.0);
        DROP_CHANCES.put("Enchanted Pearl", 2.0);
        DROP_CHANCES.put("Enriched Bone Meal", 15.0);
        DROP_CHANCES.put("Thermal Gunpowder", 80.0);
        DROP_CHANCES.put("Explosive Charge", 100.0);
        DROP_CHANCES.put("Copper Staff", 0.0);
        DROP_CHANCES.put("Copper Rod", 0.0);
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
     * Creates a Zombie Arm item.
     * Texture: Custom model data 1
     * Uses: Deals extra damage to zombies/zombie variants
     * Drop Chance: 5.0%
     */
    public static ItemStack createZombieArm() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Zombie Arm");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A grotesque arm torn from a zombie.",
            ChatColor.BLUE + "Deals extra damage to zombies and their variants.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Zombie Arm") + "%"
        ));
        meta.setCustomModelData(1);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Reinforced Bones item.
     * Texture: Custom model data 2
     * Uses: Can be combined with a bow to create a Reinforced Bow
     * Drop Chance: 3.0%
     */
    public static ItemStack createReinforcedBones() {
        ItemStack item = new ItemStack(Material.BONE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Reinforced Bones");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Strong bones reinforced with magic.",
            ChatColor.BLUE + "Combine with a bow to create a Reinforced Bow.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Reinforced Bones") + "%"
        ));
        meta.setCustomModelData(2);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Spider Legs item.
     * Texture: Custom model data 3
     * Uses: Can be used to craft Spider Boots
     * Drop Chance: 5.0%
     */
    public static ItemStack createSpiderLegs() {
        ItemStack item = new ItemStack(Material.STRING);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Spider Legs");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Legs harvested from spiders.",
            ChatColor.BLUE + "Used to craft Spider Boots for wall climbing.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Spider Legs") + "%"
        ));
        meta.setCustomModelData(3);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Blood Shard item.
     * Texture: Custom model data 4
     * Uses: Can be crafted into Blood Orb (9 required)
     * Drop Chance: 2.0%
     */
    public static ItemStack createBloodShard() {
        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Blood Shard");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A shard infused with undead blood.",
            ChatColor.RED + "Collect 9 to craft a Blood Orb.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Blood Shard") + "%"
        ));
        meta.setCustomModelData(4);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Enchanted Pearl item.
     * Texture: Custom model data 5
     * Uses: Teleports like ender pearl but without fall damage
     * Drop Chance: 2.0%
     */
    public static ItemStack createEnchantedPearl() {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Enchanted Pearl");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A pearl infused with teleportation magic.",
            ChatColor.BLUE + "Teleports like an ender pearl but without fall damage.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Enchanted Pearl") + "%"
        ));
        meta.setCustomModelData(5);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Enriched Bone Meal item.
     * Texture: Custom model data 6
     * Uses: Grows crops faster and without water source
     * Drop Chance: 15.0%
     */
    public static ItemStack createEnrichedBoneMeal() {
        ItemStack item = new ItemStack(Material.BONE_MEAL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Enriched Bone Meal");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Supercharged bone meal with enhanced growth properties.",
            ChatColor.GREEN + "Grows crops much faster and works without water.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Enriched Bone Meal") + "%"
        ));
        meta.setCustomModelData(6);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Thermal Gunpowder item.
     * Texture: Custom model data 7
     * Uses: Can be crafted with arrows to make Exploding Arrows
     * Drop Chance: 80.0%
     */
    public static ItemStack createThermalGunpowder() {
        ItemStack item = new ItemStack(Material.GUNPOWDER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Thermal Gunpowder");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Unstable gunpowder with thermal properties.",
            ChatColor.RED + "Combine with arrows to create Exploding Arrows.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Thermal Gunpowder") + "%"
        ));
        meta.setCustomModelData(7);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Explosive Charge item.
     * Texture: Custom model data 8
     * Uses: Crafting ingredient for Copper Staff
     * Drop Chance: 100% from Charged Creepers
     */
    public static ItemStack createExplosiveCharge() {
        ItemStack item = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Explosive Charge");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A volatile charge recovered from a charged creeper.",
            ChatColor.GREEN + "Use in Copper Staff crafting.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Explosive Charge") + "%"
        ));
        meta.setCustomModelData(8);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Copper Staff item.
     * Texture: Custom model data 21
     * Uses: Heavy melee staff with lightning projectile
     * Attack: Diamond Axe (9)
     * Speed: Faster than sword
     */
    public static ItemStack createCopperStaff() {
        ItemStack item = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Copper Staff");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A staff that crackles with explosive energy.",
            ChatColor.GREEN + "Deals diamond axe damage with fast swing speed.",
            ChatColor.BLUE + "Can be enchanted like a sword + channeling."));

        meta.addAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE,
            new org.bukkit.attribute.AttributeModifier(java.util.UUID.randomUUID(), "generic.attackDamage", 9.0,
                org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, org.bukkit.inventory.EquipmentSlot.HAND));
        meta.addAttributeModifier(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED,
            new org.bukkit.attribute.AttributeModifier(java.util.UUID.randomUUID(), "generic.attackSpeed", 2.0,
                org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER, org.bukkit.inventory.EquipmentSlot.HAND));

        meta.setCustomModelData(22);
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

    /**
     * Creates a Reinforced Bow item.
     * Texture: Custom model data 16
     * Uses: Deals more damage than a regular bow, can be enchanted
     * Crafted from Reinforced Bones + Bow
     */
    public static ItemStack createReinforcedBow() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Reinforced Bow");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A bow reinforced with magical bones.",
            ChatColor.RED + "Deals more damage than a regular bow.",
            ChatColor.BLUE + "Can be enchanted like a regular bow."
        ));
        meta.setCustomModelData(16);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Spider Boots item.
     * Texture: Custom model data 17
     * Uses: Allows climbing walls like spiders, can be enchanted like regular boots
     * Crafted from Diamond Boots + Spider Legs
     */
    public static ItemStack createSpiderBoots() {
        ItemStack item = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Spider Boots");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Boots enhanced with spider legs.",
            ChatColor.BLUE + "Allows climbing walls like a spider when sneaking.",
            ChatColor.GREEN + "Can be enchanted like regular boots."
        ));
        meta.setCustomModelData(17);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Blood Orb item.
     * Texture: Custom model data 18
     * Uses: Can start Blood Moon event at night
     * Crafted from 9 Blood Shards
     */
    public static ItemStack createBloodOrb() {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Blood Orb");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "An orb pulsing with dark energy.",
            ChatColor.RED + "Right-click at night to start Blood Moon event.",
            ChatColor.DARK_PURPLE + "Unleash the undead horde!"
        ));
        meta.setCustomModelData(18);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Super Enchantment Table item.
     * Texture: Custom model data 19
     * Uses: Can upgrade enchantments past their max level
     * Crafted from Enchanting Table + Diamonds + Enchanted Pearls
     */
    public static ItemStack createSuperEnchantmentTable() {
        ItemStack item = new ItemStack(Material.ENCHANTING_TABLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Super Enchantment Table");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "An enhanced enchanting table with limitless power.",
            ChatColor.LIGHT_PURPLE + "Can upgrade enchantments beyond normal limits.",
            ChatColor.DARK_PURPLE + "Place and use like a regular enchanting table."
        ));
        meta.setCustomModelData(19);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Exploding Arrow item.
     * Texture: Custom model data 20
     * Uses: Creates explosion on impact with mobs
     * Crafted from Arrow + Thermal Gunpowder
     */
    public static ItemStack createExplodingArrow() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Exploding Arrow");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "An arrow tipped with thermal gunpowder.",
            ChatColor.RED + "Creates a small explosion on impact, damaging nearby mobs.",
            ChatColor.DARK_RED + "Does not destroy blocks."
        ));
        meta.setCustomModelData(20);
        item.setItemMeta(meta);
        return item;
    }
}