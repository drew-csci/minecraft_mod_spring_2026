package com.example.worldsettings;

import com.example.worldsettings.boss.SuperEnchantmentTableListener;
import com.example.worldsettings.boss.SuperEnchantmentTableManager;
import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * WorldSettingsPlugin - Main entry point.
 * Provides a GUI-based world settings menu accessible via /worldsettings.
 */
public class WorldSettingsPlugin extends JavaPlugin {

    private static WorldSettingsPlugin instance;
    private WorldSettings worldSettings;
    private SuperEnchantmentTableManager superEnchantmentTableManager;

    @Override
    public void onEnable() {
        instance = this;
        worldSettings = new WorldSettings();
        superEnchantmentTableManager = new SuperEnchantmentTableManager(this);

        // Register the GUI click listener
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        
        // Register Super Enchantment Table listener
        getServer().getPluginManager().registerEvents(
            new SuperEnchantmentTableListener(superEnchantmentTableManager), this);
        
        // Register Super Table recipe
        superEnchantmentTableManager.registerSuperTableRecipe();

        getLogger().info("========================================");
        getLogger().info(" WorldSettingsPlugin v1.0.0 enabled!");
        getLogger().info(" Use /worldsettings to open the GUI.");
        getLogger().info(" Use /supertableinfo for enchant info.");
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
        
        if (command.getName().equalsIgnoreCase("supertableinfo")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            player.sendMessage("§6§l=== Super Enchantment Table ===");
            player.sendMessage("§7Enhanced enchantments with exclusive higher tiers!");
            player.sendMessage("§cRequirements:");
            player.sendMessage("§c• Defeat the End Dragon (progression unlock)");
            player.sendMessage("§c• Void Essence (rare drops from Endermen/Endermites)");
            player.sendMessage("§c• Dragon Core (from End Dragon defeat)");
            player.sendMessage("§eExclusive Enchants:");
            player.sendMessage("§e• Sharpness VII (vs vanilla max V)");
            player.sendMessage("§e• Protection VI (vs vanilla max IV)");
            player.sendMessage("§eCost: 1.5x lapis, 2x XP");
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
