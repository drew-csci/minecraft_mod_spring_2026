package com.example.worldsettings;

import com.example.worldsettings.boss.SuperEnchantmentTableListener;
import com.example.worldsettings.boss.SuperEnchantmentTableManager;
import com.example.worldsettings.progression.ProgressionManager;
import com.example.worldsettings.gui.SettingsGUI;
import com.example.worldsettings.listeners.CraftingListener;
import com.example.worldsettings.listeners.DragonEggDestructionListener;
import com.example.worldsettings.listeners.EntityDeathListener;
import com.example.worldsettings.listeners.FurnaceListener;
import com.example.worldsettings.listeners.GUIClickListener;
import com.example.worldsettings.listeners.HellCreeperListener;
import com.example.worldsettings.listeners.HellEndermanListener;
import com.example.worldsettings.listeners.HellSkeletonListener;
import com.example.worldsettings.listeners.HellSpiderListener;
import com.example.worldsettings.listeners.HellZombieListener;
import com.example.worldsettings.listeners.PlayerInteractListener;
import com.example.worldsettings.listeners.PlayerMovementListener;
import com.example.worldsettings.listeners.PostEndListener;
import com.example.worldsettings.listeners.ProjectileListener;
import com.example.worldsettings.listeners.WorldSettingsGameplayListener;
import com.example.worldsettings.listeners.CrimsonDescentManager;
import com.example.worldsettings.settings.WorldSettings;
import com.example.worldsettings.sidebar.PlayerJoinListener;
import com.example.worldsettings.sidebar.PlayerAdvancementListener;
import com.example.worldsettings.sidebar.ProgressionSidebar;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldSettingsPlugin extends JavaPlugin {

    private static WorldSettingsPlugin instance;
    private WorldSettings worldSettings;
    private SuperEnchantmentTableManager superEnchantmentTableManager;
    private ProgressionManager progressionManager;
    private CrimsonDescentManager crimsonDescentManager;
    private PostEndListener postEndListener;

    @Override
    public void onEnable() {
        instance = this;
        worldSettings = new WorldSettings();
        superEnchantmentTableManager = new SuperEnchantmentTableManager(this);
        progressionManager = new ProgressionManager();

        saveDefaultConfig();
        reloadSettingsFromConfig();

        // Register listeners
        getServer().getPluginManager().registerEvents(new GUIClickListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSettingsGameplayListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerAdvancementListener(), this);
        getServer().getPluginManager().registerEvents(new HellCreeperListener(), this);
        getServer().getPluginManager().registerEvents(new HellZombieListener(), this);
        getServer().getPluginManager().registerEvents(new HellSpiderListener(), this);
        getServer().getPluginManager().registerEvents(new HellEndermanListener(), this);
        getServer().getPluginManager().registerEvents(new HellSkeletonListener(), this);

        // Register the post-end listener (dragon death activation)
        postEndListener = new PostEndListener(this);
        getServer().getPluginManager().registerEvents(postEndListener, this);
        // Start Crimson Descent manager (nightly randomized event)
        crimsonDescentManager = new CrimsonDescentManager(this);

        // Register the entity death listener for custom drops
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);

        // Register the crafting listener for custom recipes
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);

        // Register the player movement listener for Spider Boots
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);

        // Register the player interact listener for Blood Orb
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        // Register the projectile listener for Exploding Arrows
        getServer().getPluginManager().registerEvents(new ProjectileListener(), this);

        // Register the furnace listener for Purifying Furnace
        getServer().getPluginManager().registerEvents(new FurnaceListener(), this);

        // Register the dragon egg destruction listener (for Void Devourer boss spawn)
        getServer().getPluginManager().registerEvents(new DragonEggDestructionListener(), this);
        
        // Register Super Enchantment Table listener
        getServer().getPluginManager().registerEvents(
            new SuperEnchantmentTableListener(superEnchantmentTableManager), this);

        // Register custom recipes
        registerRecipes();

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player onlinePlayer : getServer().getOnlinePlayers()) {
                ProgressionSidebar.updateSidebar(onlinePlayer, progressionManager);
            }
        }, 0L, 100L);

        getLogger().info("========================================");
        getLogger().info(" WorldSettingsPlugin v1.0.0 enabled!");
        getLogger().info(" Use /worldsettings to open the GUI.");
        getLogger().info(" Use /progression for advancement tracking.");
        getLogger().info(" Use /supertableinfo for enchant info.");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        if (crimsonDescentManager != null) {
            crimsonDescentManager.stopAllEvents();
        }
        if (postEndListener != null) {
            postEndListener.deactivateAll();
        }
        getLogger().info("WorldSettingsPlugin disabled.");
    }

    private World getPrimaryOverworld() {
        for (World world : getServer().getWorlds()) {
            if (world.getEnvironment() == World.Environment.NORMAL) {
                return world;
            }
        }
        return null;
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

            if (args.length > 0 && args[0].equalsIgnoreCase("stopall")) {
                if (!sender.hasPermission("worldsettings.reload")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to stop active world phases.");
                    return true;
                }

                World overworld = getPrimaryOverworld();
                if (overworld != null) {
                    worldSettings.setCrimsonDescentEnabled(false);
                    crimsonDescentManager.stopEvent(overworld);
                    postEndListener.deactivatePostEnd(overworld);
                    saveSettingsToConfig();
                }
                sender.sendMessage(ChatColor.GRAY + "Crimson Descent and Post-End phase stopped.");
                return true;
            }

            if (args.length > 1
                && args[0].equalsIgnoreCase("postend")
                && args[1].equalsIgnoreCase("start")) {
                if (!sender.hasPermission("worldsettings.reload")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to start the post-end phase.");
                    return true;
                }

                World targetWorld = sender instanceof Player player ? player.getWorld() : getPrimaryOverworld();
                if (targetWorld == null || !postEndListener.activatePostEnd(targetWorld, sender instanceof Player player ? player : null)) {
                    sender.sendMessage(ChatColor.RED + "No valid overworld found for the post-end phase.");
                    return true;
                }
                saveSettingsToConfig();
                sender.sendMessage(ChatColor.GOLD + "Post-End phase activated.");
                return true;
            }

            if (args.length > 1
                && args[0].equalsIgnoreCase("postend")
                && args[1].equalsIgnoreCase("stop")) {
                if (!sender.hasPermission("worldsettings.reload")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to stop the post-end phase.");
                    return true;
                }

                World targetWorld = sender instanceof Player player ? player.getWorld() : getPrimaryOverworld();
                if (targetWorld == null || !postEndListener.deactivatePostEnd(targetWorld)) {
                    sender.sendMessage(ChatColor.RED + "No valid overworld found for the post-end phase.");
                    return true;
                }
                saveSettingsToConfig();
                sender.sendMessage(ChatColor.GRAY + "Post-End phase stopped.");
                return true;
            }

            if (args.length > 1
                && args[0].equalsIgnoreCase("crimsondescent")
                && args[1].equalsIgnoreCase("start")) {
                if (!sender.hasPermission("worldsettings.reload")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to start Crimson Descent.");
                    return true;
                }

                World targetWorld = null;
                if (sender instanceof Player player) {
                    targetWorld = player.getWorld();
                } else {
                    for (World world : getServer().getWorlds()) {
                        if (world.getEnvironment() == World.Environment.NORMAL) {
                            targetWorld = world;
                            break;
                        }
                    }
                }

                if (targetWorld == null || !crimsonDescentManager.startNow(targetWorld)) {
                    sender.sendMessage(ChatColor.RED + "No valid overworld found for Crimson Descent.");
                    return true;
                }

                sender.sendMessage(ChatColor.DARK_RED + "Crimson Descent started in " + targetWorld.getName() + ".");
                return true;
            }

            if (args.length > 1
                && args[0].equalsIgnoreCase("crimsondescent")
                && args[1].equalsIgnoreCase("stop")) {
                if (!sender.hasPermission("worldsettings.reload")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to stop Crimson Descent.");
                    return true;
                }

                World targetWorld = sender instanceof Player player ? player.getWorld() : getPrimaryOverworld();
                if (targetWorld == null) {
                    sender.sendMessage(ChatColor.RED + "No valid overworld found for Crimson Descent.");
                    return true;
                }

                worldSettings.setCrimsonDescentEnabled(false);
                crimsonDescentManager.stopEvent(targetWorld);
                saveSettingsToConfig();
                sender.sendMessage(ChatColor.GRAY + "Crimson Descent stopped in " + targetWorld.getName() + ".");
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

        // Copper Staff recipe
        NamespacedKey copperStaffKey = new NamespacedKey(this, "copper_staff");
        ShapedRecipe copperStaffRecipe = new ShapedRecipe(copperStaffKey, ModdedItems.createCopperStaff());
        copperStaffRecipe.shape(" E ", " S ", " R ");
        copperStaffRecipe.setIngredient('E', new RecipeChoice.ExactChoice(ModdedItems.createExplosiveCharge()));
        copperStaffRecipe.setIngredient('S', Material.LIGHTNING_ROD);
        copperStaffRecipe.setIngredient('R', Material.LIGHTNING_ROD);
        getServer().addRecipe(copperStaffRecipe);

        // Purifying Furnace recipe: Furnace + Purifying Powder
        NamespacedKey purifyingFurnaceKey = new NamespacedKey(this, "purifying_furnace");
        ShapedRecipe purifyingFurnaceRecipe = new ShapedRecipe(purifyingFurnaceKey, ModdedItems.createPurifyingFurnace());
        purifyingFurnaceRecipe.shape("P", "F");
        purifyingFurnaceRecipe.setIngredient('P', new RecipeChoice.ExactChoice(ModdedItems.createPurifyingPowder()));
        purifyingFurnaceRecipe.setIngredient('F', Material.FURNACE);
        getServer().addRecipe(purifyingFurnaceRecipe);

        // Upgrade Template duplication recipe
        ItemStack twoTemplates = ModdedItems.createUpgradeTemplate();
        twoTemplates.setAmount(2);
        NamespacedKey templateDuplicateKey = new NamespacedKey(this, "upgrade_template_duplicate");
        ShapedRecipe templateDuplicateRecipe = new ShapedRecipe(templateDuplicateKey, twoTemplates);
        templateDuplicateRecipe.shape("DDD", "DTD", "DDD");
        templateDuplicateRecipe.setIngredient('D', Material.DIAMOND);
        templateDuplicateRecipe.setIngredient('T', new RecipeChoice.ExactChoice(ModdedItems.createUpgradeTemplate()));
        getServer().addRecipe(templateDuplicateRecipe);
    }
}
