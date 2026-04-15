package com.example.worldsettings.listeners;

import com.example.worldsettings.WorldSettingsPlugin;
import com.example.worldsettings.settings.WorldSettings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple manager that provides dodge behavior for hostile mobs.
 *
 * Mechanics:
 * - When a hostile mob (Monster) is damaged by a player, the manager may trigger a dodge
 *   if the WorldSettings toggle is enabled, the mob's per-mob cooldown has expired, and a
 *   probability roll succeeds.
 */


public class MobBehaviorManager implements Listener {


    private final JavaPlugin plugin;
    private final Random rng = new Random();
    private final Map<UUID, Long> lastDodgeMs = new ConcurrentHashMap<>();
    // Optional test-injected settings to avoid reaching into WorldSettingsPlugin during unit tests
    private final com.example.worldsettings.settings.WorldSettings testWorldSettings;

    // Registry of abilities (can be extended for per-mob-type in future)
    private final List<MobAbility> abilities = new ArrayList<>();


    public MobBehaviorManager(JavaPlugin plugin) {
        this(plugin, null, false);
        registerDefaultAbilities();
    }

    /**
     * Test-friendly constructor that allows skipping event registration.
     * When skipRegister is true, the manager will not register listeners with the server.
     */

    public MobBehaviorManager(JavaPlugin plugin, boolean skipRegister) {
        this(plugin, null, skipRegister);
        registerDefaultAbilities();
    }

    /**
     * Constructor with optional injected WorldSettings for easier unit testing.
     * When skipRegister is true, the manager will not register listeners with the server.
     */

    public MobBehaviorManager(JavaPlugin plugin, com.example.worldsettings.settings.WorldSettings testWorldSettings, boolean skipRegister) {
        this.plugin = plugin;
        this.testWorldSettings = testWorldSettings;
        if (!skipRegister && plugin != null) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
        registerDefaultAbilities();
    }

    /**
     * Register a mob ability (can be called by plugins or setup).
     */
    public void registerAbility(MobAbility ability) {
        abilities.add(ability);
    }

    /**
     * Register default abilities (dodge for now).
     */
    private void registerDefaultAbilities() {
        abilities.clear();
        abilities.add(new DodgeAbility());
        // Add more abilities here in the future (block breaking, climbing, etc)
    }


    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        if (!(entity instanceof Monster)) return; // only hostile mobs
        if (!(event.getDamager() instanceof Player)) return;
        LivingEntity mob = (LivingEntity) entity;
        Player attacker = (Player) event.getDamager();
        // Call all registered abilities
        for (MobAbility ability : abilities) {
            ability.onMobDamaged(mob, attacker, this);
        }
    }


    /**
     * Dodge ability implementation (modular).
     */
    public class DodgeAbility implements MobAbility {
        @Override
        public void onMobDamaged(LivingEntity mob, Player attacker, MobBehaviorManager context) {
            context.tryHandleDodge(mob, attacker);
        }
    }

    /**
     * Extracted dodge logic so tests can invoke it directly without constructing Bukkit events.
     */
    public void tryHandleDodge(LivingEntity mob, Player attacker) {
        WorldSettings ws = (testWorldSettings != null) ? testWorldSettings : WorldSettingsPlugin.getInstance().getWorldSettings();
        if (!ws.isEnhancedMobDodgeEnabled()) return;

        UUID id = mob.getUniqueId();
        long now = System.currentTimeMillis();
        long last = lastDodgeMs.getOrDefault(id, 0L);
        long cooldownMs = ws.getDodgeCooldownTicks() * 50L; // 1 tick = 50ms
        if (!canDodge(last, now, cooldownMs)) return;

        int chance = ws.getDodgeChancePercent();
        int roll = rng.nextInt(100) + 1; // 1..100
        if (!rollForDodge(chance, roll)) return;

        // Perform dodge: lateral + backward velocity relative to attacker->mob vector
        Vector attackerToMob = mob.getLocation().toVector().subtract(attacker.getLocation().toVector());
        if (attackerToMob.lengthSquared() <= 0.0001) {
            // fallback small random push
            mob.setVelocity(new Vector((rng.nextDouble() - 0.5) * 0.5, 0.2, (rng.nextDouble() - 0.5) * 0.5));
        } else {
            Vector dir = attackerToMob.normalize();
            // perpendicular vector on XZ plane
            Vector perp = new Vector(-dir.getZ(), 0, dir.getX()).normalize();
            double lateral = 0.6 + rng.nextDouble() * 0.4; // 0.6 - 1.0
            double backward = 0.2 + rng.nextDouble() * 0.4; // 0.2 - 0.6
            Vector vel = perp.multiply(lateral).add(dir.multiply(-backward));
            // small vertical lift
            vel.setY(0.2);
            mob.setVelocity(vel);
        }

        // mark cooldown
        lastDodgeMs.put(id, now);
    }

    // Helper for tests
    public static boolean rollForDodge(int chancePercent, int roll1To100) {
        return roll1To100 >= 1 && roll1To100 <= chancePercent;
    }

    public static boolean canDodge(long lastDodgeMs, long nowMs, long cooldownMs) {
        return (nowMs - lastDodgeMs) >= cooldownMs;
    }
}
