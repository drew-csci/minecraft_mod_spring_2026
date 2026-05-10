package com.example.worldsettings;

import com.example.worldsettings.progression.ProgressionManager;
import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.CraftingListener;
import com.example.worldsettings.listeners.EntityDeathListener;
import com.example.worldsettings.listeners.FurnaceListener;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.listeners.DragonEggDestructionListener;
import com.example.worldsettings.listeners.WorldSettingsGameplayListener;
import com.example.worldsettings.settings.WorldSettings;
import com.example.worldsettings.sidebar.PlayerJoinListener;
import com.example.worldsettings.sidebar.PlayerAdvancementListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldSettingsPlugin extends JavaPlugin {

    private static WorldSettingsPlugin instance;
    private WorldSettings worldSettings;
    private ProgressionManager progressionManager;

    @Override
    public void onEnable() {
        instance = this;
        worldSettings = new WorldSettings();
        progressionManager = new ProgressionManager();

        saveDefaultConfig();
        reloadSettingsFromConfig();

        // Register listeners
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSettingsGameplayListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerAdvancementListener(), this);
        getServer().getPluginManager().registerEvents(new com.example.worldsettings.listeners.HellCreeperListener(), this);
        getServer().getPluginManager().registerEvents(new com.example.worldsettings.listeners.HellZombieListener(), this);
        getServer().getPluginManager().registerEvents(new com.example.worldsettings.listeners.HellSpiderListener(), this);
        getServer().getPluginManager().registerEvents(new com.example.worldsettings.listeners.HellPhantomListener(), this);
        getServer().getPluginManager().registerEvents(new com.example.worldsettings.listeners.HellEndermanListener(), this);
        getServer().getPluginManager().registerEvents(new com.example.worldsettings.listeners.HellSkeletonListener(), this);

        // Register the dragon egg destruction listener (for Void Devourer boss spawn)
        getServer().getPluginManager().registerEvents(new DragonEggDestructionListener(), this);
        
        // Start Crimson Descent event manager and day tracker display
        new com.example.worldsettings.listeners.CrimsonDescentManager(this);
        com.example.worldsettings.listeners.DayTrackerListener.startDayTrackerTask(this);

        getLogger().info("========================================");
        getLogger().info(" WorldSettingsPlugin v1.0.0 enabled!");
        getLogger().info(" Use /worldsettings to open the GUI.");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
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

        if (command.getName().equalsIgnoreCase("progression")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            int completed = progressionManager.getCompletedAdvancementCount(player);
            int total = progressionManager.getTotalTrackedAdvancements();
            int percent = progressionManager.getProgressionPercent(player);
            String rank = progressionManager.getDifficultyLabel(percent);

            player.sendMessage("§aProgression: §e" + completed + "/" + total + " §7(" + percent + "%)");
            player.sendMessage("§aRank: " + rank);

            getLogger().info("[Progression] " + player.getName() + ": " + completed + "/" + total + " (" + percent + "%)");

            return true;
        }

        if (command.getName().equalsIgnoreCase("crimson")) {
            if (!sender.hasPermission("worldsettings.crimson")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to trigger the Crimson Descent.");
                return true;
            }
            
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            
            Player player = (Player) sender;
            // Manually trigger the Crimson Descent in the player's world
            // Find or create the CrimsonDescentManager and trigger it
            com.example.worldsettings.listeners.CrimsonDescentManager manager = 
                new com.example.worldsettings.listeners.CrimsonDescentManager(this);
            manager.manualTrigger(player.getWorld());
            player.sendMessage(ChatColor.DARK_RED + "The Crimson Descent has been triggered!");
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

    public ProgressionManager getProgressionManager() {
        return progressionManager;
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
