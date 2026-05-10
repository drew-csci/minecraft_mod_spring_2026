package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.mobs.HellEnderman;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * Converts natural endermen into Hell Endermen and leaves fire trails.
 */
public class HellEndermanListener implements Listener {

    @EventHandler
    public void onEndermanSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.ENDERMAN) return;
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.NATURAL && reason != SpawnReason.CHUNK_GEN && reason != SpawnReason.DEFAULT) return;

        Enderman enderman = (Enderman) event.getEntity();
        HellEnderman.convert(enderman);
        
        // Start fire trail task for this enderman
        startFireTrailTask(enderman);
    }

    private void startFireTrailTask(Enderman enderman) {
        WorldSettingsPlugin plugin = WorldSettingsPlugin.getInstance();
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            // Check if enderman is still alive
            if (!enderman.isValid() || !HellEnderman.isHellEnderman(enderman)) {
                return;
            }
            
            // Leave fire trail as enderman moves
            Location loc = enderman.getLocation();
            Block block = loc.getBlock();
            Block below = block.getRelative(BlockFace.DOWN);
            
            if (block.getType() == Material.AIR && below.getType().isSolid()) {
                block.setType(Material.FIRE);
            }
        }, 0L, 5L); // Run every 5 ticks (0.25 seconds)
    }
}


