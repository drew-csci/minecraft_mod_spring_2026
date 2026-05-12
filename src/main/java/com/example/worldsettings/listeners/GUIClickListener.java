package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Handles click events inside the settings GUI.
 */
public class GUIClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null) return;
        if (!event.getView().getTitle().equals(SettingsGUI.MENU_TITLE)) return;

        event.setCancelled(true); // Prevent taking items

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        WorldSettings settings = WorldSettingsPlugin.getInstance().getWorldSettings();

        int slot = event.getRawSlot();
        boolean isRightClick = event.getClick() == ClickType.RIGHT;

        switch (slot) {
            // ── Left Column ─────────────────────────────────────────────
            case SettingsGUI.SLOT_POST_END_WORLD:
                settings.togglePostEndWorld();
                notify(player, "Post-End World", settings.isPostEndWorld());
                break;

            case SettingsGUI.SLOT_POST_END_DIFFICULTY:
                if (isRightClick) settings.decreasePostEndDifficulty();
                else              settings.increasePostEndDifficulty();
                notifyValue(player, "Post-End Difficulty", settings.getPostEndDifficultyBoost() + "x");
                break;

            case SettingsGUI.SLOT_NEW_BOSS:
                settings.toggleNewBoss();
                notify(player, "New Boss", settings.isNewBoss());
                break;

            case SettingsGUI.SLOT_HORDE_EVENTS:
                settings.toggleHordeEvents();
                notify(player, "Horde Events", settings.isHordeEvents());
                break;

            case SettingsGUI.SLOT_MAX_HORDE_SIZE:
                if (isRightClick) settings.decreaseHordeSize();
                else              settings.increaseHordeSize();
                notifyValue(player, "Max Horde Size", String.valueOf(settings.getMaximumHordeSize()));
                break;

            case SettingsGUI.SLOT_ENHANCED_MOBS:
                settings.toggleEnhancedMobs();
                notify(player, "Enhanced Mobs", settings.isEnhancedMobs());
                break;

            // ── Right Column ────────────────────────────────────────────
            case SettingsGUI.SLOT_DIFFICULTY_LEVEL:
                settings.cycleDifficulty();
                notifyValue(player, "Difficulty", settings.getDifficultyLevel().display());
                break;

            case SettingsGUI.SLOT_BLOOD_MOON_EVENTS:
                settings.toggleBloodMoonEvents();
                notify(player, "Blood Moon Events", settings.isBloodMoonEvents());
                break;

            case SettingsGUI.SLOT_BLOOD_MOON_SPAWN:
                if (isRightClick) settings.decreaseBloodMoonSpawn();
                else              settings.increaseBloodMoonSpawn();
                notifyValue(player, "Blood Moon Spawn", settings.getBloodMoonSpawnMultiplier() + "%");
                break;

            case SettingsGUI.SLOT_FIRST_BLOOD_MOON:
                settings.cycleFirstBloodMoon();
                notifyValue(player, "1st Blood Moon", "Day " + settings.getFirstBloodMoonDay());
                break;

            case SettingsGUI.SLOT_BLOOD_MOON_CHANCE:
                if (isRightClick) settings.decreaseBloodMoonChance();
                else              settings.increaseBloodMoonChance();
                notifyValue(player, "Blood Moon Chance", settings.getBloodMoonChancePercent() + "%");
                break;

            case SettingsGUI.SLOT_ENHANCED_LOOT:
                settings.toggleEnhancedLootDrops();
                notify(player, "Enhanced Loot Drops", settings.isEnhancedLootDrops());
                break;

            default:
                return; // Clicked filler — do nothing, no sound
        }

        WorldSettingsPlugin.getInstance().saveSettingsToConfig();

        // Refresh the GUI to show updated values
        SettingsGUI.openMainMenu(player);
    }

    private void notify(Player player, String setting, boolean enabled) {
        String status = enabled
            ? ChatColor.GREEN + "ON"
            : ChatColor.RED + "OFF";
        player.sendMessage(ChatColor.GOLD + "[WorldSettings] "
            + ChatColor.YELLOW + setting + ": " + status);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
    }

    private void notifyValue(Player player, String setting, String value) {
        player.sendMessage(ChatColor.GOLD + "[WorldSettings] "
            + ChatColor.YELLOW + setting + ": " + ChatColor.GREEN + value);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
    }
}
