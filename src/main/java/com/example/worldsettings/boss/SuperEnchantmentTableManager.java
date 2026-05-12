package com.example.worldsettings.boss;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manager for Super Enchantment Table items, recipes, and location tracking.
 * Handles item creation, recipe registration, and progression checks.
 */
public class SuperEnchantmentTableManager {
    private final WorldSettingsPlugin plugin;
    private final Set<String> placedSuperTableLocations;

    public SuperEnchantmentTableManager(WorldSettingsPlugin plugin) {
        this.plugin = plugin;
        this.placedSuperTableLocations = new HashSet<>();
    }

    /**
     * Create a Super Enchantment Table item with custom display and lore.
     */
    public ItemStack createSuperTableItem() {
        ItemStack item = new ItemStack(Material.OBSIDIAN, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lSuper Enchantment Table");
            meta.setCustomModelData(92001);
            List<String> lore = new ArrayList<>();
            lore.add("§7A powerful enchantment station");
            lore.add("§7Requires Void Essence & Dragon Core");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Create a Void Essence item dropped from void mobs.
     */
    public ItemStack createVoidEssenceItem() {
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§5Void Essence");
            meta.setCustomModelData(92011);
            List<String> lore = new ArrayList<>();
            lore.add("§7Rare drop from The Void");
            lore.add("§d✧ Mystical Item");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Create a Dragon Core item dropped from End Dragon death.
     */
    public ItemStack createDragonCoreItem() {
        ItemStack item = new ItemStack(Material.DRAGON_BREATH, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§cDragon Core");
            meta.setCustomModelData(92012);
            List<String> lore = new ArrayList<>();
            lore.add("§7Essence of the End Dragon");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Register the Super Enchantment Table recipe.
     */
    public void registerSuperTableRecipe() {
        ItemStack recipe = createSuperTableItem();
        NamespacedKey key = new NamespacedKey(plugin, "super_enchantment_table");
        ShapedRecipe shapedRecipe = new ShapedRecipe(key, recipe);
        
        shapedRecipe.shape(
            "VDV",
            "DOS",
            "VDV"
        );
        shapedRecipe.setIngredient('V', Material.VOID_AIR);  // Placeholder; use Void Essence in real scenario
        shapedRecipe.setIngredient('D', Material.DRAGON_BREATH);
        shapedRecipe.setIngredient('O', Material.OBSIDIAN);
        shapedRecipe.setIngredient('S', Material.ENCHANTING_TABLE);
        
        Bukkit.addRecipe(shapedRecipe);
    }

    /**
     * Track a placed Super Enchantment Table location (format: world_x_y_z).
     */
    public void registerPlacedSuperTable(String location) {
        placedSuperTableLocations.add(location);
    }

    /**
     * Check if a location has a registered Super Enchantment Table.
     */
    public boolean isSuperTableLocation(String location) {
        return placedSuperTableLocations.contains(location);
    }

    /**
     * Check if player has unlocked progression (defeated End Dragon).
     */
    public boolean isProgressionUnlocked(Player player) {
        NamespacedKey endKey = NamespacedKey.minecraft("end/root");
        Advancement endAdvancement = Bukkit.getAdvancement(endKey);
        
        if (endAdvancement == null) {
            return false;
        }
        
        return player.getAdvancementProgress(endAdvancement).isDone();
    }
}
