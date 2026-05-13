package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.progression.ProgressionManager;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.Particle;
import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;

import java.util.*;

/**
 * Manages The Crimson Descent event.
 *
 * Behavior and control:
 * - This manager periodically (server tick task) watches for night start and then rolls the nightly
 *   chance to trigger the event. The chance, min start day, and forced max-gap are read from
 *   `WorldSettings` (see getters/setters in `WorldSettings`).
 * - To disable the event entirely, call `WorldSettings.setCrimsonDescentEnabled(false)` or expose
 *   that option in the plugin GUI (`SettingsGUI`). When disabled the manager will skip checks.
 * - Spawn behaviour currently spawns ~70 mobs per player; this amount and the spawn pattern can be
 *   adjusted here. Spawns are performed in a ring pattern at 10..30 blocks from each player.
 * - The red-sky effect is simulated by forcing storm/thunder, showing a boss bar, and spawning red
 *   particles above players for the event duration. The code restores previous weather after the
 *   event ends.
 *
 * Performance note:
 * - Spawning many mobs and particles can be heavy on servers with many players. Consider making the
 *   spawn count global (not per-player) or lowering the count if performance becomes an issue.
 */
public class CrimsonDescentManager {

    private final JavaPlugin plugin;
    private final Random rng = new Random();

    // Per-world state
    private final Map<UUID, Boolean> lastWasNight = new HashMap<>();
    private final Map<UUID, Integer> daysSinceLast = new HashMap<>();
    private final Map<UUID, Integer> currentChance = new HashMap<>();
    // Weather/state backups for restoration after event
    private final Map<UUID, Boolean> prevStorm = new HashMap<>();
    private final Map<UUID, Boolean> prevThundering = new HashMap<>();
    private final Map<UUID, Integer> prevWeatherDuration = new HashMap<>();
    private final Map<UUID, BossBar> activeBossBars = new HashMap<>();
    private final Map<UUID, BukkitRunnable> activeTasks = new HashMap<>();
    private final Set<UUID> activeWorlds = new HashSet<>();

    // Candidate hostile mobs for spawning (Overworld-safe)
    private static final EntityType[] MOB_TYPES = new EntityType[] {
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.SPIDER,
            EntityType.WITCH
    };

    public CrimsonDescentManager(JavaPlugin plugin) {
        this.plugin = plugin;
        // Schedule task every 100 ticks (~5 seconds)
        new BukkitRunnable() {
            @Override
            public void run() {
                tick();
            }
        }.runTaskTimer(plugin, 0L, 100L);
    }

    private void tick() {
        WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
        if (!ws.isCrimsonDescentEnabled()) return;

        for (World world : plugin.getServer().getWorlds()) {
            if (world.getEnvironment() != World.Environment.NORMAL) continue; // only overworld

            UUID wid = world.getUID();
            if (activeWorlds.contains(wid)) {
                lastWasNight.put(wid, isNightTime(world.getTime()));
                continue;
            }
            boolean isNight = isNightTime(world.getTime());
            boolean prev = lastWasNight.getOrDefault(wid, false);

            if (isNight && !prev) {
                // Night just started for this world — evaluate event
                long day = world.getFullTime() / 24000L;
                if (day < ws.getCrimsonMinStartDay()) {
                    // Not yet allowed
                    lastWasNight.put(wid, true);
                    continue;
                }

                int chance = currentChance.getOrDefault(wid, ws.getCrimsonBaseChancePercent());
                int days = daysSinceLast.getOrDefault(wid, 0);

                boolean trigger = false;
                if (days >= ws.getCrimsonMaxGapDays()) {
                    trigger = true; // force after max gap
                } else {
                    int roll = rng.nextInt(100) + 1; // 1..100
                    if (roll <= chance) trigger = true;
                }

                if (trigger) {
                    // Reset counters
                    daysSinceLast.put(wid, 0);
                    currentChance.put(wid, ws.getCrimsonBaseChancePercent());
                    startCrimsonDescent(world);
                } else {
                    daysSinceLast.put(wid, days + 1);
                    currentChance.put(wid, Math.min(100, chance + 5));
                }
            }

            lastWasNight.put(wid, isNight);
        }
    }

    private boolean isNightTime(long time) {
        // Minecraft night roughly from 13000 to 23000
        return time >= 13000L && time <= 23000L;
    }

    /**
     * Helper for tests: determine whether the event should trigger given inputs.
     * Exposed as static so tests can validate the manager's trigger logic without running
     * the full server scheduler.
     *
     * @param chance current chance percent (1..100)
     * @param daysSinceLast number of days since last event
     * @param maxGapDays forced trigger gap
     * @param roll an RNG roll value in range 1..100
     * @return true if the event should trigger
     */
    public static boolean shouldTrigger(int chance, int daysSinceLast, int maxGapDays, int roll) {
        if (daysSinceLast >= maxGapDays) return true;
        return roll >= 1 && roll <= chance;
    }

    public boolean startNow(World world) {
        if (world == null || world.getEnvironment() != World.Environment.NORMAL) {
            return false;
        }

        UUID wid = world.getUID();
        stopEvent(world);
        daysSinceLast.put(wid, 0);
        currentChance.put(
            wid,
            WorldSettingsPlugin.getInstance().getWorldSettings().getCrimsonBaseChancePercent()
        );
        lastWasNight.put(wid, isNightTime(world.getTime()));
        startCrimsonDescent(world);
        return true;
    }

    public boolean stopEvent(World world) {
        if (world == null || world.getEnvironment() != World.Environment.NORMAL) {
            return false;
        }

        UUID wid = world.getUID();
        boolean wasActive = activeWorlds.remove(wid);

        BukkitRunnable task = activeTasks.remove(wid);
        if (task != null) {
            task.cancel();
        }

        BossBar bossBar = activeBossBars.remove(wid);
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }

