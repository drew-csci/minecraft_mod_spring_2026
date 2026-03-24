package com.example.worldsettings.mobs;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class HellCreeperSpawner {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    // List of funny music discs
    private final Sound[] funnySongs = new Sound[]{
        Sound.MUSIC_DISC_CAT,
        Sound.MUSIC_DISC_BLOCKS,
        Sound.MUSIC_DISC_CHIRP,
        Sound.MUSIC_DISC_MALL,
        Sound.MUSIC_DISC_MELLOHI
    };

    public HellCreeperSpawner(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void spawnHellCreeper(Location loc) {
        // Spawn creeper
        Creeper creeper = loc.getWorld().spawn(loc, Creeper.class);
        creeper.setCustomName("§cHELL CREEPER");
        creeper.setCustomNameVisible(true);
        creeper.setPowered(true); // bigger, scarier

        // Fireproof (optional)
        creeper.setFireTicks(0);

        // Particle aura (lava + smoke)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!creeper.isDead()) {
                    loc.getWorld().spawnParticle(Particle.DRIP_LAVA, creeper.getLocation().add(0, 1, 0), 15, 0.5, 1, 0.5, 0.05);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, creeper.getLocation().add(0, 1, 0), 10, 0.5, 1, 0.5, 0.02);
                } else cancel();
            }
        }.runTaskTimer(plugin, 0, 2);

        // Schedule huge explosion after 3 seconds (60 ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!creeper.isDead()) {
                    Location explosionLoc = creeper.getLocation();

                    // MASSIVE explosion
                    loc.getWorld().createExplosion(explosionLoc, 50f, true, true);

                    // Play a random funny music disc
                    Sound song = funnySongs[random.nextInt(funnySongs.length)];
                    loc.getWorld().playSound(explosionLoc, song, 1.0f, 1.0f);

                    creeper.remove();
                }
            }
        }.runTaskLater(plugin, 60L);
    }
}