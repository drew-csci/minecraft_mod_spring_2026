package com.example.worldsettings;

import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.mobs.HellZombieSpawner;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * WorldSettingsPlugin - Main entry point.
 * Provides a GUI-based world settings menu accessible via /worldsettings.
 * Also allows manual spawning of Hell Zombies via /spawnhellzombie.
 */
public class WorldSettingsPlugin extends JavaPlugin {

    private static WorldSettingsPlugin instance;
    private WorldSettings worldSettings;

    @Override
    public void onEnable() {
        instance = this;
        worldSettings = new WorldSettings();

        // Register GUI click listener
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        
        // Register commands so onCommand is called
        if (getCommand("worldsettings") != null) getCommand("worldsettings").setExecutor(this);
        if (getCommand("spawnhellzombie") != null) getCommand("spawnhellzombie").setExecutor(this);

        getLogger().info("========================================");
        getLogger().info(" WorldSettingsPlugin v1.0.0 enabled!");
        getLogger().info(" Use /worldsettings to open the GUI.");
        getLogger().info(" Use /spawnhellzombie to spawn a Hell Zombie at your location.");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("WorldSettingsPlugin disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Open the GUI
        if (command.getName().equalsIgnoreCase("worldsettings")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            SettingsGUI.openMainMenu(player);
            return true;
        }

        // Spawn a Hell Zombie
        if (command.getName().equalsIgnoreCase("spawnhellzombie")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;

            HellZombieSpawner spawner = new HellZombieSpawner(this);
            spawner.spawnHellZombie(player.getLocation());

            player.sendMessage("§cHell Zombie spawned!");
            return true;
        }

        return false; // Important: return false for unhandled commands
    }

    public static WorldSettingsPlugin getInstance() {
        return instance;
    }

    public WorldSettings getWorldSettings() {
        return worldSettings;
    }
}