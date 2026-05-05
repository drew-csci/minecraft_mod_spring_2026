package com.example.worldsettings.listeners;

import com.example.worldsettings.mobs.HellSpider;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * Converts natural spiders into Hell Spiders.
 */
public class HellSpiderListener implements Listener {

    @EventHandler
    public void onSpiderSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.SPIDER) return;
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.NATURAL && reason != SpawnReason.CHUNK_GEN && reason != SpawnReason.DEFAULT) return;

        Spider spider = (Spider) event.getEntity();
        HellSpider.convert(spider);
    }
}
