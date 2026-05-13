package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.progression.ProgressionManager;
import com.example.worldsettings.settings.WorldSettings;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Applies selected configurable world settings to gameplay behavior.
 */
public class WorldSettingsGameplayListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        WorldSettingsPlugin plugin = WorldSettingsPlugin.getInstance();
        WorldSettings settings = plugin.getWorldSettings();

        if (!(event.getEntity() instanceof Monster)) {
            return;
        }

        LivingEntity entity = event.getEntity();
        if (!isRegularScalableMob(entity)) {
            return;
        }

        double progressionScale = plugin.getProgressionManager().getAverageProgressionScale(entity.getWorld());
        scaleMobHealth(entity, progressionScale);

        if (!settings.isEnhancedMobs()) {
            return;
        }

        int speedAmplifier = switch (settings.getDifficultyLevel()) {
            case EASY -> 0;
            case MEDIUM -> 1;
            case HARD -> 2;
        };

        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, speedAmplifier, true, false));

        if (settings.isPostEndWorld()) {
            int strengthAmplifier = settings.getPostEndDifficultyBoost() >= 2.0 ? 1 : 0;
            entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, strengthAmplifier, true, false));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Monster)) {
            return;
        }

        LivingEntity damager = (LivingEntity) event.getDamager();
        if (!isRegularScalableMob(damager)) {
            return;
        }

        ProgressionManager progressionManager = WorldSettingsPlugin.getInstance().getProgressionManager();

        double progressionScale;
        if (event.getEntity() instanceof Player player) {
            progressionScale = progressionManager.getProgressionScale(player);
        } else {
            progressionScale = progressionManager.getAverageProgressionScale(damager.getWorld());
        }

        event.setDamage(event.getDamage() * progressionScale);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        WorldSettings settings = WorldSettingsPlugin.getInstance().getWorldSettings();

        if (!settings.isEnhancedLootDrops()) {
            return;
        }
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }

        int chanceRoll = ThreadLocalRandom.current().nextInt(100) + 1;
        if (chanceRoll > settings.getBloodMoonChancePercent()) {
            return;
        }

        ItemStack bonusDrop = settings.getDifficultyLevel() == WorldSettings.DifficultyLevel.HARD
            ? new ItemStack(Material.EMERALD, 2)
            : new ItemStack(Material.EMERALD, 1);

        event.getDrops().add(bonusDrop);

        if (settings.isBloodMoonEvents() && event.getEntityType() == EntityType.ZOMBIE) {
            event.getDrops().add(new ItemStack(Material.REDSTONE, 1));
        }
    }

    private static void scaleMobHealth(LivingEntity entity, double progressionScale) {
        AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        double oldMaxHealth = Math.max(1.0, maxHealth.getBaseValue());
        double newMaxHealth = Math.max(1.0, oldMaxHealth * progressionScale);

        maxHealth.setBaseValue(newMaxHealth);

        if (entity.getHealth() > newMaxHealth) {
            entity.setHealth(newMaxHealth);
        }
    }

    private static boolean isRegularScalableMob(LivingEntity entity) {
        EntityType type = entity.getType();

        return type != EntityType.ENDER_DRAGON
            && type != EntityType.WITHER;
    }
}