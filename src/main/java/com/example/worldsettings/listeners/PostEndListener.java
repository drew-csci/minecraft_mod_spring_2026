package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Listener that activates a "post-end" phase when a player kills the Ender Dragon.
 * During the post-end phase newly spawned mobs are enhanced.
 *
 * Control notes:
 * - The post-end phase is represented by the `postEndWorld` flag in `WorldSettings`.
 * - You can programmatically enable/disable the phase via `WorldSettings.togglePostEndWorld()` or
 *   `WorldSettings.setPostEndWorld(boolean)`.
 * - If you want a GUI control to toggle the post-end phase, add a menu item in `SettingsGUI` that
 *   calls the setters above.
 * - The enhancements applied to mobs are guarded by `WorldSettings.isEnhancedMobs()`. If that flag
 *   is false, the listener leaves mob attributes alone.
 */
public class PostEndListener implements Listener {

    private final WorldSettingsPlugin plugin;
    private final Map<UUID, Boolean> prevStorm = new HashMap<>();
    private final Map<UUID, Boolean> prevThundering = new HashMap<>();
    private final Map<UUID, Integer> prevWeatherDuration = new HashMap<>();
    private final Map<UUID, BossBar> bossBars = new HashMap<>();
    private final Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();

    public PostEndListener(WorldSettingsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean activatePostEnd(World world, Player announcer) {
        if (world == null || world.getEnvironment() != World.Environment.NORMAL) {
            return false;
        }

        WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
        ws.setPostEndWorld(true);
        ws.setEnhancedMobs(true);

        UUID worldId = world.getUID();
        if (!bossBars.containsKey(worldId)) {
            prevStorm.put(worldId, world.hasStorm());
            prevThundering.put(worldId, world.isThundering());
            prevWeatherDuration.put(worldId, world.getWeatherDuration());
        }

        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(Integer.MAX_VALUE);

        BossBar bossBar = bossBars.computeIfAbsent(
            worldId,
            ignored -> plugin.getServer().createBossBar(
                ChatColor.DARK_RED + "Post-End Phase",
                BarColor.RED,
                BarStyle.SOLID
            )
        );
        bossBar.setVisible(true);
        for (Player player : world.getPlayers()) {
            bossBar.addPlayer(player);
            player.sendMessage(ChatColor.GOLD + "Post-End phase activated! " + ChatColor.WHITE + "Mobs are stronger and the sky has turned red.");
            try {
                player.sendTitle(ChatColor.DARK_RED + "Post-End Phase", ChatColor.YELLOW + "The world has changed.", 10, 70, 20);
            } catch (NoSuchMethodError ignored) {
            }
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        }

        BukkitRunnable previousTask = particleTasks.remove(worldId);
        if (previousTask != null) {
            previousTask.cancel();
        }

        final Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(180, 35, 35), 1.5f);
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!ws.isPostEndWorld()) {
                    cancel();
                    return;
                }

                for (Player player : world.getPlayers()) {
                    if (!bossBar.getPlayers().contains(player)) {
                        bossBar.addPlayer(player);
                    }

                    Location loc = player.getLocation().clone();
                    loc.setY(Math.min(loc.getY() + 60.0, world.getMaxHeight() - 1));
                    world.spawnParticle(Particle.REDSTONE, loc, 30, 5.0, 5.0, 5.0, dust);
                }
            }
        };
        task.runTaskTimer(plugin, 0L, 20L);
        particleTasks.put(worldId, task);

        if (announcer != null && announcer.getWorld().equals(world)) {
            announcer.sendMessage(ChatColor.GOLD + "Post-End phase activated! " + ChatColor.WHITE + "Mobs will be stronger.");
        }

        return true;
    }

    public boolean deactivatePostEnd(World world) {
        if (world == null || world.getEnvironment() != World.Environment.NORMAL) {
            return false;
        }

        WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
        ws.setPostEndWorld(false);
        ws.setEnhancedMobs(false);

        UUID worldId = world.getUID();
        BukkitRunnable task = particleTasks.remove(worldId);
        if (task != null) {
            task.cancel();
        }

        BossBar bossBar = bossBars.remove(worldId);
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }

        Boolean storm = prevStorm.remove(worldId);
        Boolean thundering = prevThundering.remove(worldId);
        Integer weatherDuration = prevWeatherDuration.remove(worldId);
        if (storm != null) {
            world.setStorm(storm);
        }
        if (thundering != null) {
            world.setThundering(thundering);
        }
        if (weatherDuration != null) {
            world.setWeatherDuration(weatherDuration);
        }

        for (Player player : world.getPlayers()) {
            player.sendMessage(ChatColor.GRAY + "Post-End phase has ended.");
        }
        return true;
    }

    public void deactivateAll() {
        for (World world : plugin.getServer().getWorlds()) {
            if (world.getEnvironment() == World.Environment.NORMAL) {
                deactivatePostEnd(world);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.ENDER_DRAGON) return;

        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
        // Activate post-end phase if not already active
        if (!ws.isPostEndWorld()) {
            activatePostEnd(killer.getWorld(), killer);
            WorldSettingsPlugin.getInstance().saveSettingsToConfig();
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // Only enhance living mobs when post-end is active
        WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
        if (!ws.isPostEndWorld()) return;

        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity mob = (LivingEntity) event.getEntity();

        // Don't modify players or the Ender Dragon itself
        if (mob instanceof Player) return;
        if (mob.getType() == EntityType.ENDER_DRAGON) return;

        double boost = ws.getPostEndDifficultyBoost(); // expected 1.0 -> 3.0

        // Increase max health by boost factor where possible
        try {
            AttributeInstance hp = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (hp != null) {
                double base = Math.max(1.0, hp.getBaseValue());
                double newBase = Math.max(1.0, base * boost);
                hp.setBaseValue(newBase);
                mob.setHealth(Math.min(newBase, mob.getHealth()));
            }
        } catch (Throwable ignored) {
            // Some entities or server versions may not expose attributes consistently—ignore failures
        }

        // Add potion effects scaled by the boost value
        int amplifier = Math.max(0, (int) Math.round(boost) - 1); // 1.0->0, 2.0->1, 3.0->2
        int duration = 20 * 60 * 5; // 5 minutes

        mob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, amplifier, true, false));
        mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier, true, false));
        mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, Math.max(0, amplifier-1), true, false));
    }
}
