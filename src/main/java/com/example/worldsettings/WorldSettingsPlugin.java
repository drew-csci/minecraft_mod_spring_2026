package com.example.worldsettings;

import com.example.worldsettings.progression.ProgressionManager;
import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.settings.WorldSettings;
import com.example.worldsettings.sidebar.PlayerJoinListener;
import com.example.worldsettings.sidebar.PlayerAdvancementListener;
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

        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerAdvancementListener(), this);

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
}