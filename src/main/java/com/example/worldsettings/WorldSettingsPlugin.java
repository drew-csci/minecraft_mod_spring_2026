package com.example.worldsettings;

import com.example.worldsettings.boss.BossScalingManager;
import com.example.worldsettings.boss.BossScalingStorage;
import com.example.worldsettings.boss.BossDefeatListener;
import com.example.worldsettings.boss.BossSpawnListener;
import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.listeners.WorldSettingsGameplayListener;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * WorldSettingsPlugin - Main entry point.
 * Provides a GUI-based world settings menu accessible via /worldsettings.
 * Also manages boss scaling with tier progression and persistence.
 */
public class WorldSettingsPlugin extends JavaPlugin {

    private static WorldSettingsPlugin instance;
    private WorldSettings worldSettings;
    private BossScalingManager bossScalingManager;
    private BossScalingStorage bossScalingStorage;

    @Override
    public void onEnable() {
        instance = this;
        worldSettings = new WorldSettings();

        saveDefaultConfig();
        reloadSettingsFromConfig();

        // Initialize boss scaling
        bossScalingStorage = new BossScalingStorage(this);
        bossScalingManager = new BossScalingManager();
        bossScalingManager.loadFromStorage(bossScalingStorage.loadData());

        // Register listeners
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSettingsGameplayListener(), this);
        getServer().getPluginManager().registerEvents(new BossDefeatListener(bossScalingManager, bossScalingStorage, this), this);
        getServer().getPluginManager().registerEvents(new BossSpawnListener(bossScalingManager), this);

        getLogger().info("========================================");
        getLogger().info(" WorldSettingsPlugin v1.0.0 enabled!");
        getLogger().info(" Boss Scaling system initialized.");
        getLogger().info(" Use /worldsettings to open the GUI.");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        // Save boss scaling data before shutdown
        if (bossScalingStorage != null && bossScalingManager != null) {
            bossScalingStorage.saveData(bossScalingManager.exportData());
        }
        getLogger().info("WorldSettingsPlugin disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("worldsettings")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("worldsettings.reload")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to reload settings.");
                    return true;
                }
                reloadConfig();
                reloadSettingsFromConfig();
                sender.sendMessage(ChatColor.GREEN + "World settings config reloaded.");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            SettingsGUI.openMainMenu(player);
            return true;
        }
        return false;
    }

    public static WorldSettingsPlugin getInstance() {
        return instance;
    }

    public WorldSettings getWorldSettings() {
        return worldSettings;
    }

    public BossScalingManager getBossScalingManager() {
        return bossScalingManager;
    }

    public BossScalingStorage getBossScalingStorage() {
        return bossScalingStorage;
    }

    public void reloadSettingsFromConfig() {
        worldSettings.loadFromConfig(getConfig());
        worldSettings.sanitizeRanges();
        worldSettings.writeToConfig(getConfig());
        saveConfig();
    }

    public void saveSettingsToConfig() {
        worldSettings.sanitizeRanges();
        worldSettings.writeToConfig(getConfig());
        saveConfig();
    }
}
