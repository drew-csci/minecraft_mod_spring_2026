package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Helper for spawning and identifying the custom Hell Creeper.
 */
public final class HellCreeper {

    public static final String METADATA_KEY = "HellCreeper";
    public static final String CUSTOM_NAME = ChatColor.DARK_RED + "Hell Creeper";
    public static final float EXPLOSION_POWER = 8.0f;

    private HellCreeper() {
        // utility class
    }

    public static Creeper spawn(World world, Location location) {
        Creeper creeper = (Creeper) world.spawnEntity(location, EntityType.CREEPER);
        return convert(creeper);
    }

    public static Creeper convert(Creeper creeper) {
        if (creeper == null || isHellCreeper(creeper)) {
            return creeper;
        }

        creeper.setMetadata(METADATA_KEY, new FixedMetadataValue(WorldSettingsPlugin.getInstance(), true));
        creeper.setCustomName(CUSTOM_NAME);
        creeper.setCustomNameVisible(true);
        creeper.setPowered(true);
        creeper.setAI(true);
        creeper.setRemoveWhenFarAway(false);

        AttributeInstance hp = creeper.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
        if (hp != null) {
            creeper.setHealth(Math.max(1.0, hp.getBaseValue()));
        }
        return creeper;
    }

    public static boolean isHellCreeper(org.bukkit.entity.Entity entity) {
        return entity instanceof Creeper && entity.hasMetadata(METADATA_KEY);
    }
}