        Boolean storm = prevStorm.remove(wid);
        Boolean thundering = prevThundering.remove(wid);
        Integer weatherDuration = prevWeatherDuration.remove(wid);
        if (storm != null) {
            world.setStorm(storm);
        }
        if (thundering != null) {
            world.setThundering(thundering);
        }
        if (weatherDuration != null) {
            world.setWeatherDuration(weatherDuration);
        }

        return wasActive || task != null || bossBar != null;
    }

    public void stopAllEvents() {
        for (World world : plugin.getServer().getWorlds()) {
            if (world.getEnvironment() == World.Environment.NORMAL) {
                stopEvent(world);
            }
        }
    }

    private void startCrimsonDescent(World world) {
        stopEvent(world);

        // Choose a single mob type for the entire event
        EntityType chosen = MOB_TYPES[rng.nextInt(MOB_TYPES.length)];

        String title = ChatColor.DARK_RED + "The Crimson Descent";
        String subtitle = ChatColor.RED + "A single tide of " + ChatColor.WHITE + chosen.name().toLowerCase() + ChatColor.RED + " descends...";

        // Backup and change weather to create red night atmosphere
        UUID wid = world.getUID();
        prevStorm.put(wid, world.hasStorm());
        prevThundering.put(wid, world.isThundering());
        prevWeatherDuration.put(wid, world.getWeatherDuration());
        int durationTicks = 20 * 60 * 5; // 5 minutes effect
        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(durationTicks);
        activeWorlds.add(wid);

        // Create a red boss bar visible to players
        final org.bukkit.boss.BossBar boss = plugin.getServer().createBossBar(title, BarColor.RED, BarStyle.SOLID);
        boss.setVisible(true);
        activeBossBars.put(wid, boss);

        // For each player in the world, give feedback and spawn mobs
        for (Player player : world.getPlayers()) {
            player.sendMessage(ChatColor.DARK_RED + "The Crimson Descent has begun! " + ChatColor.WHITE + "Beware...");
            try { player.sendTitle(title, subtitle, 10, 80, 20); } catch (NoSuchMethodError ignored) {}
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
            boss.addPlayer(player);

            int scaledSpawnCount = calculateCrimsonSpawnCount(player, 70);
            spawnMobsAroundPlayer(world, player, chosen, scaledSpawnCount);
        }

        // Spawn red sky particles periodically and remove effects after duration
        final DustOptions dust = new DustOptions(Color.fromRGB(200, 20, 20), 1.5f);
        BukkitRunnable task = new BukkitRunnable() {
            int ticksLeft = durationTicks;

            @Override
            public void run() {
                if (ticksLeft <= 0) {
                    stopEvent(world);
                    cancel();
                    return;
                }

                // spawn particles above each player to simulate red sky
                for (Player p : world.getPlayers()) {
                    Location loc = p.getLocation().clone();
                    loc.setY(Math.min(loc.getY() + 60.0, world.getMaxHeight() - 1));
                    // a burst of red dust
                    world.spawnParticle(Particle.REDSTONE, loc, 40, 5.0, 5.0, 5.0, dust);
                }

                ticksLeft -= 20; // we run every 20 ticks
            }
        };
        task.runTaskTimer(plugin, 0L, 20L);
        activeTasks.put(wid, task);
    }

        static int calculateScaledSpawnCount(int baseCount, int progressionPercent) {
            return ProgressionManager.scaleCountByProgression(baseCount, progressionPercent);
    }

    private int calculateCrimsonSpawnCount(Player player, int baseCount) {
        int progressionPercent = WorldSettingsPlugin.getInstance()
            .getProgressionManager()
            .getProgressionPercent(player);

        return calculateScaledSpawnCount(baseCount, progressionPercent);
    }

    private void spawnMobsAroundPlayer(World world, Player player, EntityType type, int count) {
        Location base = player.getLocation();

        for (int i = 0; i < count; i++) {
            // choose a random angle and distance (10..30 blocks)
            double angle = rng.nextDouble() * Math.PI * 2.0;
            double dist = 10.0 + rng.nextDouble() * 20.0;
            double dx = Math.cos(angle) * dist;
            double dz = Math.sin(angle) * dist;

            int tx = base.getBlockX() + (int) Math.round(dx);
            int tz = base.getBlockZ() + (int) Math.round(dz);
            int ty = world.getHighestBlockYAt(tx, tz);

            Location spawnLoc = new Location(world, tx + 0.5, ty + 1.0, tz + 0.5);
            try {
                org.bukkit.entity.Entity ent = world.spawnEntity(spawnLoc, type);
                // Respect existing toggle: only apply mob enhancements if enabled in settings
                WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
                if (ws.isEnhancedMobs() && ent instanceof LivingEntity) {
                    LivingEntity mob = (LivingEntity) ent;
                    double boost = ws.getPostEndDifficultyBoost(); // reuse boost setting
                    try {
                        AttributeInstance hp = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        if (hp != null) {
                            double hpBase = Math.max(1.0, hp.getBaseValue());
                            double newBase = Math.max(1.0, hpBase * boost);
                            hp.setBaseValue(newBase);
                            mob.setHealth(Math.min(newBase, mob.getHealth()));
                        }
                    } catch (Throwable ignored) {
                        // ignore attribute failures
                    }

                    int amplifier = Math.max(0, (int) Math.round(boost) - 1);
                    int duration = 20 * 60 * 5; // 5 minutes
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, amplifier, true, false));
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier, true, false));
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, Math.max(0, amplifier-1), true, false));
                }
            } catch (Throwable t) {
                // ignore spawn failures for certain entity types or cramped spaces
            }
        }
    }
}
