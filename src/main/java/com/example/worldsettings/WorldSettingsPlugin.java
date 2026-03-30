package com.example.worldsettings;

import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.listeners.SuperEnchantListener;
import com.example.worldsettings.settings.WorldSettings;
import com.example.worldsettings.superenchant.SuperEnchantmentManager;
import com.example.worldsettings.superenchant.SuperEnchantmentTableGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * WorldSettingsPlugin - Main entry point.
 * Provides a GUI-based world settings menu accessible via /worldsettings.
 */
public class WorldSettingsPlugin extends JavaPlugin {

    private static WorldSettingsPlugin instance;
    private WorldSettings worldSettings;

    @Override
    public void onEnable() {
        instance = this;
        worldSettings = new WorldSettings();
        SuperEnchantmentManager.initialize(this);

        // Register the GUI click listeners
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        getServer().getPluginManager().registerEvents(new SuperEnchantListener(), this);
        registerSuperEnchantmentRecipes();

        getLogger().info("========================================");
        getLogger().info(" WorldSettingsPlugin v1.0.0 enabled!");
        getLogger().info(" Use /worldsettings to open the GUI.");
        getLogger().info(" Super Enchantment Table feature enabled.");
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

        if (command.getName().equalsIgnoreCase("superenchant")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            if (!SuperEnchantmentManager.isSuperEnchantmentTable(player.getInventory().getItemInMainHand())) {
                player.sendMessage(ChatColor.RED + "Hold a Super Enchantment Table in your hand to use it.");
                return true;
            }
            SuperEnchantmentTableGUI.open(player);
            return true;
        }

        if (command.getName().equalsIgnoreCase("supertable")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            if (!sender.hasPermission("worldsettings.superenchant.give")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to receive the Super Enchantment Table.");
                return true;
            }
            Player player = (Player) sender;
            player.getInventory().addItem(SuperEnchantmentManager.createSuperEnchantmentTable());
            player.sendMessage(ChatColor.GREEN + "A Super Enchantment Table has been added to your inventory.");
            return true;
        }

        return false;
    }

    private void registerSuperEnchantmentRecipes() {
        ShapedRecipe coreRecipe = new ShapedRecipe(new NamespacedKey(this, "reinforced_enchantment_core"), SuperEnchantmentManager.createReinforcedEnchantmentCore());
        coreRecipe.shape("VDV", "D B", "VDV");
        coreRecipe.setIngredient('V', new RecipeChoice.ExactChoice(SuperEnchantmentManager.createVoidCrystal()));
        coreRecipe.setIngredient('D', new RecipeChoice.ExactChoice(SuperEnchantmentManager.createAncientArcaneDust()));
        coreRecipe.setIngredient('B', Material.BOOK);
        getServer().addRecipe(coreRecipe);

        ShapedRecipe tableRecipe = new ShapedRecipe(new NamespacedKey(this, "super_enchantment_table"), SuperEnchantmentManager.createSuperEnchantmentTable());
        tableRecipe.shape("VDV", "DTE", "VDV");
        tableRecipe.setIngredient('V', new RecipeChoice.ExactChoice(SuperEnchantmentManager.createVoidCrystal()));
        tableRecipe.setIngredient('D', new RecipeChoice.ExactChoice(SuperEnchantmentManager.createAncientArcaneDust()));
        tableRecipe.setIngredient('T', Material.ENCHANTING_TABLE);
        tableRecipe.setIngredient('E', new RecipeChoice.ExactChoice(SuperEnchantmentManager.createReinforcedEnchantmentCore()));
        getServer().addRecipe(tableRecipe);
    }

    public static WorldSettingsPlugin getInstance() {
        return instance;
    }

    public WorldSettings getWorldSettings() {
        return worldSettings;
    }
}
