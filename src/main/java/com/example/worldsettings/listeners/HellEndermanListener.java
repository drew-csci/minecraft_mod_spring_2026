package com.example.worldsettings.listeners;

import com.example.worldsettings.mobs.HellEnderman;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * Converts natural endermen into Hell Endermen.
 */
public class HellEndermanListener implements Listener {

    @EventHandler
    public void onEndermanSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.ENDERMAN) return;
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.NATURAL && reason != SpawnReason.CHUNK_GEN && reason != SpawnReason.DEFAULT) return;

        Enderman enderman = (Enderman) event.getEntity();
        HellEnderman.convert(enderman);
    }
}
