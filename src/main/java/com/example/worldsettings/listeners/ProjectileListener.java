package com.example.worldsettings.listeners;

import com.example.worldsettings.ModdedItems;
import com.example.worldsettings.WorldSettingsPlugin;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Handles projectile impacts for special effects.
 * Specifically handles Exploding Arrow explosions.
 */
public class ProjectileListener implements Listener {

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player && event.getProjectile() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getProjectile();
            ItemStack ammo = event.getConsumable();

            if (ammo != null && ammo.hasItemMeta() && ammo.getItemMeta().hasCustomModelData() &&
                ammo.getItemMeta().getCustomModelData() == 20) {
                arrow.setMetadata("ExplodingArrow", new FixedMetadataValue(WorldSettingsPlugin.getInstance(), true));
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if (arrow.hasMetadata("ExplodingArrow")) {
                arrow.getWorld().createExplosion(arrow.getLocation(), 2.0f, false, false);
            }
        }
    }
}