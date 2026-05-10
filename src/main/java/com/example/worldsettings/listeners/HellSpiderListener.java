package com.example.worldsettings.listeners;

import com.example.worldsettings.mobs.HellSpider;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

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

    @EventHandler
    public void onHellSpiderAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Spider)) return;
        Spider spider = (Spider) event.getDamager();
        if (!HellSpider.isHellSpider(spider)) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            // Send player flying above the clouds
            Vector velocity = new Vector(0, 6.0, 0); // Extreme upward velocity to reach clouds
            player.setVelocity(velocity);
            
            // Play a Minecraft music disc sound
            player.playSound(player.getLocation(), Sound.MUSIC_DISC_11, SoundCategory.RECORDS, 1.0f, 1.0f);
        }
    }
}

