package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.settings.WorldSettings;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
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
        WorldSettings settings = WorldSettingsPlugin.getInstance().getWorldSettings();

        if (!settings.isEnhancedMobs()) {
            return;
        }
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }

        LivingEntity entity = event.getEntity();

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
}
