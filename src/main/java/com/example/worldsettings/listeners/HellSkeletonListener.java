package com.example.worldsettings.listeners;

import com.example.worldsettings.mobs.HellSkeleton;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

/**
 * Converts natural skeletons into Hell Skeletons and handles Hell Skeleton arrow behavior.
 */
public class HellSkeletonListener implements Listener {

    @EventHandler
    public void onSkeletonSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.SKELETON) return;
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.NATURAL && reason != SpawnReason.CHUNK_GEN && reason != SpawnReason.DEFAULT) return;

        Skeleton skeleton = (Skeleton) event.getEntity();
        HellSkeleton.convert(skeleton);
    }

    @EventHandler
    public void onHellSkeletonShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Skeleton)) return;
        Skeleton shooter = (Skeleton) event.getEntity();
        if (!HellSkeleton.isHellSkeleton(shooter)) return;

        if (event.getProjectile() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getProjectile();
            
            // Set arrow speed
            Vector velocity = arrow.getVelocity();
            arrow.setVelocity(velocity.multiply(HellSkeleton.ARROW_SPEED_MULTIPLIER));

            // Set arrow damage (one-shot kill for players)
            arrow.setDamage(arrow.getDamage() * HellSkeleton.ARROW_DAMAGE_MULTIPLIER);
            arrow.setCritical(true); // Ensures critical hit damage
            arrow.setFireTicks(20 * 5); // Flame arrows (5 seconds of fire)

            // Make arrows large (visual effect, needs further client-side mod for true large arrows)
            // For now, this is a placeholder or can be simulated with particles on hit if desired.
            // No direct API for large arrows in Bukkit without client modifications.

            // Target accuracy: For a simple implementation, we can aim directly at the player
            // For more advanced accuracy, a custom AI behavior would be needed.
            LivingEntity target = shooter.getTarget();
            if (target instanceof Player) {
                Location targetLoc = target.getEyeLocation();
                Location shooterLoc = shooter.getEyeLocation();
                Vector direction = targetLoc.toVector().subtract(shooterLoc.toVector()).normalize();
                arrow.setVelocity(direction.multiply(arrow.getVelocity().length()));
            }
        }
    }

    @EventHandler
    public void onHellSkeletonArrowHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;
        if (!(event.getEntity().getShooter() instanceof Skeleton)) return;

        Skeleton shooter = (Skeleton) event.getEntity().getShooter();
        if (!HellSkeleton.isHellSkeleton(shooter)) return;

        Arrow arrow = (Arrow) event.getEntity();
        
        // Explode on impact
        Location impactLocation = arrow.getLocation();
        impactLocation.getWorld().createExplosion(impactLocation, HellSkeleton.EXPLOSION_POWER, true, true);
        
        arrow.remove(); // Remove the arrow after explosion
    }
}
