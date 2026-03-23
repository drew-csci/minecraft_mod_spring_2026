package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Listener that activates a "post-end" phase when a player kills the Ender Dragon.
 * During the post-end phase newly spawned mobs are enhanced.
 *
 * Control notes:
 * - The post-end phase is represented by the `postEndWorld` flag in `WorldSettings`.
 * - You can programmatically enable/disable the phase via `WorldSettings.togglePostEndWorld()` or
 *   `WorldSettings.setPostEndWorld(boolean)`.
 * - If you want a GUI control to toggle the post-end phase, add a menu item in `SettingsGUI` that
 *   calls the setters above.
 * - The enhancements applied to mobs are guarded by `WorldSettings.isEnhancedMobs()`. If that flag
 *   is false, the listener leaves mob attributes alone.
 */
public class PostEndListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.ENDER_DRAGON) return;

        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
        // Activate post-end phase if not already active
        if (!ws.isPostEndWorld()) {
            ws.setPostEndWorld(true);
            killer.sendMessage(ChatColor.GOLD + "Post-End phase activated! "+ChatColor.WHITE+"Mobs will be stronger.");
            // Send a prominent title and play a sound for feedback
            try {
                killer.sendTitle(ChatColor.DARK_RED + "Post-End Phase", ChatColor.YELLOW + "Mobs have been empowered!", 10, 70, 20);
            } catch (NoSuchMethodError ignored) {
                // Older server APIs may not have sendTitle overloads; ignore if absent
            }
            killer.playSound(killer.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // Only enhance living mobs when post-end is active
        WorldSettings ws = WorldSettingsPlugin.getInstance().getWorldSettings();
        if (!ws.isPostEndWorld()) return;

        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity mob = (LivingEntity) event.getEntity();

        // Don't modify players or the Ender Dragon itself
        if (mob instanceof Player) return;
        if (mob.getType() == EntityType.ENDER_DRAGON) return;

        double boost = ws.getPostEndDifficultyBoost(); // expected 1.0 -> 3.0

        // Increase max health by boost factor where possible
        try {
            AttributeInstance hp = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (hp != null) {
                double base = Math.max(1.0, hp.getBaseValue());
                double newBase = Math.max(1.0, base * boost);
                hp.setBaseValue(newBase);
                mob.setHealth(Math.min(newBase, mob.getHealth()));
            }
        } catch (Throwable ignored) {
            // Some entities or server versions may not expose attributes consistently—ignore failures
        }

        // Add potion effects scaled by the boost value
        int amplifier = Math.max(0, (int) Math.round(boost) - 1); // 1.0->0, 2.0->1, 3.0->2
        int duration = 20 * 60 * 5; // 5 minutes

        mob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, amplifier, true, false));
        mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier, true, false));
        mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, Math.max(0, amplifier-1), true, false));
    }
}
