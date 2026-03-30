package com.example.worldsettings.boss;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * Listens for boss spawns and applies scaled HP/damage based on current tier.
 */
public class BossSpawnListener implements Listener {

    private final BossScalingManager manager;

    public BossSpawnListener(BossScalingManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity entity = (LivingEntity) event.getEntity();
        String bossKey = manager.getBossKey(entity);
        if (bossKey == null) {
            return; // Not a tracked boss
        }

        int tier = manager.getTier(bossKey);
        if (tier <= 0) {
            return; // No scaling for first encounters
        }

        applyScaling(entity, tier);
    }

    /**
     * Apply scaling multipliers to boss HP and damage.
     */
    private void applyScaling(LivingEntity entity, int tier) {
        double healthMult = manager.getHealthMultiplier(tier);
        double damageMult = manager.getDamageMultiplier(tier);

        // Apply health scaling
        AttributeInstance healthAttr = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttr != null) {
            double baseHealth = healthAttr.getBaseValue();
            double scaledHealth = baseHealth * healthMult;
            healthAttr.setBaseValue(scaledHealth);
            entity.setHealth(scaledHealth); // Full heal on spawn
        }

        // Apply damage scaling (attack damage attribute if available)
        AttributeInstance damageAttr = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (damageAttr != null) {
            double baseDamage = damageAttr.getBaseValue();
            double scaledDamage = baseDamage * damageMult;
            damageAttr.setBaseValue(scaledDamage);
        }
    }
}
