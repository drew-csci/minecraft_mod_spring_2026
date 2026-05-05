package com.example.worldsettings.listeners;

import com.example.worldsettings.mobs.HellZombie;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        }
    }
}

