package com.example.worldsettings;

import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.settings.WorldSettings;
import com.example.worldsettings.mobs.HellZombieSpawner;
import com.example.worldsettings.mobs.HellCreeperSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldSettingsPlugin extends JavaPlugin {

    private static WorldSettingsPlugin instance;
    private WorldSettings worldSettings;

    @Override
    public void onEnable() {
        instance = this;
        worldSettings = new WorldSettings();

        // Register GUI click listener
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);

        // Register commands
        if (getCommand("worldsettings") != null) {
            getCommand("worldsettings").setExecutor(this);
        }

        if (getCommand("spawnhellzombie") != null) {
            getCommand("spawnhellzombie").setExecutor(this);
        }

        if (getCommand("spawnhellcreeper") != null) {
            getCommand("spawnhellcreeper").setExecutor(this);
        }

        getLogger().info("========================================");
        getLogger().info(" WorldSettingsPlugin v1.0.0 enabled!");
        getLogger().info(" Use /worldsettings to open the GUI.");
        getLogger().info(" Use /spawnhellzombie to spawn a Hell Zombie.");
        getLogger().info(" Use /spawnhellcreeper to spawn a Hell Creeper.");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("WorldSettingsPlugin disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // /worldsettings
        if (command.getName().equalsIgnoreCase("worldsettings")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;
            SettingsGUI.openMainMenu(player);
            return true;
        }

        // /spawnhellzombie
        if (command.getName().equalsIgnoreCase("spawnhellzombie")) {

            sender.sendMessage("DEBUG: Hell Zombie command reached"); // debug line

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

        // /spawnhellcreeper
        if (command.getName().equalsIgnoreCase("spawnhellcreeper")) {

            sender.sendMessage("DEBUG: Hell Creeper command reached"); // debug line

            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;

            HellCreeperSpawner spawner = new HellCreeperSpawner(this);
            spawner.spawnHellCreeper(player.getLocation());

            player.sendMessage("§cHell Creeper spawned! Brace yourself!");
            return true;
        }

        return false; // VERY IMPORTANT
    }

    public static WorldSettingsPlugin getInstance() {
        return instance;
    }

    public WorldSettings getWorldSettings() {
        return worldSettings;
    }
}