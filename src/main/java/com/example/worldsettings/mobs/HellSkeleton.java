package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Helper for spawning and identifying the custom Hell Skeleton.
 */
public final class HellSkeleton {

    public static final String METADATA_KEY = "HellSkeleton";
    public static final String CUSTOM_NAME = ChatColor.DARK_RED + "Hell Skeleton";
    public static final double ARROW_SPEED_MULTIPLIER = 5.0; // Incredible fast
    public static final double ARROW_DAMAGE_MULTIPLIER = 100.0; // One shot kill
    public static final float EXPLOSION_POWER = 4.0f; // Explosion on impact

    private HellSkeleton() {
        // utility class
    }

    public static Skeleton spawn(World world, Location location) {
        Skeleton skeleton = (Skeleton) world.spawnEntity(location, EntityType.SKELETON);
        return convert(skeleton);
    }

    public static Skeleton convert(Skeleton skeleton) {
        if (skeleton == null || isHellSkeleton(skeleton)) {
            return skeleton;
        }

        skeleton.setMetadata(METADATA_KEY, new FixedMetadataValue(WorldSettingsPlugin.getInstance(), true));
        skeleton.setCustomName(CUSTOM_NAME);
        skeleton.setCustomNameVisible(true);
        skeleton.setAI(true);
        skeleton.setRemoveWhenFarAway(false);

        AttributeInstance health = skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(30.0); // More health
            skeleton.setHealth(30.0);
        }

        AttributeInstance speed = skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(0.35); // Super fast (skeleton itself)
        }

        AttributeInstance damage = skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(5.0); // More damage (melee, if applicable)
        }

        return skeleton;
    }

    public static boolean isHellSkeleton(org.bukkit.entity.Entity entity) {
        return entity instanceof Skeleton && entity.hasMetadata(METADATA_KEY);
    }
}
