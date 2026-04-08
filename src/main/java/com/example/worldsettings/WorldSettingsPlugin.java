package com.example.worldsettings;

import org.bukkit.Material;

import com.example.worldsettings.ModdedItems;
import com.example.worldsettings.gui.SettingsGUI;

import com.example.worldsettings.listeners.CraftingListener;
import com.example.worldsettings.listeners.EntityDeathListener;
import com.example.worldsettings.listeners.FurnaceListener;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.listeners.HellCreeperListener;
import com.example.worldsettings.listeners.PostEndListener;
import com.example.worldsettings.listeners.PlayerInteractListener;
import com.example.worldsettings.listeners.PlayerMovementListener;
import com.example.worldsettings.listeners.ProjectileListener;
import com.example.worldsettings.settings.WorldSettings;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
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

    @Override
    public void onEnable() {
        instance = this;
        worldSettings = new WorldSettings();

        // Register the GUI click listener
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        // Register the post-end listener (dragon death activation + mob enhancements)
        getServer().getPluginManager().registerEvents(new PostEndListener(), this);
        // Start Crimson Descent manager which handles the nightly randomized event
        new com.example.worldsettings.listeners.CrimsonDescentManager(this);

        // Register the entity death listener for custom drops
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);

        // Register the crafting listener for custom recipes
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);
        getServer().getPluginManager().registerEvents(new HellCreeperListener(), this);

        // Register the player movement listener for Spider Boots
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);

        // Register the player interact listener for Blood Orb
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        // Register the projectile listener for Exploding Arrows
        getServer().getPluginManager().registerEvents(new ProjectileListener(), this);

        // Register the furnace listener for Purifying Furnace
        getServer().getPluginManager().registerEvents(new FurnaceListener(), this);

        // Register custom recipes
        registerRecipes();

        getLogger().info("========================================");
        getLogger().info(" WorldSettingsPlugin v1.0.0 enabled!");
        getLogger().info(" Use /worldsettings to open the GUI.");
        getLogger().info("========================================");
    }

    private void registerRecipes() {
        // Reinforced Bow recipe: Shaped recipe with bow in center, bones around
        NamespacedKey reinforcedBowKey = new NamespacedKey(this, "reinforced_bow");
        ShapedRecipe reinforcedBowRecipe = new ShapedRecipe(reinforcedBowKey, ModdedItems.createReinforcedBow());
        reinforcedBowRecipe.shape(" B ", "BAB", " B ");
        reinforcedBowRecipe.setIngredient('B', new RecipeChoice.ExactChoice(ModdedItems.createReinforcedBones()));
        reinforcedBowRecipe.setIngredient('A', Material.BOW);
        getServer().addRecipe(reinforcedBowRecipe);

        // Spider Boots recipe: Diamond boots in center, spider legs around
        NamespacedKey spiderBootsKey = new NamespacedKey(this, "spider_boots");
        ShapedRecipe spiderBootsRecipe = new ShapedRecipe(spiderBootsKey, ModdedItems.createSpiderBoots());
        spiderBootsRecipe.shape("   ", "SDS", "S S");
        spiderBootsRecipe.setIngredient('S', new RecipeChoice.ExactChoice(ModdedItems.createSpiderLegs()));
        spiderBootsRecipe.setIngredient('D', Material.DIAMOND_BOOTS);
        getServer().addRecipe(spiderBootsRecipe);

        // Blood Orb recipe: 9 Blood Shards in 3x3 grid
        NamespacedKey bloodOrbKey = new NamespacedKey(this, "blood_orb");
        ShapedRecipe bloodOrbRecipe = new ShapedRecipe(bloodOrbKey, ModdedItems.createBloodOrb());
        bloodOrbRecipe.shape("BBB", "BBB", "BBB");
        bloodOrbRecipe.setIngredient('B', new RecipeChoice.ExactChoice(ModdedItems.createBloodShard()));
        getServer().addRecipe(bloodOrbRecipe);

        // Super Enchantment Table recipe: Enchanting table in center, diamonds in corners, pearls elsewhere
        NamespacedKey superEnchantKey = new NamespacedKey(this, "super_enchantment_table");
        ShapedRecipe superEnchantRecipe = new ShapedRecipe(superEnchantKey, ModdedItems.createSuperEnchantmentTable());
        superEnchantRecipe.shape("DPD", "PEP", "DPD");
        superEnchantRecipe.setIngredient('D', Material.DIAMOND);
        superEnchantRecipe.setIngredient('P', new RecipeChoice.ExactChoice(ModdedItems.createEnchantedPearl()));
        superEnchantRecipe.setIngredient('E', Material.ENCHANTING_TABLE);
        getServer().addRecipe(superEnchantRecipe);

        // Exploding Arrow recipe: Gunpowder on top of arrow
        NamespacedKey explodingArrowKey = new NamespacedKey(this, "exploding_arrow");
        ShapedRecipe explodingArrowRecipe = new ShapedRecipe(explodingArrowKey, ModdedItems.createExplodingArrow());
        explodingArrowRecipe.shape(" G ", " A ", "   ");
        explodingArrowRecipe.setIngredient('G', new RecipeChoice.ExactChoice(ModdedItems.createThermalGunpowder()));
        explodingArrowRecipe.setIngredient('A', Material.ARROW);
        getServer().addRecipe(explodingArrowRecipe);

        // Copper Staff recipe: top mid Explosive Charge, mid mid Lightning Rod, bottom mid Lightning Rod
        NamespacedKey copperStaffKey = new NamespacedKey(this, "copper_staff");
        ShapedRecipe copperStaffRecipe = new ShapedRecipe(copperStaffKey, ModdedItems.createCopperStaff());
        copperStaffRecipe.shape(" E ", " S ", " R ");
        copperStaffRecipe.setIngredient('E', new RecipeChoice.ExactChoice(ModdedItems.createExplosiveCharge()));
        copperStaffRecipe.setIngredient('S', Material.LIGHTNING_ROD);
        copperStaffRecipe.setIngredient('R', Material.LIGHTNING_ROD);
        getServer().addRecipe(copperStaffRecipe);

        // Purifying Furnace recipe: Furnace + Purifying Powder (1x2 vertical)
        NamespacedKey purifyingFurnaceKey = new NamespacedKey(this, "purifying_furnace");
        ShapedRecipe purifyingFurnaceRecipe = new ShapedRecipe(purifyingFurnaceKey, ModdedItems.createPurifyingFurnace());
        purifyingFurnaceRecipe.shape("P", "F");
        purifyingFurnaceRecipe.setIngredient('P', new RecipeChoice.ExactChoice(ModdedItems.createPurifyingPowder()));
        purifyingFurnaceRecipe.setIngredient('F', Material.FURNACE);
        getServer().addRecipe(purifyingFurnaceRecipe);

        // Upgrade Template duplication recipe (1 template + 1 Blood Orb + 7 diamonds -> 2 templates)
        ItemStack twoTemplates = ModdedItems.createUpgradeTemplate();
        twoTemplates.setAmount(2);
        NamespacedKey templateDuplicateKey = new NamespacedKey(this, "upgrade_template_duplicate");
        ShapedRecipe templateDuplicateRecipe = new ShapedRecipe(templateDuplicateKey, twoTemplates);
        templateDuplicateRecipe.shape("DDD", "TBT", "DDD");
        templateDuplicateRecipe.setIngredient('D', Material.DIAMOND);
        templateDuplicateRecipe.setIngredient('T', new RecipeChoice.ExactChoice(ModdedItems.createUpgradeTemplate()));
        templateDuplicateRecipe.setIngredient('B', new RecipeChoice.ExactChoice(ModdedItems.createBloodOrb()));
        getServer().addRecipe(templateDuplicateRecipe);
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
        return false;
    }

    public static WorldSettingsPlugin getInstance() {
        return instance;
    }

    public WorldSettings getWorldSettings() {
        return worldSettings;
    }
}
