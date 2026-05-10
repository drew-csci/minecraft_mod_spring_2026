package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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

    // Candidate hostile mobs for spawning (Overworld-safe and hellified)
    private static final EntityType[] MOB_TYPES = new EntityType[] {
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.SPIDER,
            EntityType.ENDERMAN,
            EntityType.PHANTOM
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

    public void manualTrigger(World world) {
        if (world.getEnvironment() != World.Environment.NORMAL) return;
        startCrimsonDescent(world);
    }

    private void tick() {
        WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
        if (!ws.isCrimsonDescentEnabled()) return;

        for (World world : plugin.getServer().getWorlds()) {
            if (world.getEnvironment() != World.Environment.NORMAL) continue; // only overworld

            UUID wid = world.getUID();
            boolean isNight = isNightTime(world.getTime());
            boolean prev = lastWasNight.getOrDefault(wid, false);

            if (isNight && !prev) {
                // Night just started for this world — spawn nightly hell phantoms first and evaluate event
                spawnNightlyPhantoms(world);
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

    private void startCrimsonDescent(World world) {
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

        // Create a red boss bar visible to players
        final org.bukkit.boss.BossBar boss = plugin.getServer().createBossBar(title, BarColor.RED, BarStyle.SOLID);
        boss.setVisible(true);

        // For each player in the world, give feedback and spawn mobs
        for (Player player : world.getPlayers()) {
            player.sendMessage(ChatColor.DARK_RED + "The Crimson Descent has begun! " + ChatColor.WHITE + "Beware...");
            try { player.sendTitle(title, subtitle, 10, 80, 20); } catch (NoSuchMethodError ignored) {}
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
            player.playSound(player.getLocation(), Sound.MUSIC_DISC_11, SoundCategory.RECORDS, 1.2f, 0.9f);
            boss.addPlayer(player);

            spawnMobsAroundPlayer(world, player, chosen, 70);
        }

        // Spawn red sky particles periodically and remove effects after duration
        final DustOptions dust = new DustOptions(Color.fromRGB(200, 20, 20), 1.5f);
        new BukkitRunnable() {
            int ticksLeft = durationTicks;

            @Override
            public void run() {
                if (ticksLeft <= 0) {
                    // cleanup: restore weather and remove bossbar
                    boss.removeAll();
                    boss.setVisible(false);
                    Boolean s = prevStorm.remove(wid);
                    Boolean t = prevThundering.remove(wid);
                    Integer dur = prevWeatherDuration.remove(wid);
                    if (s != null) world.setStorm(s);
                    if (t != null) world.setThundering(t);
                    if (dur != null) world.setWeatherDuration(dur);
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
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void spawnMobsAroundPlayer(World world, Player player, EntityType type, int count) {
        Location base = player.getLocation();

        for (int i = 0; i < count; i++) {
            // choose a random angle and distance (10..30 blocks)
            double angle = rng.nextDouble() * Math.PI * 2.0;
            double dist = 10.0 + rng.nextDouble() * 20.0;
            double dx = Math.cos(angle) * dist;
            double dz = Math.sin(angle) * dist;

            Location spawnLoc;
            if (type == EntityType.PHANTOM) {
                double height = 20.0 + rng.nextDouble() * 10.0;
                spawnLoc = base.clone().add(dx, height, dz);
            } else {
                int tx = base.getBlockX() + (int) Math.round(dx);
                int tz = base.getBlockZ() + (int) Math.round(dz);
                int ty = world.getHighestBlockYAt(tx, tz);
                spawnLoc = new Location(world, tx + 0.5, ty + 1.0, tz + 0.5);
            }
            try {
                org.bukkit.entity.Entity ent = world.spawnEntity(spawnLoc, type);
                if (ent instanceof org.bukkit.entity.Creeper) {
                    com.example.worldsettings.mobs.HellCreeper.convert((org.bukkit.entity.Creeper) ent);
                } else if (ent instanceof org.bukkit.entity.Zombie) {
                    com.example.worldsettings.mobs.HellZombie.convert((org.bukkit.entity.Zombie) ent);
                } else if (ent instanceof org.bukkit.entity.Skeleton) {
                    com.example.worldsettings.mobs.HellSkeleton.convert((org.bukkit.entity.Skeleton) ent);
                } else if (ent instanceof org.bukkit.entity.Spider) {
                    com.example.worldsettings.mobs.HellSpider.convert((org.bukkit.entity.Spider) ent);
                } else if (ent instanceof org.bukkit.entity.Enderman) {
                    com.example.worldsettings.mobs.HellEnderman.convert((org.bukkit.entity.Enderman) ent);
                } else if (ent instanceof org.bukkit.entity.Phantom) {
                    com.example.worldsettings.mobs.HellPhantom.convert((org.bukkit.entity.Phantom) ent);
                }

                if (ent instanceof LivingEntity) {
                    LivingEntity mob = (LivingEntity) ent;
                    double boost = 4.0; // stronger Crimson mobs
                    try {
                        AttributeInstance hp = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        if (hp != null) {
                            double hpBase = Math.max(1.0, hp.getBaseValue());
                            double newBase = Math.max(1.0, hpBase * boost);
                            hp.setBaseValue(newBase);
                            mob.setHealth(newBase);
                        }
                    } catch (Throwable ignored) {}

                    int duration = 20 * 60 * 5; // 5 minutes
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, 2, true, false));
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 2, true, false));
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, 1, true, false));
                }
            } catch (Throwable t) {
                // ignore spawn failures for certain entity types or cramped spaces
            }
        }
    }

    private void spawnNightlyPhantoms(World world) {
        plugin.getLogger().info("Spawning nightly hell phantoms in world " + world.getName());
        for (Player player : world.getPlayers()) {
            int count = 3 + rng.nextInt(3);
            for (int i = 0; i < count; i++) {
                double angle = rng.nextDouble() * Math.PI * 2.0;
                double dist = 8.0 + rng.nextDouble() * 12.0;
                double dx = Math.cos(angle) * dist;
                double dz = Math.sin(angle) * dist;
                int tx = player.getLocation().getBlockX() + (int) Math.round(dx);
                int tz = player.getLocation().getBlockZ() + (int) Math.round(dz);
                int topY = world.getHighestBlockYAt(tx, tz);
                double y = Math.min(world.getMaxHeight() - 2, topY + 24.0 + rng.nextDouble() * 6.0);
                Location spawnLoc = new Location(world, tx + 0.5, y, tz + 0.5);

                try {
                    org.bukkit.entity.Entity ent = world.spawnEntity(spawnLoc, EntityType.PHANTOM);
                    if (ent instanceof org.bukkit.entity.Phantom) {
                        com.example.worldsettings.mobs.HellPhantom.convert((org.bukkit.entity.Phantom) ent);
                    }
                } catch (Throwable t) {
                    plugin.getLogger().warning("Failed to spawn nightly hell phantom at " + spawnLoc + ": " + t.getMessage());
                }
            }

            player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, SoundCategory.HOSTILE, 0.9f, 1.2f);
            player.sendMessage(ChatColor.RED + "Hell Phantoms stalk the night...");
        }
    }
}
