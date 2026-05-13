package com.example.worldsettings.listeners;

import com.example.worldsettings.mobs.HellPhantom;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class HellPhantomListener implements Listener {

    @EventHandler
    public void onPhantomSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.PHANTOM) return;
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.NATURAL && reason != SpawnReason.DEFAULT && reason != SpawnReason.SPAWNER_EGG && reason != SpawnReason.CUSTOM) return;

        if (event.getEntity() instanceof Phantom phantom) {
            HellPhantom.convert(phantom);
        }
    }

    @EventHandler
    public void onHellPhantomHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Phantom phantom)) return;
        if (!HellPhantom.isHellPhantom(phantom)) return;
        if (!(event.getEntity() instanceof Player player)) return;

        Location hitLoc = player.getLocation();

        // Create an annoying explosion effect that doesn't kill the player
        hitLoc.getWorld().createExplosion(hitLoc, 0.0f, false, false);
        hitLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, hitLoc, 2, 0.5, 0.5, 0.5, 0.0);
        hitLoc.getWorld().playSound(hitLoc, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.8f, 0.8f);

        Vector push = player.getLocation().toVector().subtract(phantom.getLocation().toVector()).normalize().multiply(2.8);
        push.setY(1.2);
        player.setVelocity(push);

        // Spawn extra hell phantoms each time the player is hit
        for (int i = 0; i < 2; i++) {
            double angle = Math.random() * Math.PI * 2.0;
            double distance = 3.0 + Math.random() * 4.0;
            double dx = Math.cos(angle) * distance;
            double dz = Math.sin(angle) * distance;
            Location spawnLocation = player.getLocation().clone().add(dx, 4.0 + Math.random() * 4.0, dz);
            if (spawnLocation.getBlock().getType().isAir()) {
                Phantom extra = (Phantom) player.getWorld().spawnEntity(spawnLocation, EntityType.PHANTOM);
                HellPhantom.convert(extra);
            }
        }
    }
}
