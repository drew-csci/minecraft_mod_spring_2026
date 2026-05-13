package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Zombie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HellZombie.
 */
public class HellZombieTest {

    @Test
    public void testConvert() {
        WorldSettingsPlugin mockPlugin = mock(WorldSettingsPlugin.class);
        try (MockedStatic<WorldSettingsPlugin> mockedStatic = mockStatic(WorldSettingsPlugin.class)) {
            mockedStatic.when(WorldSettingsPlugin::getInstance).thenReturn(mockPlugin);

            Zombie mockZombie = mock(Zombie.class);
            AttributeInstance mockHp = mock(AttributeInstance.class);
            AttributeInstance mockSpeed = mock(AttributeInstance.class);
            AttributeInstance mockDamage = mock(AttributeInstance.class);

            when(mockZombie.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mockHp);
            when(mockZombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).thenReturn(mockSpeed);
            when(mockZombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).thenReturn(mockDamage);

            HellZombie.convert(mockZombie);

            verify(mockZombie).setMetadata(eq(HellZombie.METADATA_KEY), any());
            verify(mockZombie).setCustomName(HellZombie.CUSTOM_NAME);
            verify(mockZombie).setCustomNameVisible(true);
            verify(mockZombie).setAI(true);
            verify(mockZombie).setRemoveWhenFarAway(false);
            verify(mockHp).setBaseValue(40.0);
            verify(mockZombie).setHealth(40.0);
            verify(mockSpeed).setBaseValue(0.4);
            verify(mockDamage).setBaseValue(8.0);
        }
    }

    @Test
    public void testIsHellZombie() {
        Zombie mockZombie = mock(Zombie.class);

        when(mockZombie.hasMetadata(HellZombie.METADATA_KEY)).thenReturn(true);
        assertTrue(HellZombie.isHellZombie(mockZombie));

        when(mockZombie.hasMetadata(HellZombie.METADATA_KEY)).thenReturn(false);
        assertFalse(HellZombie.isHellZombie(mockZombie));
    }
}