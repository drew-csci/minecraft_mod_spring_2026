package com.example.worldsettings.superenchant;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SuperEnchantmentManager {

    public static NamespacedKey SOULBOUND_KEY;
    public static NamespacedKey TEMPORAL_STRIKE_KEY;

    public static void initialize(WorldSettingsPlugin plugin) {
        SOULBOUND_KEY = new NamespacedKey(plugin, "soulbound");
        TEMPORAL_STRIKE_KEY = new NamespacedKey(plugin, "temporal_strike");
    }

    public enum EnchantmentOption {
        SHARPNESS_VII("Sharpness VII", Enchantment.DAMAGE_ALL, 7, 16, 35,
            "Blade power beyond the ordinary."),
        PROTECTION_V("Protection V", Enchantment.PROTECTION_ENVIRONMENTAL, 5, 14, 30,
            "Late-game armor strength for epic battles."),
        UNBREAKING_IV("Unbreaking IV", Enchantment.DURABILITY, 4, 10, 25,
            "Exceptional durability for endgame gear."),
        MENDING("Mending", Enchantment.MENDING, 1, 12, 40,
            "Repair your item with experience orbs."),
        SOULBOUND("Soulbound", null, 0, 18, 45,
            "Keeps this item on death if you retain it."),
        TEMPORAL_STRIKE("Temporal Strike", Enchantment.DAMAGE_ALL, 5, 20, 50,
            "A severe hit that leaves foes slowed.");

        private final String displayName;
        private final Enchantment enchantment;
        private final int level;
        private final int lapisCost;
        private final int xpCost;
        private final String description;

        EnchantmentOption(String displayName,
                          Enchantment enchantment,
                          int level,
                          int lapisCost,
                          int xpCost,
                          String description) {
            this.displayName = displayName;
            this.enchantment = enchantment;
            this.level = level;
            this.lapisCost = lapisCost;
            this.xpCost = xpCost;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public int getLevel() {
            return level;
        }

        public int getLapisCost() {
            return lapisCost;
        }

        public int getXpCost() {
            return xpCost;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCustomEffect() {
            return this == SOULBOUND || this == TEMPORAL_STRIKE;
        }
    }

    public static ItemStack createSuperEnchantmentTable() {
        return createCustomItem(Material.ENCHANTING_TABLE,
            ChatColor.LIGHT_PURPLE + "Super Enchantment Table",
            Arrays.asList(
                ChatColor.GRAY + "A late-game upgrade to the classic table.",
                ChatColor.GRAY + "Right-click to access powerful and exclusive enchants.",
                ChatColor.DARK_GRAY + "Requires rare mob drops and lapis."
            ),
            1000005);
    }

    public static ItemStack createAncientArcaneDust() {
        return createCustomItem(Material.GLOWSTONE_DUST,
            ChatColor.AQUA + "Ancient Arcane Dust",
            Collections.singletonList(ChatColor.GRAY + "A shimmering dust collected from ancient foes."),
            1000001);
    }

    public static ItemStack createVoidCrystal() {
        return createCustomItem(Material.AMETHYST_SHARD,
            ChatColor.DARK_PURPLE + "Void Crystal",
            Collections.singletonList(ChatColor.GRAY + "A rare crystal imbued with phantom energy."),
            1000002);
    }

    public static ItemStack createEnchantedBoneShard() {
        return createCustomItem(Material.BONE,
            ChatColor.WHITE + "Enchanted Bone Shard",
            Collections.singletonList(ChatColor.GRAY + "The ectoplasm of a powerful undead warrior."),
            1000003);
    }

    public static ItemStack createReinforcedEnchantmentCore() {
        return createCustomItem(Material.ENCHANTED_BOOK,
            ChatColor.GOLD + "Reinforced Enchantment Core",
            Arrays.asList(
                ChatColor.GRAY + "A rare binding core used to craft the Super Enchantment Table.",
                ChatColor.GRAY + "Forged from enchanted bone, void crystal, and ancient dust."
            ),
            1000004);
    }

    public static boolean isSuperEnchantmentTable(ItemStack item) {
        return isNamedCustomItem(item, "Super Enchantment Table", 1000005);
    }

    public static boolean isAncientArcaneDust(ItemStack item) {
        return isNamedCustomItem(item, "Ancient Arcane Dust", 1000001);
    }

    public static boolean isVoidCrystal(ItemStack item) {
        return isNamedCustomItem(item, "Void Crystal", 1000002);
    }

    public static boolean isEnchantedBoneShard(ItemStack item) {
        return isNamedCustomItem(item, "Enchanted Bone Shard", 1000003);
    }

    public static boolean isReinforcedEnchantmentCore(ItemStack item) {
        return isNamedCustomItem(item, "Reinforced Enchantment Core", 1000004);
    }

    public static boolean canEnchantItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        String name = item.getType().name().toUpperCase(Locale.ROOT);
        return name.contains("SWORD")
            || name.contains("AXE")
            || name.contains("PICKAXE")
            || name.contains("HOE")
            || name.contains("SHOVEL")
            || name.contains("BOW")
            || name.contains("CROSSBOW")
            || name.contains("HELMET")
            || name.contains("CHESTPLATE")
            || name.contains("LEGGINGS")
            || name.contains("BOOTS")
            || name.contains("SHIELD")
            || name.contains("ELYTRA")
            || name.contains("TRIDENT");
    }

    public static boolean playerHasRequiredLapis(Player player, int required) {
        return countItem(player.getInventory(), Material.LAPIS_LAZULI) >= required;
    }

    public static void removeLapis(Player player, int required) {
        PlayerInventory inventory = player.getInventory();
        int remaining = required;
        for (int i = 0; i < inventory.getSize() && remaining > 0; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null || stack.getType() != Material.LAPIS_LAZULI) {
                continue;
            }
            int amount = stack.getAmount();
            if (amount <= remaining) {
                remaining -= amount;
                inventory.clear(i);
            } else {
                stack.setAmount(amount - remaining);
                inventory.setItem(i, stack);
                remaining = 0;
            }
        }
        player.updateInventory();
    }

    public static boolean playerHasRequiredLevels(Player player, int required) {
        return player.getLevel() >= required;
    }

    public static boolean applyEnchantmentOption(Player player, ItemStack item, EnchantmentOption option) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        if (option.isCustomEffect()) {
            if (option == EnchantmentOption.SOULBOUND) {
                if (hasSoulbound(item)) {
                    return false;
                }
                addPersistentBoolean(meta, SOULBOUND_KEY, true);
                addLoreLine(meta, ChatColor.DARK_PURPLE + "Soulbound: Returns to you after death.");
            } else if (option == EnchantmentOption.TEMPORAL_STRIKE) {
                if (hasTemporalStrike(item)) {
                    return false;
                }
                addPersistentBoolean(meta, TEMPORAL_STRIKE_KEY, true);
                addLoreLine(meta, ChatColor.DARK_PURPLE + "Temporal Strike: Slows struck enemies.");
            }
        } else {
            if (option.getEnchantment() != null) {
                meta.addEnchant(option.getEnchantment(), option.getLevel(), true);
            }
        }

        if (!meta.hasDisplayName()) {
            meta.setDisplayName(ChatColor.GOLD + "Super " + item.getType().name().toLowerCase().replace('_', ' '));
        }

        addLoreLine(meta, ChatColor.GRAY + option.getDisplayName());
        item.setItemMeta(meta);
        return true;
    }

    public static boolean hasSoulbound(ItemStack item) {
        ItemMeta meta = item == null ? null : item.getItemMeta();
        if (meta == null) {
            return false;
        }
        return meta.getPersistentDataContainer().has(SOULBOUND_KEY, PersistentDataType.BYTE);
    }

    public static boolean hasTemporalStrike(ItemStack item) {
        ItemMeta meta = item == null ? null : item.getItemMeta();
        if (meta == null) {
            return false;
        }
        return meta.getPersistentDataContainer().has(TEMPORAL_STRIKE_KEY, PersistentDataType.BYTE);
    }

    public static ItemStack createCustomItem(Material material, String displayName, List<String> lore, int modelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            meta.setCustomModelData(modelData);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static int countItem(PlayerInventory inventory, Material material) {
        return Arrays.stream(inventory.getContents())
            .filter(Objects::nonNull)
            .filter(item -> item.getType() == material)
            .mapToInt(ItemStack::getAmount)
            .sum();
    }

    private static boolean isNamedCustomItem(ItemStack item, String expectedName, int modelData) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName() || !meta.hasCustomModelData()) {
            return false;
        }
        return meta.getDisplayName().equals(expectedName)
            && meta.getCustomModelData() == modelData;
    }

    private static void addLoreLine(ItemMeta meta, String line) {
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        if (!lore.contains(line)) {
            lore.add(line);
        }
        meta.setLore(lore);
    }

    private static void addPersistentBoolean(ItemMeta meta, NamespacedKey key, boolean value) {
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) (value ? 1 : 0));
    }
}
