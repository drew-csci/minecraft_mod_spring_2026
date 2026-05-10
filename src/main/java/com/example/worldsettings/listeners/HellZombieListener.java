package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.mobs.HellZombie;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Converts natural zombies into Hell Zombies.
 */
public class HellZombieListener implements Listener {
    private static final String MUSIC_TASK_KEY = "HellZombieMusicTask";
    private static final double DETECTION_RANGE = 30.0;

    @EventHandler
    public void onZombieSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.ZOMBIE) return;
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.NATURAL && reason != SpawnReason.CHUNK_GEN && reason != SpawnReason.DEFAULT) return;

        Zombie zombie = (Zombie) event.getEntity();
        HellZombie.convert(zombie);
        startZombieMusicTask(zombie);
    }

    private void startZombieMusicTask(Zombie zombie) {
        WorldSettingsPlugin plugin = WorldSettingsPlugin.getInstance();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!zombie.isValid()) {
                    cancel();
                    return;
                }

                // Check for nearby players and play scary music
                for (Player player : zombie.getWorld().getPlayers()) {
                    double distance = zombie.getLocation().distance(player.getLocation());
                    if (distance <= DETECTION_RANGE) {
                        player.playSound(player.getLocation(), Sound.MUSIC_DISC_CHIRP, SoundCategory.RECORDS, 0.5f, 0.8f);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 100L); // Run every 5 seconds
    }

    @EventHandler
    public void onHellZombieAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Zombie)) return;
        Zombie zombie = (Zombie) event.getDamager();
        if (!HellZombie.isHellZombie(zombie)) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            // Set player on fire (100 ticks = 5 seconds)
            player.setFireTicks(100);
            
            // Apply slowness effect (Slowness III for 10 seconds)
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2, false, false));

            // Insane knockback: send the player hard backwards, aiming for the nearest wall.
            Vector knockback = computeWallKnockback(player, zombie);
            player.setVelocity(knockback);
        }
    }

    private Vector computeWallKnockback(Player player, Zombie zombie) {
        Vector direction = player.getLocation().toVector().subtract(zombie.getLocation().toVector());
        if (direction.lengthSquared() < 0.0001) {
            direction = player.getEyeLocation().getDirection().multiply(-1.0);
        }
        direction.normalize();
        direction.setY(0.12);

        Vector wallDirection = findWallDirection(player, direction, 8);
        double speed = 3.5;
        if (wallDirection != null) {
            direction = wallDirection.normalize();
            direction.setY(0.12);
            speed = 5.0;
        }

        return direction.multiply(speed);
    }

    private Vector findWallDirection(Player player, Vector direction, int maxDistance) {
        Location test = player.getLocation().clone().add(0, player.getEyeHeight() * 0.5, 0);
        Vector step = direction.clone().normalize();
        for (int i = 1; i <= maxDistance; i++) {
            test.add(step);
            if (!test.getBlock().getType().isAir()) {
                return direction;
            }
        }
        return null;
    }
}


