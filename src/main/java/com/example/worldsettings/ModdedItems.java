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
        DROP_CHANCES.put("Purifying Powder", 10.0);
        DROP_CHANCES.put("Bloodmoon Ingot", 7.0);
        DROP_CHANCES.put("Upgrade Template", 6.5);
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
     * Creates Purifying Powder item.
     * Texture: Custom model data 9
     * Uses: Craft with furnace to create Purifying Furnace
     * Drop Chance: 10.0% from Witches
     */
    public static ItemStack createPurifyingPowder() {
        ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Purifying Powder");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A magical powder that purifies materials.",
            ChatColor.LIGHT_PURPLE + "Craft with a furnace to create a Purifying Furnace.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Purifying Powder") + "%"
        ));
        meta.setCustomModelData(9);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Bloodmoon Ingot item.
     * Texture: Custom model data 10
     * Uses: Direct Netherite upgrade ingredient (Bloodmoon event reward)
     * Drop Chance: 7.0%
     */
    public static ItemStack createBloodmoonIngot() {
        ItemStack item = new ItemStack(Material.NETHERITE_INGOT);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Bloodmoon Ingot");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A rare ingot forged during the Bloodmoon.",
            ChatColor.RED + "Used for upgrading Netherite gear into Bloodstone.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Bloodmoon Ingot") + "%"
        ));
        meta.setCustomModelData(10);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Upgrade Template item.
     * Texture: Custom model data 11
     * Uses: Convert Netherite armor/tools into Bloodstone armor/tools (designs later)
     * Drop Chance: 6.5%
     */
    public static ItemStack createUpgradeTemplate() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Upgrade Template");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A mystical template used to craft Bloodstone upgrades.",
            ChatColor.BLUE + "Can be duplicated with Blood Orb + diamonds.",
            ChatColor.YELLOW + "Drop Chance: " + DROP_CHANCES.get("Upgrade Template") + "%"
        ));
        meta.setCustomModelData(11);
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

    /**
     * Creates Cooked Flesh item.
     * Texture: Custom model data 23
     * Uses: Food item that restores hunger without poison effect
     * Made by smelting Rotten Flesh in Purifying Furnace
     */
    public static ItemStack createCookedFlesh() {
        ItemStack item = new ItemStack(Material.COOKED_MUTTON);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Cooked Flesh");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Purified rotten flesh that is safe to eat.",
            ChatColor.GOLD + "Restores 3 hunger points.",
            ChatColor.GREEN + "Does not inflict hunger effect."
        ));
        meta.setCustomModelData(23);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates Purifying Furnace item.
     * Texture: Custom model data 24
     * Uses: Special furnace that can cook Rotten Flesh into Cooked Flesh
     * Crafted from Furnace + Purifying Powder (in 2x2 grid or crafting table)
     */
    public static ItemStack createPurifyingFurnace() {
        ItemStack item = new ItemStack(Material.FURNACE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Purifying Furnace");
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "A furnace enhanced with purifying magic.",
            ChatColor.LIGHT_PURPLE + "Smelts Rotten Flesh into safe Cooked Flesh.",
            ChatColor.BLUE + "Works like a regular furnace for all other recipes."
        ));
        meta.setCustomModelData(24);
        item.setItemMeta(meta);
        return item;
    }
}