package com.example.worldsettings.boss;

import java.util.logging.Level;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

/**
 * Listens for boss death events and increments tier/defeat count.
 */
public class BossDefeatListener implements Listener {

    private final BossScalingManager manager;
    private final BossScalingStorage storage;
    private final Plugin plugin;

    public BossDefeatListener(BossScalingManager manager, BossScalingStorage storage, Plugin plugin) {
        this.manager = manager;
        this.storage = storage;
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        String bossKey = manager.getBossKey(entity);
        if (bossKey == null) {
            return; // Not a tracked boss
        }

        // Increment tier and persist immediately
        int newTier = manager.incrementDefeatCount(bossKey);
        storage.saveData(manager.exportData());

        plugin.getLogger().log(Level.INFO,
            "Boss defeated: " + bossKey + " (new tier: " + newTier + ")");
    }
}
