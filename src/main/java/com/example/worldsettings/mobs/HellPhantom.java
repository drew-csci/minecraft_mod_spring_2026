package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.metadata.FixedMetadataValue;

public final class HellPhantom {

    public static final String METADATA_KEY = "HellPhantom";
    public static final String CUSTOM_NAME = ChatColor.DARK_RED + "Hell Phantom";

    private HellPhantom() {
        // utility class
    }

    public static Phantom spawn(World world, Location location) {
        Phantom phantom = (Phantom) world.spawnEntity(location, EntityType.PHANTOM);
        return convert(phantom);
    }

    public static Phantom convert(Phantom phantom) {
        if (phantom == null || isHellPhantom(phantom)) {
            return phantom;
        }

        phantom.setMetadata(METADATA_KEY, new FixedMetadataValue(WorldSettingsPlugin.getInstance(), true));
        phantom.setCustomName(CUSTOM_NAME);
        phantom.setCustomNameVisible(true);
        phantom.setAI(true);
        phantom.setRemoveWhenFarAway(false);

        AttributeInstance health = phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(30.0);
            phantom.setHealth(30.0);
        }

        AttributeInstance speed = phantom.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(0.55);
        }

        AttributeInstance reach = phantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
        if (reach != null) {
            reach.setBaseValue(50.0);
        }

        return phantom;
    }

    public static boolean isHellPhantom(org.bukkit.entity.Entity entity) {
        return entity instanceof Phantom && entity.hasMetadata(METADATA_KEY);
    }
}
