package com.example.worldsettings.gui;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builds the "Customize World Settings" chest GUI.
 *
 * Layout (54-slot double chest = 6 rows x 9 columns):
 *
 *   Row 0: Title bar (gray stained glass)
 *   Row 1: [slot 9-12]  Left Item 1   |  divider  |  [slot 14-17]  Right Item 1
 *   Row 2: [slot 18-21] Left Item 2   |  divider  |  [slot 23-26]  Right Item 2
 *   Row 3: [slot 27-30] Left Item 3   |  divider  |  [slot 32-35]  Right Item 3
 *   Row 4: [slot 36-39] Left Item 4   |  divider  |  [slot 41-44]  Right Item 4
 *   Row 5: [slot 45-48] Left Item 5   |  divider  |  [slot 50-53]  Right Item 5
 *
 * Each setting occupies one clickable slot per row (left col + right col).
 */
public class SettingsGUI {

    public static final String MENU_TITLE = ChatColor.DARK_GRAY + "Customize World Settings";
    public static final int MENU_SIZE = 54; // 6 rows

    // Slot positions for left-column items (rows 1-5, column 1)
    public static final int SLOT_POST_END_WORLD       = 10;
    public static final int SLOT_POST_END_DIFFICULTY   = 19;
    public static final int SLOT_NEW_BOSS              = 28;
    public static final int SLOT_HORDE_EVENTS          = 37;
    public static final int SLOT_MAX_HORDE_SIZE        = 46;

    // Slot positions for right-column items (rows 1-5, column 7)
    public static final int SLOT_DIFFICULTY_LEVEL      = 14;
    public static final int SLOT_BLOOD_MOON_EVENTS     = 23;
    public static final int SLOT_BLOOD_MOON_SPAWN      = 32;
    public static final int SLOT_FIRST_BLOOD_MOON      = 41;
    public static final int SLOT_BLOOD_MOON_CHANCE     = 50;

