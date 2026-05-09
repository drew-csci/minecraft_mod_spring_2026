package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Helper for spawning and identifying the custom Hell Enderman.
 */
public final class HellEnderman {

    public static final String METADATA_KEY = "HellEnderman";
    public static final String CUSTOM_NAME = ChatColor.DARK_RED + "Hell Enderman";

    private HellEnderman() {
        // utility class
    }

    public static Enderman spawn(World world, Location location) {
        Enderman enderman = (Enderman) world.spawnEntity(location, EntityType.ENDERMAN);
        return convert(enderman);
    }

    public static Enderman convert(Enderman enderman) {
        if (enderman == null || isHellEnderman(enderman)) {
            return enderman;
        }

        enderman.setMetadata(METADATA_KEY, new FixedMetadataValue(WorldSettingsPlugin.getInstance(), true));
        enderman.setCustomName(CUSTOM_NAME);
        enderman.setCustomNameVisible(true);
        enderman.setAI(true);
        enderman.setRemoveWhenFarAway(false);

        AttributeInstance health = enderman.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(60.0); // More health
            enderman.setHealth(60.0);
        }

        AttributeInstance speed = enderman.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(0.45); // Super fast
        }

        AttributeInstance damage = enderman.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(10.0); // More damage
        }

        return enderman;
    }

    public static boolean isHellEnderman(org.bukkit.entity.Entity entity) {
        return entity instanceof Enderman && entity.hasMetadata(METADATA_KEY);
    }
}
