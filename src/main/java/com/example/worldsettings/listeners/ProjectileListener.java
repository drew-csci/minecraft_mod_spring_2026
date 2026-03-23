package com.example.worldsettings.listeners;

import com.example.worldsettings.ModdedItems;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Handles projectile impacts for special effects.
 * Specifically handles Exploding Arrow explosions.
 */
public class ProjectileListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            // Check if the arrow is an Exploding Arrow
            if (arrow.getItemStack() != null && arrow.getItemStack().hasItemMeta() &&
                arrow.getItemStack().getItemMeta().hasCustomModelData() &&
                arrow.getItemStack().getItemMeta().getCustomModelData() == 20) {

                // Create explosion at the hit location
                arrow.getWorld().createExplosion(arrow.getLocation(), 2.0f, false, false);
            }
        }
    }
}