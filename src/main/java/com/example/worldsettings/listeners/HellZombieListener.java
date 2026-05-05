package com.example.worldsettings.listeners;

import com.example.worldsettings.mobs.HellZombie;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * Converts natural zombies into Hell Zombies.
 */
public class HellZombieListener implements Listener {

    @EventHandler
    public void onZombieSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.ZOMBIE) return;
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.NATURAL && reason != SpawnReason.CHUNK_GEN && reason != SpawnReason.DEFAULT) return;

        Zombie zombie = (Zombie) event.getEntity();
        HellZombie.convert(zombie);
    }
}
