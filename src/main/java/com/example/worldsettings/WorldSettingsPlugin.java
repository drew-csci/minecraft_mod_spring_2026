package com.example.worldsettings;

import com.example.worldsettings.progression.ProgressionManager;
import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.settings.WorldSettings;
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
        progressionManager = new ProgressionManager(); // ✅ IMPORTANT

        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);

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

        // EXISTING COMMAND
        if (command.getName().equalsIgnoreCase("worldsettings")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            SettingsGUI.openMainMenu(player);
            return true;
        }

        // ✅ NEW COMMAND
        if (command.getName().equalsIgnoreCase("progression")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            int score = progressionManager.calculateProgressionScore(player);
            player.sendMessage("§aYour progression score: §e" + score);
            getLogger().info("[Progression] " + player.getName() + " score: " + score);

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
}