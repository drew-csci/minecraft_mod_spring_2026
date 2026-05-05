package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Helper for spawning and identifying the custom Hell Spider.
 */
public final class HellSpider {

    public static final String METADATA_KEY = "HellSpider";
    public static final String CUSTOM_NAME = ChatColor.DARK_RED + "Hell Spider";

    private HellSpider() {
        // utility class
    }

    public static Spider spawn(World world, Location location) {
        Spider spider = (Spider) world.spawnEntity(location, EntityType.SPIDER);
        return convert(spider);
    }

    public static Spider convert(Spider spider) {
        if (spider == null || isHellSpider(spider)) {
            return spider;
        }

        spider.setMetadata(METADATA_KEY, new FixedMetadataValue(WorldSettingsPlugin.getInstance(), true));
        spider.setCustomName(CUSTOM_NAME);
        spider.setCustomNameVisible(true);
        spider.setAI(true);
        spider.setRemoveWhenFarAway(false);

        AttributeInstance health = spider.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(32.0); // More health
            spider.setHealth(32.0);
        }

        AttributeInstance speed = spider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(0.4); // Super fast
        }

        AttributeInstance damage = spider.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(6.0); // More damage
        }

        return spider;
    }

    public static boolean isHellSpider(org.bukkit.entity.Entity entity) {
        return entity instanceof Spider && entity.hasMetadata(METADATA_KEY);
    }
}
