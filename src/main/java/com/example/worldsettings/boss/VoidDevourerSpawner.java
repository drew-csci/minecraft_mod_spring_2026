package com.example.worldsettings.boss;

import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * Handles spawning and configuring the Void Devourer boss —
 * a custom Ender Dragon variant that's stronger, faster, and harder than normal.
 *
 * Features:
 * - Red-themed summon effects (particles, sounds, title message)
 * - Stat scaling based on game difficulty
 * - Enhanced health, speed, and damage
 * - BossBar for visual difficulty indication
 */
public class VoidDevourerSpawner {

    /**
     * Spawns the Void Devourer boss near world spawn with summon effects.
     *
     * @param world      The world to spawn in
     * @param triggerer  The player who triggered the spawn
     * @param settings   Plugin settings for difficulty scaling
     */
    public static void spawnVoidDevourer(World world, Player triggerer, WorldSettings settings) {
        // Get world spawn location
        Location spawnLoc = world.getSpawnLocation();
        
        // Generate a spawn location near world spawn (within ~50 blocks radius)
        Location bossSpawnLoc = generateRandomSpawnNearby(spawnLoc, 50);
        bossSpawnLoc.setY(Math.max(70, bossSpawnLoc.getY() + 10)); // Ensure height for dragon flight
        
        // Get difficulty multiplier
        double difficultyMultiplier = getDifficultyMultiplier(settings.getDifficultyLevel());

        // ── SUMMON EFFECTS (before spawn) ──────────────────────────────
        performSummonEffects(world, bossSpawnLoc, triggerer);

        // ── SPAWN THE DRAGON ───────────────────────────────────────────
        EnderDragon dragon = (EnderDragon) world.spawnEntity(bossSpawnLoc, EntityType.ENDER_DRAGON);

        // ── CONFIGURE STATS ────────────────────────────────────────────
        configureVoidDevourerStats(dragon, difficultyMultiplier);

        // ── CREATE BOSS BAR ────────────────────────────────────────────
        BossBar bossBar = Bukkit.createBossBar(
            ChatColor.DARK_RED + "" + ChatColor.BOLD + "Void Devourer",
            BarColor.RED,
            BarStyle.SOLID
        );
        bossBar.addPlayer(triggerer);
        bossBar.setProgress(1.0);

        // Link BossBar to dragon (update as it takes damage)
        linkBossBarToDragon(dragon, bossBar);

        // ── ANNOUNCEMENT ───────────────────────────────────────────────
        String diffName = settings.getDifficultyLevel().display();
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "☬ " + ChatColor.RED + ChatColor.BOLD 
            + "THE VOID DEVOURER HAS EMERGED" + ChatColor.DARK_RED + " (Difficulty: " + diffName + ") ☬");
    }

    /**
     * Performs red-themed summon effects: particles, sounds, and title message.
     */
    private static void performSummonEffects(World world, Location loc, Player triggerer) {
        // Dense red dust cloud in two tones to make the summon look distinctly crimson.
        for (int i = 0; i < 120; i++) {
            double offsetX = (Math.random() - 0.5) * 10;
            double offsetY = (Math.random() - 0.5) * 6;
            double offsetZ = (Math.random() - 0.5) * 10;

            Location particleLoc = loc.clone().add(offsetX, offsetY, offsetZ);
            Particle.DustOptions dust = (i % 2 == 0)
                ? new Particle.DustOptions(org.bukkit.Color.fromRGB(255, 25, 25), 2.5f)
                : new Particle.DustOptions(org.bukkit.Color.fromRGB(120, 0, 0), 2.0f);
            world.spawnParticle(Particle.REDSTONE, particleLoc, 1, 0.0, 0.0, 0.0, 0.0, dust);
        }

        // Flame ring around summon center.
        for (int i = 0; i < 40; i++) {
            double angle = (Math.PI * 2 * i) / 40;
            double offsetX = Math.cos(angle) * 6;
            double offsetZ = Math.sin(angle) * 6;

            Location particleLoc = loc.clone().add(offsetX, 1.5, offsetZ);
            world.spawnParticle(Particle.FLAME, particleLoc, 6, 0.15, 0.15, 0.15, 0.01);
        }

        // Dark smoke plume for void corruption effect.
        world.spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(0, 1.5, 0), 120, 2.8, 1.5, 2.8, 0.02);

        // Dragon breath haze to hint this is a corrupted dragon variant.
        world.spawnParticle(Particle.DRAGON_BREATH, loc.clone().add(0, 1.0, 0), 220, 3.2, 1.8, 3.2, 0.04);

        // Dramatic sound effect chain
        world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 2.0f, 0.5f); // Deep roar
        world.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.5f, 0.8f);        // Ominous spawn
        world.playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.2f, 0.7f); // Electric charge

        // Title message to triggerer
        triggerer.sendTitle(
            ChatColor.DARK_RED + "" +ChatColor.BOLD + "☬ VOID DEVOURER ☬",
            ChatColor.RED + "Prepare for battle...",
            10, 60, 10
        );
    }

    /**
     * Configures Void Devourer stats: health, speed, damage based on multiplier.
     */
    private static void configureVoidDevourerStats(EnderDragon dragon, double difficultyMultiplier) {
        // Base Ender Dragon health is 200
        double baseHealth = 200.0;
        double boostedHealth = baseHealth * (1.0 + 0.5 * difficultyMultiplier); // +50% per difficulty level
        
        // Set max health and current health
        var maxHealthAttr = dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.setBaseValue(boostedHealth);
            dragon.setHealth(boostedHealth);
        }

        // Base dragon speed multiplier is ~0.1
        // Increase by 30% per difficulty level
        var speedAttr = dragon.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speedAttr != null) {
            double baseSpeed = 0.1;
            double boostedSpeed = baseSpeed * (1.0 + 0.3 * difficultyMultiplier);
            speedAttr.setBaseValue(boostedSpeed);
        }

        // Damage: add attack damage if available (some versions may not have it for dragons)
        // Spike damage via critical hits and aggression instead
        var dmgAttr = dragon.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (dmgAttr != null) {
            double baseDamage = 6.0;
            double boostedDamage = baseDamage * (1.0 + 0.4 * difficultyMultiplier);
            dmgAttr.setBaseValue(boostedDamage);
        }

        // Try to scale dragon size when the server/API exposes GENERIC_SCALE.
        // Uses reflection-safe enum lookup so this class still compiles on older APIs.
        double sizeScale = 1.25 + (0.15 * difficultyMultiplier); // Easy~1.325, Medium~1.40, Hard~1.475
        tryApplyScale(dragon, sizeScale);

        // Make the dragon aggressive and persistent
        dragon.setCustomName(ChatColor.DARK_RED + "Void Devourer");
        dragon.setCustomNameVisible(true);
        dragon.setRemoveWhenFarAway(false); // Dragon won't despawn
    }

    /**
     * Applies entity scale if the running API supports GENERIC_SCALE.
     */
    private static void tryApplyScale(EnderDragon dragon, double scaleValue) {
        try {
            Attribute scaleAttribute = Attribute.valueOf("GENERIC_SCALE");
            var scaleInstance = dragon.getAttribute(scaleAttribute);
            if (scaleInstance != null) {
                scaleInstance.setBaseValue(scaleValue);
            }
        } catch (IllegalArgumentException ignored) {
            // GENERIC_SCALE is not available on this API version; skip sizing.
        }
    }

    /**
     * Returns a difficulty multiplier based on the set difficulty.
     * EASY = 0.5x, MEDIUM = 1.0x, HARD = 1.5x
     */
    private static double getDifficultyMultiplier(WorldSettings.DifficultyLevel difficulty) {
        switch (difficulty) {
            case EASY:
                return 0.5;
            case MEDIUM:
                return 1.0;
            case HARD:
                return 1.5;
            default:
                return 1.0;
        }
    }

    /**
     * Generates a random spawn location near a center point.
     */
    private static Location generateRandomSpawnNearby(Location center, int radius) {
        double angle = Math.random() * Math.PI * 2;
        double distance = Math.random() * radius;
        double offsetX = Math.cos(angle) * distance;
        double offsetZ = Math.sin(angle) * distance;

        return center.clone().add(offsetX, 0, offsetZ);
    }

    /**
     * Links a BossBar to a dragon: updates the bar's progress as the dragon takes damage.
     * The bar is removed once the dragon dies.
     */
    private static void linkBossBarToDragon(EnderDragon dragon, BossBar bossBar) {
        // Task to update bar every tick
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            Bukkit.getPluginManager().getPlugin("WorldSettingsPlugin"),
            () -> {
                if (!dragon.isValid() || dragon.isDead()) {
                    bossBar.removeAll();
                    return;
                }

                double healthPercent = dragon.getHealth() / dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                bossBar.setProgress(Math.max(0, Math.min(1, healthPercent)));
            },
            0L,
            1L
        );
    }
}
