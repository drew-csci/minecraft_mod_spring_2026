package com.example.worldsettings.mobs;

import com.example.worldsettings.WorldSettingsPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Enderman;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HellEnderman.
 */
public class HellEndermanTest {

    @Test
    public void testConvert() {
        WorldSettingsPlugin mockPlugin = mock(WorldSettingsPlugin.class);
        try (MockedStatic<WorldSettingsPlugin> mockedStatic = mockStatic(WorldSettingsPlugin.class)) {
            mockedStatic.when(WorldSettingsPlugin::getInstance).thenReturn(mockPlugin);

            Enderman mockEnderman = mock(Enderman.class);
            AttributeInstance mockHp = mock(AttributeInstance.class);
            AttributeInstance mockSpeed = mock(AttributeInstance.class);
            AttributeInstance mockDamage = mock(AttributeInstance.class);

            when(mockEnderman.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mockHp);
            when(mockEnderman.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).thenReturn(mockSpeed);
            when(mockEnderman.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).thenReturn(mockDamage);

            HellEnderman.convert(mockEnderman);

            verify(mockEnderman).setMetadata(eq(HellEnderman.METADATA_KEY), any());
            verify(mockEnderman).setCustomName(HellEnderman.CUSTOM_NAME);
            verify(mockEnderman).setCustomNameVisible(true);
            verify(mockEnderman).setAI(true);
            verify(mockEnderman).setRemoveWhenFarAway(false);
            verify(mockHp).setBaseValue(60.0);
            verify(mockEnderman).setHealth(60.0);
            verify(mockSpeed).setBaseValue(0.45);
            verify(mockDamage).setBaseValue(10.0);
        }
    }

    @Test
    public void testIsHellEnderman() {
        Enderman mockEnderman = mock(Enderman.class);

        when(mockEnderman.hasMetadata(HellEnderman.METADATA_KEY)).thenReturn(true);
        assertTrue(HellEnderman.isHellEnderman(mockEnderman));

        when(mockEnderman.hasMetadata(HellEnderman.METADATA_KEY)).thenReturn(false);
        assertFalse(HellEnderman.isHellEnderman(mockEnderman));
    }
}