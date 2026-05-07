package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Helper for spawning and identifying the custom Hell Zombie.
 */
public final class HellZombie {

    public static final String METADATA_KEY = "HellZombie";
    public static final String CUSTOM_NAME = ChatColor.DARK_RED + "Hell Zombie";

    private HellZombie() {
        // utility class
    }

    public static Zombie spawn(World world, Location location) {
        Zombie zombie = (Zombie) world.spawnEntity(location, EntityType.ZOMBIE);
        return convert(zombie);
    }

    public static Zombie convert(Zombie zombie) {
        if (zombie == null || isHellZombie(zombie)) {
            return zombie;
        }

        zombie.setMetadata(METADATA_KEY, new FixedMetadataValue(WorldSettingsPlugin.getInstance(), true));
        zombie.setCustomName(CUSTOM_NAME);
        zombie.setCustomNameVisible(true);
        zombie.setAI(true);
        zombie.setRemoveWhenFarAway(false);

        AttributeInstance health = zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(40.0); // More health
            zombie.setHealth(40.0);
        }

        AttributeInstance speed = zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(0.4); // Super fast
        }

        AttributeInstance damage = zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(8.0); // More damage
        }

        return zombie;
    }

    public static boolean isHellZombie(org.bukkit.entity.Entity entity) {
        return entity instanceof Zombie && entity.hasMetadata(METADATA_KEY);
    }
}