    /**
     * Opens the settings menu for a player.
     */
    public static void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, MENU_SIZE, MENU_TITLE);
        WorldSettings s = WorldSettingsPlugin.getInstance().getWorldSettings();

        // ── Fill background with dark gray glass panes ──────────────────
        ItemStack filler = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < MENU_SIZE; i++) {
            gui.setItem(i, filler);
        }

        // ── Title row (slot 4 = center of row 0) ───────────────────────
        gui.setItem(4, createItem(Material.NETHER_STAR,
            ChatColor.GOLD + "" + ChatColor.BOLD + "World Settings",
            Arrays.asList(
                ChatColor.GRAY + "Basic Settings",
                ChatColor.DARK_GRAY + "Click items to toggle"
            )));

        // ── Center column divider (column 4, slots 4/13/22/31/40/49) ───
        ItemStack divider = createItem(Material.BLACK_STAINED_GLASS_PANE, " ", null);
        for (int row = 1; row <= 5; row++) {
            gui.setItem(row * 9 + 4, divider);
        }

        // ── LEFT COLUMN ─────────────────────────────────────────────────

        // 1. Post-End World: ON/OFF
        gui.setItem(SLOT_POST_END_WORLD, createToggleItem(
            "Post-End World", s.isPostEndWorld(),
            Material.END_STONE, Material.STONE));

        // 2. Post-End Difficulty Boost: slider
        gui.setItem(SLOT_POST_END_DIFFICULTY, createItem(
            Material.IRON_SWORD,
            ChatColor.YELLOW + "Post-End Difficulty Boost",
            Arrays.asList(
                ChatColor.WHITE + "Current: " + ChatColor.GREEN + s.getPostEndDifficultyBoost() + "x",
                ChatColor.GRAY + "Range: 1.0x - 3.0x",
                "",
                ChatColor.DARK_GRAY + "Left-click: +0.5  |  Right-click: -0.5"
            )));

        // 3. New Boss: ON/OFF
        gui.setItem(SLOT_NEW_BOSS, createToggleItem(
            "New Boss", s.isNewBoss(),
            Material.WITHER_SKELETON_SKULL, Material.SKELETON_SKULL));

        // 4. Horde Events: ON/OFF
        gui.setItem(SLOT_HORDE_EVENTS, createToggleItem(
            "Horde Events", s.isHordeEvents(),
            Material.ZOMBIE_HEAD, Material.PLAYER_HEAD,
            Arrays.asList(
                ChatColor.RED + "Performance: Moderate",
                ChatColor.GRAY + "Spawns more mobs when enabled"
            )));

        // 5. Maximum Horde Size: slider 10-100
        gui.setItem(SLOT_MAX_HORDE_SIZE, createItem(
            Material.IRON_AXE,
            ChatColor.YELLOW + "Maximum Horde Size",
            Arrays.asList(
                ChatColor.WHITE + "Current: " + ChatColor.GREEN + s.getMaximumHordeSize(),
                ChatColor.GRAY + "Range: 10 - 100",
                ChatColor.RED + "Performance: High at larger values",
                "",
                ChatColor.DARK_GRAY + "Left-click: +10  |  Right-click: -10"
            )));

        // ── RIGHT COLUMN ────────────────────────────────────────────────

        // 6. Difficulty Level: Easy/Medium/Hard
        Material diffMat;
        switch (s.getDifficultyLevel()) {
            case HARD:   diffMat = Material.DIAMOND_SWORD; break;
            case MEDIUM: diffMat = Material.IRON_SWORD;    break;
            default:     diffMat = Material.WOODEN_SWORD;  break;
        }
        gui.setItem(SLOT_DIFFICULTY_LEVEL, createItem(
            diffMat,
            ChatColor.YELLOW + "Difficulty Level",
            Arrays.asList(
                ChatColor.WHITE + "Current: " + ChatColor.GREEN + s.getDifficultyLevel().display(),
                "",
                ChatColor.DARK_GRAY + "Click to cycle: Easy > Medium > Hard"
            )));

        // 7. Blood Moon Events: ON/OFF
        gui.setItem(SLOT_BLOOD_MOON_EVENTS, createToggleItem(
            "Blood Moon Events", s.isBloodMoonEvents(),
            Material.REDSTONE_BLOCK, Material.COAL_BLOCK));

        // 8. Blood Moon Spawn Multiplier: % slider
        gui.setItem(SLOT_BLOOD_MOON_SPAWN, createItem(
            Material.SPAWNER,
            ChatColor.YELLOW + "Blood Moon Spawn Multiplier",
            Arrays.asList(
                ChatColor.WHITE + "Current: " + ChatColor.GREEN + s.getBloodMoonSpawnMultiplier() + "%",
                ChatColor.GRAY + "Range: 25% - 500%",
                ChatColor.RED + "Performance: High at values above 100%",
                "",
                ChatColor.DARK_GRAY + "Left-click: +25%  |  Right-click: -25%"
            )));

        // 9. 1st Blood Moon timing
        gui.setItem(SLOT_FIRST_BLOOD_MOON, createItem(
            Material.CLOCK,
            ChatColor.YELLOW + "1st Blood Moon",
            Arrays.asList(
                ChatColor.WHITE + "Current: " + ChatColor.GREEN + "Day " + s.getFirstBloodMoonDay(),
                "",
                ChatColor.DARK_GRAY + "Click to cycle: Day 1 > 3 > 5"
            )));

        // 10. Blood Moon Chance: % per night
        gui.setItem(SLOT_BLOOD_MOON_CHANCE, createItem(
            Material.ENDER_EYE,
            ChatColor.YELLOW + "Blood Moon Chance",
            Arrays.asList(
                ChatColor.WHITE + "Current: " + ChatColor.GREEN + s.getBloodMoonChancePercent() + "% per night",
                ChatColor.GRAY + "Range: 0% - 100%",
                "",
                ChatColor.DARK_GRAY + "Left-click: +5%  |  Right-click: -5%"
            )));

        player.openInventory(gui);
    }

    // ── Helper Methods ──────────────────────────────────────────────────

    static List<String> buildToggleLore(boolean enabled, List<String> extraLore) {
        String status = enabled
            ? ChatColor.GREEN + "" + ChatColor.BOLD + "ON"
            : ChatColor.RED + "" + ChatColor.BOLD + "OFF";
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Status: " + status);
        if (extraLore != null && !extraLore.isEmpty()) {
            lore.add("");
            lore.addAll(extraLore);
        }
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "Click to toggle");
        return lore;
    }

    /**
     * Creates a toggle-style item: green name + ON material, or red + OFF material.
     */
    private static ItemStack createToggleItem(String name, boolean enabled,
                                               Material onMat, Material offMat) {
        return createToggleItem(name, enabled, onMat, offMat, null);
    }

    private static ItemStack createToggleItem(String name, boolean enabled,
                                               Material onMat, Material offMat,
                                               List<String> extraLore) {
        List<String> lore = buildToggleLore(enabled, extraLore);
        return createItem(
            enabled ? onMat : offMat,
            ChatColor.YELLOW + name,
            lore);
    }

    /**
     * Creates a named ItemStack with optional lore lines.
     */
    public static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null) {
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
