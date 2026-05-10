package com.example.worldsettings.listeners;

import com.example.worldsettings.mobs.HellCreeper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * Converts natural creepers into Hell Creepers and handles Hell Creeper explosions.
 */
public class HellCreeperListener implements Listener {

    @EventHandler
    public void onCreeperSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.CREEPER) return;
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.NATURAL && reason != SpawnReason.CHUNK_GEN && reason != SpawnReason.DEFAULT) return;

        Creeper creeper = (Creeper) event.getEntity();
        HellCreeper.convert(creeper);
    }

    @EventHandler
    public void onHellCreeperExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof Creeper)) return;
        if (!HellCreeper.isHellCreeper(event.getEntity())) return;

        event.setCancelled(true);
        Location center = event.getLocation();
        center.getWorld().playSound(center, Sound.MUSIC_DISC_BLOCKS, SoundCategory.HOSTILE, 2.0f, 1.0f);
        center.getWorld().createExplosion(center, HellCreeper.EXPLOSION_POWER, true, true);
        leaveFireTrail(center, 10);
        spreadFire(center, 15);
    }

    private void leaveFireTrail(Location center, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (Math.abs(x) + Math.abs(z) > radius) continue;
                Location target = center.clone().add(x, 0, z);
                Block block = target.getBlock();
                Block below = block.getRelative(BlockFace.DOWN);
                if (block.getType() == Material.AIR && below.getType().isSolid()) {
                    block.setType(Material.FIRE);
                }
            }
        }
    }

    private void spreadFire(Location center, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) + Math.abs(z) > radius) continue;
                    Location target = center.clone().add(x, y, z);
                    Block block = target.getBlock();
                    if (block.getType().isBurnable()) {
                        block.setType(Material.FIRE);
                    }
                }
            }
        }
    }
}
